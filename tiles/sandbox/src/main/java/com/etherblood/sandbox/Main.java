package com.etherblood.sandbox;

import com.etherblood.rules.RandomTracker;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.ComponentDefinition;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.EntityPool;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.Event;
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
import com.etherblood.rules.battle.DamageEvent;
import com.etherblood.rules.battle.DamageHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.components.GameEventDispatcher;
import com.etherblood.rules.game.GameStartEvent;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndEvent;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartEvent;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.movement.Coordinates;
import com.etherblood.rules.movement.SetPositionEvent;
import com.etherblood.rules.stats.RefreshAllStatHandler;
import com.etherblood.rules.stats.ResetActiveStatHandler;
import com.etherblood.rules.stats.SetComponentHandler;
import com.etherblood.rules.stats.UpdateBuffedStatHandler;
import com.etherblood.rules.stats.actionpoints.ResetActiveActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.SetActiveActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.SetBuffedActionPointsEvent;
import com.etherblood.rules.stats.actionpoints.UpdateBuffedActionPointsEvent;
import com.etherblood.rules.stats.health.ResetActiveHealthEvent;
import com.etherblood.rules.stats.health.SetActiveHealthEvent;
import com.etherblood.rules.stats.health.SetBuffedHealthEvent;
import com.etherblood.rules.stats.health.UpdateBuffedHealthEvent;
import com.etherblood.rules.stats.movepoints.ResetActiveMovePointsEvent;
import com.etherblood.rules.stats.movepoints.SetActiveMovePointsEvent;
import com.etherblood.rules.stats.movepoints.SetBuffedMovePointsEvent;
import com.etherblood.rules.stats.movepoints.UpdateBuffedMovePointsEvent;
import com.etherblood.rules.stats.power.air.ResetActiveAirPowerEvent;
import com.etherblood.rules.stats.power.air.UpdateBuffedAirPowerEvent;
import com.etherblood.rules.stats.power.earth.ResetActiveEarthPowerEvent;
import com.etherblood.rules.stats.power.earth.UpdateBuffedEarthPowerEvent;
import com.etherblood.rules.stats.power.fire.ResetActiveFirePowerEvent;
import com.etherblood.rules.stats.power.fire.UpdateBuffedFirePowerEvent;
import com.etherblood.rules.stats.power.water.ResetActiveWaterPowerEvent;
import com.etherblood.rules.stats.power.water.UpdateBuffedWaterPowerEvent;
import com.etherblood.rules.stats.toughness.air.ResetActiveAirToughnessEvent;
import com.etherblood.rules.stats.toughness.air.UpdateBuffedAirToughnessEvent;
import com.etherblood.rules.stats.toughness.earth.ResetActiveEarthToughnessEvent;
import com.etherblood.rules.stats.toughness.earth.UpdateBuffedEarthToughnessEvent;
import com.etherblood.rules.stats.toughness.fire.ResetActiveFireToughnessEvent;
import com.etherblood.rules.stats.toughness.fire.UpdateBuffedFireToughnessEvent;
import com.etherblood.rules.stats.toughness.water.ResetActiveWaterToughnessEvent;
import com.etherblood.rules.stats.toughness.water.UpdateBuffedWaterToughnessEvent;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
    public static void main(String[] args) throws IOException, IllegalArgumentException, IllegalAccessException {
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
        EntityData data = new EntityData(Components.DEFINITIONS);

        EntityDebugObjectMapper debugMapper = new EntityDebugObjectMapper();
//        debugMapper.registerWithReflection(components);
//        debugMapper.register(components.controlledBy, "controlledBy");
//        debugMapper.register(components.memberOf, "memberOf");
//        debugMapper.register(components.nextTeam, "nextTeam");
//        debugMapper.register(components.mapSize, "mapSize", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
//        debugMapper.register(components.mapObstacle, "mapObstacle", x -> empty);
//        debugMapper.register(components.position, "position", p -> new Point(Coordinates.x(p), Coordinates.y(p)));
//        debugMapper.register(components.buffOn, "buffOn");
//        debugMapper.register(components.activeTurn, "activeTurn", x -> empty);
//        
//        debugMapper.register(components.spriteId, "spriteId");
//        debugMapper.register(components.stats.health, "health");
//        debugMapper.register(components.stats.actionPoints, "actionPoints");
//        debugMapper.register(components.stats.movePoints, "movePoints");
//        debugMapper.register(components.stats.elements.air, "air");
//        debugMapper.register(components.stats.elements.fire, "fire");
//        debugMapper.register(components.stats.elements.water, "water");
//        debugMapper.register(components.stats.elements.earth, "earth");
//
//        debugMapper.register(components.abilities.passTurn, "passTurnAbility");
//        debugMapper.register(components.abilities.walk, "walkAbility");
//        debugMapper.register(components.abilities.razorLeaf, "razorLeafAbility");
        ActionGenerator<Event> actions = new ActionAggregator(
                new RazorLeafGenerator(data),
                new WalkGenerator(data, p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !data.component(Components.POSITION).exists(e -> data.component(Components.POSITION).hasValue(e, p))),
                new PassTurnGenerator(data));

        GameEventDispatcher dispatcher = new GameEventDispatcher(data, random::nextInt);
        EventQueue events = new EventQueueImpl(dispatcher::accept, loggerFactory.apply(EventQueueImpl.class));
        dispatcher.setQueue(events);

        dispatcher.addHandlers(SetActiveHealthEvent.class, new SetComponentHandler<>("activeHealth", Components.Stats.Health.ACTIVE));
        dispatcher.addHandlers(SetBuffedHealthEvent.class, new SetComponentHandler<>("buffedHealth", Components.Stats.Health.BUFFED));
        dispatcher.addHandlers(UpdateBuffedHealthEvent.class, new UpdateBuffedStatHandler<>("health", Components.Stats.Health.BASE, Components.Stats.Health.ADDITIVE, SetBuffedHealthEvent::new));
        dispatcher.addHandlers(ResetActiveHealthEvent.class, new ResetActiveStatHandler<>("health", Components.Stats.Health.BUFFED, SetActiveHealthEvent::new));

        dispatcher.addHandlers(SetActiveActionPointsEvent.class, new SetComponentHandler<>("activeActionPoints", Components.Stats.ActionPoints.ACTIVE));
        dispatcher.addHandlers(SetBuffedActionPointsEvent.class, new SetComponentHandler<>("buffedActionPoints", Components.Stats.ActionPoints.BUFFED));
        dispatcher.addHandlers(UpdateBuffedActionPointsEvent.class, new UpdateBuffedStatHandler<>("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ADDITIVE, SetBuffedActionPointsEvent::new));
        dispatcher.addHandlers(ResetActiveActionPointsEvent.class, new ResetActiveStatHandler<>("actionPoints", Components.Stats.ActionPoints.BUFFED, SetActiveActionPointsEvent::new));

        dispatcher.addHandlers(SetActiveMovePointsEvent.class, new SetComponentHandler<>("activeMovePoints", Components.Stats.MovePoints.ACTIVE));
        dispatcher.addHandlers(SetBuffedMovePointsEvent.class, new SetComponentHandler<>("buffedMovePoints", Components.Stats.MovePoints.BUFFED));
        dispatcher.addHandlers(UpdateBuffedMovePointsEvent.class, new UpdateBuffedStatHandler<>("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ADDITIVE, SetBuffedMovePointsEvent::new));
        dispatcher.addHandlers(ResetActiveMovePointsEvent.class, new ResetActiveStatHandler<>("movePoints", Components.Stats.MovePoints.BUFFED, SetActiveMovePointsEvent::new));

        dispatcher.addHandlers(SetPositionEvent.class, new SetComponentHandler<>("position", Components.POSITION));

        dispatcher.addHandlers(DamageEvent.class,
                //                new ApplyDamageTypeHandler(types),
                new DamageHandler());

        dispatcher.addHandlers(WalkAction.class, new WalkHandler());
        dispatcher.addHandlers(PassTurnAction.class, new PassTurnHandler());
        dispatcher.addHandlers(RazorLeafAction.class, new RazorLeafHandler());

        dispatcher.addHandlers(GameStartEvent.class,
                new RefreshAllStatHandler<>("health", Components.Stats.Health.BASE, Components.Stats.Health.ACTIVE, Components.Stats.Health.BUFFED, Components.Stats.Health.ADDITIVE, UpdateBuffedHealthEvent::new, ResetActiveHealthEvent::new),
                new RefreshAllStatHandler<>("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ACTIVE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, UpdateBuffedMovePointsEvent::new, ResetActiveMovePointsEvent::new),
                new RefreshAllStatHandler<>("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ACTIVE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, UpdateBuffedActionPointsEvent::new, ResetActiveActionPointsEvent::new),
                new RefreshAllStatHandler<>("airPower", Components.Stats.Power.Air.BASE, Components.Stats.Power.Air.ACTIVE, Components.Stats.Power.Air.BUFFED, Components.Stats.Power.Air.ADDITIVE, UpdateBuffedAirPowerEvent::new, ResetActiveAirPowerEvent::new),
                new RefreshAllStatHandler<>("earthPower", Components.Stats.Power.Earth.BASE, Components.Stats.Power.Earth.ACTIVE, Components.Stats.Power.Earth.BUFFED, Components.Stats.Power.Earth.ADDITIVE, UpdateBuffedEarthPowerEvent::new, ResetActiveEarthPowerEvent::new),
                new RefreshAllStatHandler<>("firePower", Components.Stats.Power.Fire.BASE, Components.Stats.Power.Fire.ACTIVE, Components.Stats.Power.Fire.BUFFED, Components.Stats.Power.Fire.ADDITIVE, UpdateBuffedFirePowerEvent::new, ResetActiveFirePowerEvent::new),
                new RefreshAllStatHandler<>("waterPower", Components.Stats.Power.Water.BASE, Components.Stats.Power.Water.ACTIVE, Components.Stats.Power.Water.BUFFED, Components.Stats.Power.Water.ADDITIVE, UpdateBuffedWaterPowerEvent::new, ResetActiveWaterPowerEvent::new),
                new RefreshAllStatHandler<>("airToughness", Components.Stats.Toughness.Air.BASE, Components.Stats.Toughness.Air.ACTIVE, Components.Stats.Toughness.Air.BUFFED, Components.Stats.Toughness.Air.ADDITIVE, UpdateBuffedAirToughnessEvent::new, ResetActiveAirToughnessEvent::new),
                new RefreshAllStatHandler<>("earthToughness", Components.Stats.Toughness.Earth.BASE, Components.Stats.Toughness.Earth.ACTIVE, Components.Stats.Toughness.Earth.BUFFED, Components.Stats.Toughness.Earth.ADDITIVE, UpdateBuffedEarthToughnessEvent::new, ResetActiveEarthToughnessEvent::new),
                new RefreshAllStatHandler<>("fireToughness", Components.Stats.Toughness.Fire.BASE, Components.Stats.Toughness.Fire.ACTIVE, Components.Stats.Toughness.Fire.BUFFED, Components.Stats.Toughness.Fire.ADDITIVE, UpdateBuffedFireToughnessEvent::new, ResetActiveFireToughnessEvent::new),
                new RefreshAllStatHandler<>("waterToughness", Components.Stats.Toughness.Water.BASE, Components.Stats.Toughness.Water.ACTIVE, Components.Stats.Toughness.Water.BUFFED, Components.Stats.Toughness.Water.ADDITIVE, UpdateBuffedWaterToughnessEvent::new, ResetActiveWaterToughnessEvent::new),
                new StartTurnOfRandomTeamHandler());
        dispatcher.addHandlers(TurnEndEvent.class,
                new RefreshAllStatHandler<>("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ACTIVE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, UpdateBuffedMovePointsEvent::new, ResetActiveMovePointsEvent::new),
                new RefreshAllStatHandler<>("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ACTIVE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, UpdateBuffedActionPointsEvent::new, ResetActiveActionPointsEvent::new),
                new TurnEndHandler());

        dispatcher.addHandlers(TurnStartEvent.class, new TurnStartHandler());

//        dispatcher.addHandlers(RefreshBuffedPowerEvent.class, new RefreshBuffedPowerHandler(loggerFactory.apply(RefreshBuffedPowerHandler.class), events, types, buffOn));
//        dispatcher.addHandlers(SetAdditivePowerEvent.class, new SetAdditivePowerHandler(loggerFactory.apply(SetAdditivePowerHandler.class), events, types, buffOn));
//        dispatcher.addHandlers(SetActivePowerEvent.class, new SetActivePowerHandler(loggerFactory.apply(SetActivePowerHandler.class), events, types));
//
//        dispatcher.addHandlers(RefreshBuffedToughnessEvent.class, new RefreshBuffedToughnessHandler(loggerFactory.apply(RefreshBuffedToughnessHandler.class), events, types, buffOn));
//        dispatcher.addHandlers(SetAdditiveToughnessEvent.class, new SetAdditiveToughnessHandler(loggerFactory.apply(SetAdditiveToughnessHandler.class), events, types, buffOn));
//        dispatcher.addHandlers(SetActiveToughnessEvent.class, new SetActiveToughnessHandler(loggerFactory.apply(SetActiveToughnessHandler.class), events, types));
        Pokemons pokemons = new Pokemons(data);

        int gameState = entities.create();
        data.component(Components.Arena.SIZE).set(gameState, Coordinates.of(mapWidth, mapHeight));

        int teamA = entities.create();
        int teamB = entities.create();
        data.component(Components.NEXT_TEAM).set(teamA, teamB);
        data.component(Components.NEXT_TEAM).set(teamB, teamA);

        int human1 = 0;
        int human2 = 1;

        int bulbasaur = entities.create();
        pokemons.bulbasaur(bulbasaur);
        data.component(Components.CONTROLLED_BY).set(bulbasaur, human1);
        data.component(Components.MEMBER_OF).set(bulbasaur, teamA);
        data.component(Components.POSITION).set(bulbasaur, Coordinates.of(1, 1));
        data.component(Components.Abilities.WALK).set(bulbasaur, 0);
        data.component(Components.Abilities.PASS_TURN).set(bulbasaur, 0);
        data.component(Components.Abilities.RAZORLEAF).set(bulbasaur, 4);

        int charmander = entities.create();
        pokemons.charmander(charmander);
        data.component(Components.CONTROLLED_BY).set(charmander, human2);
        data.component(Components.MEMBER_OF).set(charmander, teamB);
        data.component(Components.POSITION).set(charmander, Coordinates.of(5, 9));
        data.component(Components.Abilities.WALK).set(charmander, 0);
        data.component(Components.Abilities.PASS_TURN).set(charmander, 0);

        int squirtle = entities.create();
        pokemons.squirtle(squirtle);
        data.component(Components.CONTROLLED_BY).set(squirtle, human1);
        data.component(Components.MEMBER_OF).set(squirtle, teamB);
        data.component(Components.POSITION).set(squirtle, Coordinates.of(3, 5));
        data.component(Components.Abilities.WALK).set(squirtle, 0);
        data.component(Components.Abilities.PASS_TURN).set(squirtle, 0);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = entities.create();
                data.component(Components.POSITION).set(obstacle, Coordinates.of(x, y));
                data.component(Components.Arena.OBSTACLE).set(obstacle, 0);
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
            logState(log, debugMapper, data, actorShortcuts, mapWidth, mapHeight);
//            events.action(chooseAction(activeTurn, actions));
        }

    }

    private static void logState(Logger log, EntityDebugObjectMapper builder, EntityData data, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("{}", GSON.toJson(builder.toDebugObjects(data)));
//            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
//                new MapPrinter().printMap(actorShortcuts, mapWidth, mapHeight, Components.POSITION, ps);
//                log.debug("{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
//            }
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
