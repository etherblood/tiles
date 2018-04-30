package com.etherblood.sandbox;

import com.etherblood.rules.RandomTracker;
import com.etherblood.rules.context.StatComponentMaps;
import com.etherblood.rules.context.DebugEntityBuilder;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.EntityPool;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.entities.UniqueComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventDispatcher;
import com.etherblood.events.EventQueue;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.rules.abilities.ActionAggregator;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnAction;
import com.etherblood.rules.abilities.endTurn.PassTurnGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnHandler;
import com.etherblood.rules.abilities.razorLeaf.RazorLeafAction;
import com.etherblood.rules.abilities.razorLeaf.RazorLeafGenerator;
import com.etherblood.rules.abilities.razorLeaf.RazorLeafHandler;
import com.etherblood.rules.abilities.walk.WalkAction;
import com.etherblood.rules.abilities.walk.WalkGenerator;
import com.etherblood.rules.abilities.walk.WalkHandler;
import com.etherblood.rules.battle.ApplyDamageTypeHandler;
import com.etherblood.rules.battle.DamageEvent;
import com.etherblood.rules.battle.DamageHandler;
import com.etherblood.rules.context.TypeComponentMaps;
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
import com.etherblood.rules.stats.types.RefreshAllTypeStatsHandler;
import com.etherblood.rules.stats.types.power.RefreshBuffedPowerEvent;
import com.etherblood.rules.stats.types.power.RefreshBuffedPowerHandler;
import com.etherblood.rules.stats.types.power.SetActivePowerEvent;
import com.etherblood.rules.stats.types.power.SetActivePowerHandler;
import com.etherblood.rules.stats.types.power.SetAdditivePowerEvent;
import com.etherblood.rules.stats.types.power.SetAdditivePowerHandler;
import com.etherblood.rules.stats.types.toughness.RefreshBuffedToughnessEvent;
import com.etherblood.rules.stats.types.toughness.RefreshBuffedToughnessHandler;
import com.etherblood.rules.stats.types.toughness.SetActiveToughnessEvent;
import com.etherblood.rules.stats.types.toughness.SetActiveToughnessHandler;
import com.etherblood.rules.stats.types.toughness.SetAdditiveToughnessEvent;
import com.etherblood.rules.stats.types.toughness.SetAdditiveToughnessHandler;
import com.etherblood.templates.Pokemons;
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
public class Main {

