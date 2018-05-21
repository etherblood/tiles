package com.etherblood.sandbox;

import com.etherblood.rules.RandomTracker;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.ComponentMap;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.EntityPool;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionAggregator;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnHandler;
import com.etherblood.rules.abilities.razorLeaf.RazorleafGenerator;
import com.etherblood.rules.abilities.razorLeaf.RazorleafHandler;
import com.etherblood.rules.abilities.walk.WalkGenerator;
import com.etherblood.rules.abilities.walk.WalkHandler;
import com.etherblood.rules.battle.DamageHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.components.GameEventDispatcher;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.movement.Coordinates;
import com.etherblood.rules.stats.RefreshAllStatHandler;
import com.etherblood.rules.stats.ResetActiveStatHandler;
import com.etherblood.rules.stats.SetComponentHandler;
import com.etherblood.rules.stats.UpdateBuffedStatHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "info");
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
        Map<String, EventDefinition> eventMap = new EventDefGenerator().generate();
        EventDefinition[] eventDefinitions = new EventDefinition[eventMap.size()];
        for (EventDefinition value : eventMap.values()) {
            eventDefinitions[value.index()] = value;
        }
        for (EventDefinition eventDefinition : eventDefinitions) {
            Objects.requireNonNull(eventDefinition);
        }
        ActionGenerator actions = new ActionAggregator(
                new RazorleafGenerator(data, eventMap.get("RazorleafAction").id()),
                new WalkGenerator(data, p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !data.component(Components.POSITION).exists(e -> data.component(Components.POSITION).hasValue(e, p)), eventMap.get("WalkAction").id()),
                new PassTurnGenerator(data, eventMap.get("PassTurnAction").id()));

        EventQueueImpl events = new EventQueueImpl(eventDefinitions);
        GameEventDispatcher dispatcher = new GameEventDispatcher(data, events, random::nextInt);

        dispatcher.setHandlers(eventMap.get("SetActiveHealth").id(), new SetComponentHandler("activeHealth", Components.Stats.Health.ACTIVE));
        dispatcher.setHandlers(eventMap.get("SetBuffedHealth").id(), new SetComponentHandler("buffedHealth", Components.Stats.Health.BUFFED));
        dispatcher.setHandlers(eventMap.get("UpdateBuffedHealth").id(), new UpdateBuffedStatHandler("health", Components.Stats.Health.BASE, Components.Stats.Health.ADDITIVE, eventMap.get("SetBuffedHealth").id()));
        dispatcher.setHandlers(eventMap.get("ResetActiveHealth").id(), new ResetActiveStatHandler("health", Components.Stats.Health.BUFFED, eventMap.get("SetActiveHealth").id()));

        dispatcher.setHandlers(eventMap.get("SetActiveActionPoints").id(), new SetComponentHandler("activeActionPoints", Components.Stats.ActionPoints.ACTIVE));
        dispatcher.setHandlers(eventMap.get("SetBuffedActionPoints").id(), new SetComponentHandler("buffedActionPoints", Components.Stats.ActionPoints.BUFFED));
        dispatcher.setHandlers(eventMap.get("UpdateBuffedActionPoints").id(), new UpdateBuffedStatHandler("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ADDITIVE, eventMap.get("SetBuffedActionPoints").id()));
        dispatcher.setHandlers(eventMap.get("ResetActiveActionPoints").id(), new ResetActiveStatHandler("actionPoints", Components.Stats.ActionPoints.BUFFED, eventMap.get("SetActiveActionPoints").id()));

        dispatcher.setHandlers(eventMap.get("SetActiveMovePoints").id(), new SetComponentHandler("activeMovePoints", Components.Stats.MovePoints.ACTIVE));
        dispatcher.setHandlers(eventMap.get("SetBuffedMovePoints").id(), new SetComponentHandler("buffedMovePoints", Components.Stats.MovePoints.BUFFED));
        dispatcher.setHandlers(eventMap.get("UpdateBuffedMovePoints").id(), new UpdateBuffedStatHandler("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ADDITIVE, eventMap.get("SetBuffedMovePoints").id()));
        dispatcher.setHandlers(eventMap.get("ResetActiveMovePoints").id(), new ResetActiveStatHandler("movePoints", Components.Stats.MovePoints.BUFFED, eventMap.get("SetActiveMovePoints").id()));

        dispatcher.setHandlers(eventMap.get("SetPosition").id(), new SetComponentHandler("position", Components.POSITION));

        dispatcher.setHandlers(eventMap.get("EarthDamage").id(),
                new DamageHandler());
        dispatcher.setHandlers(eventMap.get("FireDamage").id(),
                new DamageHandler());
        dispatcher.setHandlers(eventMap.get("AirDamage").id(),
                new DamageHandler());
        dispatcher.setHandlers(eventMap.get("WaterDamage").id(),
                new DamageHandler());

        dispatcher.setHandlers(eventMap.get("WalkAction").id(), new WalkHandler(eventMap.get("SetPosition")));
        dispatcher.setHandlers(eventMap.get("PassTurnAction").id(), new PassTurnHandler(eventMap.get("EndTurn")));
        dispatcher.setHandlers(eventMap.get("RazorleafAction").id(), new RazorleafHandler(eventMap.get("EarthDamage").id()));

        dispatcher.setHandlers(eventMap.get("GameStart").id(),
                new RefreshAllStatHandler("health", Components.Stats.Health.BASE, Components.Stats.Health.ACTIVE, Components.Stats.Health.BUFFED, Components.Stats.Health.ADDITIVE, eventMap.get("UpdateBuffedHealth").id(), eventMap.get("ResetActiveHealth").id()),
                new RefreshAllStatHandler("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ACTIVE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, eventMap.get("UpdateBuffedMovePoints").id(), eventMap.get("ResetActiveMovePoints").id()),
                new RefreshAllStatHandler("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ACTIVE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, eventMap.get("UpdateBuffedActionPoints").id(), eventMap.get("ResetActiveActionPoints").id()),
                new RefreshAllStatHandler("airPower", Components.Stats.Power.Air.BASE, Components.Stats.Power.Air.ACTIVE, Components.Stats.Power.Air.BUFFED, Components.Stats.Power.Air.ADDITIVE, eventMap.get("UpdateBuffedAirPower").id(), eventMap.get("ResetActiveAirPower").id()),
                new RefreshAllStatHandler("earthPower", Components.Stats.Power.Earth.BASE, Components.Stats.Power.Earth.ACTIVE, Components.Stats.Power.Earth.BUFFED, Components.Stats.Power.Earth.ADDITIVE, eventMap.get("UpdateBuffedEarthPower").id(), eventMap.get("ResetActiveEarthPower").id()),
                new RefreshAllStatHandler("firePower", Components.Stats.Power.Fire.BASE, Components.Stats.Power.Fire.ACTIVE, Components.Stats.Power.Fire.BUFFED, Components.Stats.Power.Fire.ADDITIVE, eventMap.get("UpdateBuffedFirePower").id(), eventMap.get("ResetActiveFirePower").id()),
                new RefreshAllStatHandler("waterPower", Components.Stats.Power.Water.BASE, Components.Stats.Power.Water.ACTIVE, Components.Stats.Power.Water.BUFFED, Components.Stats.Power.Water.ADDITIVE, eventMap.get("UpdateBuffedWaterPower").id(), eventMap.get("ResetActiveWaterPower").id()),
                new RefreshAllStatHandler("airToughness", Components.Stats.Toughness.Air.BASE, Components.Stats.Toughness.Air.ACTIVE, Components.Stats.Toughness.Air.BUFFED, Components.Stats.Toughness.Air.ADDITIVE, eventMap.get("UpdateBuffedAirToughness").id(), eventMap.get("ResetActiveAirToughness").id()),
                new RefreshAllStatHandler("earthToughness", Components.Stats.Toughness.Earth.BASE, Components.Stats.Toughness.Earth.ACTIVE, Components.Stats.Toughness.Earth.BUFFED, Components.Stats.Toughness.Earth.ADDITIVE, eventMap.get("UpdateBuffedEarthToughness").id(), eventMap.get("ResetActiveEarthToughness").id()),
                new RefreshAllStatHandler("fireToughness", Components.Stats.Toughness.Fire.BASE, Components.Stats.Toughness.Fire.ACTIVE, Components.Stats.Toughness.Fire.BUFFED, Components.Stats.Toughness.Fire.ADDITIVE, eventMap.get("UpdateBuffedFireToughness").id(), eventMap.get("ResetActiveFireToughness").id()),
                new RefreshAllStatHandler("waterToughness", Components.Stats.Toughness.Water.BASE, Components.Stats.Toughness.Water.ACTIVE, Components.Stats.Toughness.Water.BUFFED, Components.Stats.Toughness.Water.ADDITIVE, eventMap.get("UpdateBuffedWaterToughness").id(), eventMap.get("ResetActiveWaterToughness").id()),
                new StartTurnOfRandomTeamHandler(eventMap.get("TurnStart")));
        dispatcher.setHandlers(eventMap.get("TurnEnd").id(),
                new RefreshAllStatHandler("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ACTIVE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, eventMap.get("UpdateBuffedMovePoints").id(), eventMap.get("ResetActiveMovePoints").id()),
                new RefreshAllStatHandler("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ACTIVE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, eventMap.get("UpdateBuffedActionPoints").id(), eventMap.get("ResetActiveActionPoints").id()),
                new TurnEndHandler(eventMap.get("TurnStart")));

        dispatcher.setHandlers(eventMap.get("TurnStart").id(), new TurnStartHandler());

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
        events.action(eventMap.get("GameStart").id());
        while (true) {
            logState(log, debugMapper, data, actorShortcuts, mapWidth, mapHeight);
            Action action = chooseAction(data.component(Components.ACTIVE_TURN), actions);
            events.action(action.eventId, action.eventArgs);
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

    private static Action chooseAction(ComponentMap activeTurn, ActionGenerator actions) {
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
