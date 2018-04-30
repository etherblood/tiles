package com.etherblood.sandbox;

import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.EntityPool;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.entities.UniqueComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventDispatcher;
import com.etherblood.events.EventQueue;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.rules.RandomTracker;
import com.etherblood.rules.abilities.ActionAggregator;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnAction;
import com.etherblood.rules.abilities.endTurn.PassTurnGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnHandler;
import com.etherblood.rules.abilities.walk.WalkAction;
import com.etherblood.rules.abilities.walk.WalkGenerator;
import com.etherblood.rules.abilities.walk.WalkHandler;
import com.etherblood.rules.context.DebugEntityBuilder;
import com.etherblood.rules.context.ElementComponentMaps;
import com.etherblood.rules.context.StatComponentMaps;
import com.etherblood.rules.game.GameStartEvent;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndEvent;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartEvent;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.movement.Coordinates;
import com.etherblood.rules.movement.SetPositionEvent;
import com.etherblood.rules.movement.SetPositionHandler;
import com.etherblood.rules.stats.actionpoints.RefreshAllActionPointsHandler;
import com.etherblood.rules.stats.actionpoints.ResetActiveActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.ResetActiveActionPointsHandler;
import com.etherblood.rules.stats.actionpoints.SetActiveActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.SetActiveActionPointsHandler;
import com.etherblood.rules.stats.actionpoints.UpdateBuffedActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.UpdateBuffedActionPointsHandler;
import com.etherblood.rules.stats.health.RefreshAllHealthHandler;
import com.etherblood.rules.stats.health.ResetActiveHealthEvent;
import com.etherblood.rules.stats.health.ResetActiveHealthHandler;
import com.etherblood.rules.stats.health.SetActiveHealthEvent;
import com.etherblood.rules.stats.health.SetActiveHealthHandler;
import com.etherblood.rules.stats.health.UpdateBuffedHealthEvent;
import com.etherblood.rules.stats.health.UpdateBuffedHealthHandler;
import com.etherblood.rules.stats.movepoints.RefreshAllMovePointsHandler;
import com.etherblood.rules.stats.movepoints.ResetActiveMovePointsEvent;
import com.etherblood.rules.stats.movepoints.ResetActiveMovePointsHandler;
import com.etherblood.rules.stats.movepoints.SetActiveMovePointsEvent;
import com.etherblood.rules.stats.movepoints.SetActiveMovePointsHandler;
import com.etherblood.rules.stats.movepoints.UpdateBuffedMovePointsEvent;
import com.etherblood.rules.stats.movepoints.UpdateBuffedMovePointsHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.awt.Point;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class Game {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void run() throws IOException {
        System.setProperty("org.slf4j.simpleLogger.logFile", "System.out");
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
        String matchName = "matchName";
        Function<Class<?>, Logger> loggerFactory = clazz -> LoggerFactory.getLogger(matchName + " " + clazz.getSimpleName());

        Object empty = new Object();
        int mapWidth = 10;
        int mapHeight = 10;
        List<String> playerNames = new ArrayList<>();
        playerNames.add("player1");
        playerNames.add("player2");
        playerNames.add("bot");

        RandomTracker random = new RandomTracker(new Random(453));
        EntityPool entities = new EntityPool(random::nextInt);

        DebugEntityBuilder builder = new DebugEntityBuilder();

        SimpleComponentMap controlledBy = builder.simple("controlledBy", playerNames::get);
        SimpleComponentMap memberOf = builder.simple("memberOf");
        SimpleComponentMap nextTeam = builder.simple("nextTeam");
        SimpleComponentMap spriteId = builder.simple("spriteId");
        UniqueComponentMap mapSize = builder.unique("mapSize", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
        SimpleComponentMap mapObstacle = builder.simple("mapObstacle", x -> empty);
        SimpleComponentMap position = builder.simple("position", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
        SimpleComponentMap buffOn = builder.simple("buffOn");
        SimpleComponentMap activeTurn = builder.simple("activeTurn", x -> empty);

        StatComponentMaps health = builder.stats("health");
        StatComponentMaps actionPoints = builder.stats("actionPoints");
        StatComponentMaps movePoints = builder.stats("movePoints");

        ElementComponentMaps normal = builder.element("normal");
        ElementComponentMaps fight = builder.element("fight");
        ElementComponentMaps flying = builder.element("flying");
        ElementComponentMaps poison = builder.element("poison");
        ElementComponentMaps ground = builder.element("ground");
        ElementComponentMaps rock = builder.element("rock");
        ElementComponentMaps bug = builder.element("bug");
        ElementComponentMaps ghost = builder.element("ghost");
        ElementComponentMaps steel = builder.element("steel");
        ElementComponentMaps fire = builder.element("fire");
        ElementComponentMaps water = builder.element("water");
        ElementComponentMaps grass = builder.element("grass");
        ElementComponentMaps electric = builder.element("electric");
        ElementComponentMaps psychic = builder.element("psychic");
        ElementComponentMaps ice = builder.element("ice");
        ElementComponentMaps dragon = builder.element("dragon");
        ElementComponentMaps dark = builder.element("dark");
        ElementComponentMaps fairy = builder.element("fairy");

        SimpleComponentMap walkAbility = builder.simple("walkAbility", x -> empty);
        SimpleComponentMap passAbility = builder.simple("passAbility", x -> empty);

        ActionGenerator<Event> actions = new ActionAggregator(
                new WalkGenerator(walkAbility, position, (movePoints.active), p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !position.exists(e -> position.hasValue(e, p))),
                new PassTurnGenerator(passAbility));
        EventDispatcher dispatcher = new EventDispatcher();
        EventQueue events = new EventQueueImpl(dispatcher::fire, loggerFactory.apply(EventQueueImpl.class));

        dispatcher.setListeners(SetActiveHealthEvent.class, new SetActiveHealthHandler(loggerFactory.apply(SetActiveHealthHandler.class), events, (health.active)));
        dispatcher.setListeners(UpdateBuffedHealthEvent.class, new UpdateBuffedHealthHandler(loggerFactory.apply(UpdateBuffedHealthHandler.class), events, (health.base), (health.additive), (health.buffed), buffOn));
        dispatcher.setListeners(ResetActiveHealthEvent.class, new ResetActiveHealthHandler(loggerFactory.apply(ResetActiveHealthHandler.class), events, (health.buffed)));

        dispatcher.setListeners(SetActiveActionPointsEvent.class, new SetActiveActionPointsHandler(loggerFactory.apply(SetActiveActionPointsHandler.class), events, (actionPoints.active)));
        dispatcher.setListeners(UpdateBuffedActionPointsEvent.class, new UpdateBuffedActionPointsHandler(loggerFactory.apply(UpdateBuffedActionPointsHandler.class), events, (actionPoints.base), (actionPoints.additive), (actionPoints.buffed), buffOn));
        dispatcher.setListeners(ResetActiveActionPointsEvent.class, new ResetActiveActionPointsHandler(loggerFactory.apply(ResetActiveActionPointsHandler.class), events, (actionPoints.buffed)));

        dispatcher.setListeners(SetActiveMovePointsEvent.class, new SetActiveMovePointsHandler(loggerFactory.apply(SetActiveMovePointsHandler.class), events, (movePoints.active)));
        dispatcher.setListeners(UpdateBuffedMovePointsEvent.class, new UpdateBuffedMovePointsHandler(loggerFactory.apply(UpdateBuffedMovePointsHandler.class), events, (movePoints.base), (movePoints.additive), (movePoints.buffed), buffOn));
        dispatcher.setListeners(ResetActiveMovePointsEvent.class, new ResetActiveMovePointsHandler(loggerFactory.apply(ResetActiveMovePointsHandler.class), events, (movePoints.buffed)));

        dispatcher.setListeners(WalkAction.class, new WalkHandler(loggerFactory.apply(WalkHandler.class), events, activeTurn, (movePoints.active)));
        dispatcher.setListeners(PassTurnAction.class, new PassTurnHandler(loggerFactory.apply(PassTurnHandler.class), events, activeTurn, memberOf));
        dispatcher.setListeners(SetPositionEvent.class, new SetPositionHandler(loggerFactory.apply(SetPositionHandler.class), events, position));

        dispatcher.setListeners(GameStartEvent.class,
                new RefreshAllHealthHandler<>(loggerFactory.apply(RefreshAllHealthHandler.class), events, (health.active), (health.base), (health.buffed), (health.additive), buffOn),
                new RefreshAllActionPointsHandler<>(loggerFactory.apply(RefreshAllActionPointsHandler.class), events, (actionPoints.active), (actionPoints.base), (actionPoints.buffed), (actionPoints.additive), buffOn),
                new RefreshAllMovePointsHandler<>(loggerFactory.apply(RefreshAllMovePointsHandler.class), events, (movePoints.active), (movePoints.base), (movePoints.buffed), (movePoints.additive), buffOn),
                new StartTurnOfRandomTeamHandler<>(loggerFactory.apply(StartTurnOfRandomTeamHandler.class), events, random::nextInt, nextTeam));
        dispatcher.setListeners(TurnEndEvent.class,
                new RefreshAllActionPointsHandler<>(loggerFactory.apply(RefreshAllActionPointsHandler.class), events, (actionPoints.active), (actionPoints.base), (actionPoints.buffed), (actionPoints.additive), buffOn),
                new RefreshAllMovePointsHandler<>(loggerFactory.apply(RefreshAllMovePointsHandler.class), events, (movePoints.active), (movePoints.base), (movePoints.buffed), (movePoints.additive), buffOn),
                new TurnEndHandler(loggerFactory.apply(TurnEndHandler.class), events, nextTeam));

        dispatcher.setListeners(TurnStartEvent.class, new TurnStartHandler(loggerFactory.apply(TurnStartHandler.class), events, activeTurn, memberOf));

        int gameState = entities.create();
        mapSize.set(gameState, Coordinates.of(mapWidth, mapHeight));

        int teamA = entities.create();
        int teamB = entities.create();
        int teamC = entities.create();
        nextTeam.set(teamA, teamB);
        nextTeam.set(teamB, teamC);
        nextTeam.set(teamC, teamA);

        int human1 = 0;
        int human2 = 1;
        int bot3 = 2;

        int character1 = entities.create();
        controlledBy.set(character1, human1);
        memberOf.set(character1, teamA);
        position.set(character1, Coordinates.of(1, 1));
        spriteId.set(character1, 143);
        (health.base).set(character1, 17);
        (actionPoints.base).set(character1, 1);
        (movePoints.base).set(character1, 2);
        walkAbility.set(character1, 0);
        passAbility.set(character1, 0);
        ice.power.base.set(character1, 1405);
        rock.toughness.base.set(character1, -379);

        int character2 = entities.create();
        controlledBy.set(character2, human2);
        memberOf.set(character2, teamB);
        position.set(character2, Coordinates.of(5, 9));
        spriteId.set(character2, 247);
        (health.base).set(character2, 27);
        (actionPoints.base).set(character2, 2);
        (movePoints.base).set(character2, 3);
        walkAbility.set(character2, 0);
        passAbility.set(character2, 0);

        int character3 = entities.create();
        controlledBy.set(character3, bot3);
        memberOf.set(character3, teamC);
        position.set(character3, Coordinates.of(3, 5));
        spriteId.set(character3, 148);
        (health.base).set(character3, 37);
        (actionPoints.base).set(character3, 3);
        (movePoints.base).set(character3, 4);
        walkAbility.set(character3, 0);
        passAbility.set(character3, 0);

        int character4 = entities.create();
        controlledBy.set(character4, bot3);
        memberOf.set(character4, teamA);
        position.set(character4, Coordinates.of(8, 2));
        spriteId.set(character4, 132);
        (health.base).set(character4, 47);
        (actionPoints.base).set(character4, 4);
        (movePoints.base).set(character4, 5);
        walkAbility.set(character4, 0);
        passAbility.set(character4, 0);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = entities.create();
                position.set(obstacle, Coordinates.of(x, y));
                mapObstacle.set(obstacle, 0);
            }
        }

        Map<Integer, Character> actorShortcuts = new MapBuilder<Integer, Character>()
                .with(character1, '1')
                .with(character2, '2')
                .with(character3, '3')
                .with(character4, '4')
                .build();
        Logger log = loggerFactory.apply(Main.class);
        events.action(new GameStartEvent());
        while (true) {
            logState(log, builder, entities, actorShortcuts, mapWidth, mapHeight, position);
            events.action(chooseAction(activeTurn, actions));
        }

    }

    private static void logState(Logger log, DebugEntityBuilder builder, EntityPool entities, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight, SimpleComponentMap position) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("{}", GSON.toJson(builder.toDebugObjects(entities.getEntities())));
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
                new MapPrinter().printMap(actorShortcuts, mapWidth, mapHeight, position, ps);
                log.debug("{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static Event chooseAction(SimpleComponentMap activeTurn, ActionGenerator<Event> actions) {
        for (int actor : activeTurn.entities()) {
            System.out.println("availableActions of " + actor + ": " + actions.availableActions(actor));
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("actor");
            int actor = activeTurn.entities().get(scanner.nextInt());
            System.out.println("action");
            int action = scanner.nextInt();
            return actions.availableActions(actor).get(action);
        }
    }
}