    private final static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
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
        UniqueComponentMap mapSize = builder.unique("mapSize", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
        SimpleComponentMap mapObstacle = builder.simple("mapObstacle", x -> empty);
        SimpleComponentMap position = builder.simple("position", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
        SimpleComponentMap buffOn = builder.simple("buffOn");
        SimpleComponentMap activeTurn = builder.simple("activeTurn", x -> empty);

        SimpleComponentMap spriteId = builder.simple("spriteId");
        StatComponentMaps health = builder.stats("health");
        StatComponentMaps actionPoints = builder.stats("actionPoints");
        StatComponentMaps movePoints = builder.stats("movePoints");

        TypeComponentMaps types = new TypeComponentMaps(builder);

        SimpleComponentMap walkAbility = builder.simple("walkAbility", x -> empty);
        SimpleComponentMap passAbility = builder.simple("passAbility", x -> empty);
        SimpleComponentMap razorLeafAbility = builder.simple("razorLeafAbility");

        ActionGenerator<Event> actions = new ActionAggregator(
                new RazorLeafGenerator(razorLeafAbility, position, (actionPoints.active)),
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
        
        dispatcher.setListeners(SetPositionEvent.class, new SetPositionHandler(loggerFactory.apply(SetPositionHandler.class), events, position));
        
        dispatcher.setListeners(DamageEvent.class,
                new ApplyDamageTypeHandler(loggerFactory.apply(ApplyDamageTypeHandler.class), events, types),
                new DamageHandler(loggerFactory.apply(DamageHandler.class), events, activeTurn));

        dispatcher.setListeners(WalkAction.class, new WalkHandler(loggerFactory.apply(WalkHandler.class), events, activeTurn, (movePoints.active)));
        dispatcher.setListeners(PassTurnAction.class, new PassTurnHandler(loggerFactory.apply(PassTurnHandler.class), events, activeTurn, memberOf));
        dispatcher.setListeners(RazorLeafAction.class, new RazorLeafHandler(loggerFactory.apply(RazorLeafHandler.class), events, razorLeafAbility, (actionPoints.active)));

        dispatcher.setListeners(GameStartEvent.class,
                new RefreshAllTypeStatsHandler<>(loggerFactory.apply(RefreshAllTypeStatsHandler.class), events, types, buffOn),
                new RefreshAllHealthHandler<>(loggerFactory.apply(RefreshAllHealthHandler.class), events, (health.active), (health.base), (health.buffed), (health.additive), buffOn),
                new RefreshAllActionPointsHandler<>(loggerFactory.apply(RefreshAllActionPointsHandler.class), events, (actionPoints.active), (actionPoints.base), (actionPoints.buffed), (actionPoints.additive), buffOn),
                new RefreshAllMovePointsHandler<>(loggerFactory.apply(RefreshAllMovePointsHandler.class), events, (movePoints.active), (movePoints.base), (movePoints.buffed), (movePoints.additive), buffOn),
                new StartTurnOfRandomTeamHandler<>(loggerFactory.apply(StartTurnOfRandomTeamHandler.class), events, random::nextInt, nextTeam));
        dispatcher.setListeners(TurnEndEvent.class,
                new RefreshAllActionPointsHandler<>(loggerFactory.apply(RefreshAllActionPointsHandler.class), events, (actionPoints.active), (actionPoints.base), (actionPoints.buffed), (actionPoints.additive), buffOn),
                new RefreshAllMovePointsHandler<>(loggerFactory.apply(RefreshAllMovePointsHandler.class), events, (movePoints.active), (movePoints.base), (movePoints.buffed), (movePoints.additive), buffOn),
                new TurnEndHandler(loggerFactory.apply(TurnEndHandler.class), events, nextTeam));

        dispatcher.setListeners(TurnStartEvent.class, new TurnStartHandler(loggerFactory.apply(TurnStartHandler.class), events, activeTurn, memberOf));

        dispatcher.setListeners(RefreshBuffedPowerEvent.class, new RefreshBuffedPowerHandler(loggerFactory.apply(RefreshBuffedPowerHandler.class), events, types, buffOn));
        dispatcher.setListeners(SetAdditivePowerEvent.class, new SetAdditivePowerHandler(loggerFactory.apply(SetAdditivePowerHandler.class), events, types, buffOn));
        dispatcher.setListeners(SetActivePowerEvent.class, new SetActivePowerHandler(loggerFactory.apply(SetActivePowerHandler.class), events, types));

        dispatcher.setListeners(RefreshBuffedToughnessEvent.class, new RefreshBuffedToughnessHandler(loggerFactory.apply(RefreshBuffedToughnessHandler.class), events, types, buffOn));
        dispatcher.setListeners(SetAdditiveToughnessEvent.class, new SetAdditiveToughnessHandler(loggerFactory.apply(SetAdditiveToughnessHandler.class), events, types, buffOn));
        dispatcher.setListeners(SetActiveToughnessEvent.class, new SetActiveToughnessHandler(loggerFactory.apply(SetActiveToughnessHandler.class), events, types));

        Pokemons pokemons = new Pokemons(spriteId, health, actionPoints, movePoints, types);

        int gameState = entities.create();
        mapSize.set(gameState, Coordinates.of(mapWidth, mapHeight));

        int teamA = entities.create();
        int teamB = entities.create();
        nextTeam.set(teamA, teamB);
        nextTeam.set(teamB, teamA);

        int human1 = 0;
        int human2 = 1;

        int bulbasaur = entities.create();
        pokemons.bulbasaur(bulbasaur);
        controlledBy.set(bulbasaur, human1);
        memberOf.set(bulbasaur, teamA);
        position.set(bulbasaur, Coordinates.of(1, 1));
        walkAbility.set(bulbasaur, 0);
        passAbility.set(bulbasaur, 0);
        razorLeafAbility.set(bulbasaur, 4);

        int charmander = entities.create();
        pokemons.charmander(charmander);
        controlledBy.set(charmander, human2);
        memberOf.set(charmander, teamB);
        position.set(charmander, Coordinates.of(5, 9));
        walkAbility.set(charmander, 0);
        passAbility.set(charmander, 0);

        int squirtle = entities.create();
        pokemons.squirtle(squirtle);
        controlledBy.set(squirtle, human1);
        memberOf.set(squirtle, teamB);
        position.set(squirtle, Coordinates.of(3, 5));
        walkAbility.set(squirtle, 0);
        passAbility.set(squirtle, 0);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = entities.create();
                position.set(obstacle, Coordinates.of(x, y));
                mapObstacle.set(obstacle, 0);
            }
        }

        Map<Integer, Character> actorShortcuts = new MapBuilder<Integer, Character>()
                .with(bulbasaur, '1')
                .with(charmander, '2')
                .with(squirtle, '3')
                .build();
        Logger log = loggerFactory.apply(Main.class);
        events.action(new GameStartEvent());
        while (true) {
            logState(log, builder, entities, actorShortcuts, mapWidth, mapHeight, position, activeTurn, actions);
            events.action(chooseAction(activeTurn, actions));
        }

    }

    private static void logState(Logger log, DebugEntityBuilder builder, EntityPool entities, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight, SimpleComponentMap position, SimpleComponentMap activeTurn, ActionGenerator<Event> actions) throws IOException {
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
