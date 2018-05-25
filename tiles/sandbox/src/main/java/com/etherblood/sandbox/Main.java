package com.etherblood.sandbox;

import com.etherblood.rules.RandomTracker;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.SimpleEntityData;
import com.etherblood.entities.IdSequences;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.rules.abilities.Action;
import com.etherblood.rules.abilities.ActionAggregator;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnGenerator;
import com.etherblood.rules.abilities.endTurn.PassTurnHandler;
import com.etherblood.rules.abilities.razorleaf.RazorleafGenerator;
import com.etherblood.rules.abilities.razorleaf.RazorleafHandler;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Function;
import java.util.stream.Collectors;
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

        int mapWidth = 10;
        int mapHeight = 10;

        RandomTracker random = new RandomTracker(new Random(453));
        SimpleEntityData data = new SimpleEntityData(Components.DEFINITIONS, IdSequences.simple2());

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
                new WalkGenerator(data, p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !data.query(Components.POSITION).exists(e -> data.hasValue(e, Components.POSITION, p)), eventMap.get("WalkAction").id()),
                new PassTurnGenerator(data, eventMap.get("PassTurnAction").id()));

        EventQueueImpl events = new EventQueueImpl(eventDefinitions);
        GameEventDispatcher dispatcher = new GameEventDispatcher(data, events, random::nextInt);

        defaultStatHandlers("Health", dispatcher, eventMap, Components.Stats.Health.BASE, Components.Stats.Health.BUFFED, Components.Stats.Health.ADDITIVE, Components.Stats.Health.ACTIVE);
        defaultStatHandlers("ActionPoints", dispatcher, eventMap, Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, Components.Stats.ActionPoints.ACTIVE);
        defaultStatHandlers("MovePoints", dispatcher, eventMap, Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, Components.Stats.MovePoints.ACTIVE);

        defaultStatHandlers("AirPower", dispatcher, eventMap, Components.Stats.Power.Air.BASE, Components.Stats.Power.Air.BUFFED, Components.Stats.Power.Air.ADDITIVE, Components.Stats.Power.Air.ACTIVE);
        defaultStatHandlers("FirePower", dispatcher, eventMap, Components.Stats.Power.Fire.BASE, Components.Stats.Power.Fire.BUFFED, Components.Stats.Power.Fire.ADDITIVE, Components.Stats.Power.Fire.ACTIVE);
        defaultStatHandlers("WaterPower", dispatcher, eventMap, Components.Stats.Power.Water.BASE, Components.Stats.Power.Water.BUFFED, Components.Stats.Power.Water.ADDITIVE, Components.Stats.Power.Water.ACTIVE);
        defaultStatHandlers("EarthPower", dispatcher, eventMap, Components.Stats.Power.Earth.BASE, Components.Stats.Power.Earth.BUFFED, Components.Stats.Power.Earth.ADDITIVE, Components.Stats.Power.Earth.ACTIVE);

        defaultStatHandlers("AirToughness", dispatcher, eventMap, Components.Stats.Toughness.Air.BASE, Components.Stats.Toughness.Air.BUFFED, Components.Stats.Toughness.Air.ADDITIVE, Components.Stats.Toughness.Air.ACTIVE);
        defaultStatHandlers("FireToughness", dispatcher, eventMap, Components.Stats.Toughness.Fire.BASE, Components.Stats.Toughness.Fire.BUFFED, Components.Stats.Toughness.Fire.ADDITIVE, Components.Stats.Toughness.Fire.ACTIVE);
        defaultStatHandlers("WaterToughness", dispatcher, eventMap, Components.Stats.Toughness.Water.BASE, Components.Stats.Toughness.Water.BUFFED, Components.Stats.Toughness.Water.ADDITIVE, Components.Stats.Toughness.Water.ACTIVE);
        defaultStatHandlers("EarthToughness", dispatcher, eventMap, Components.Stats.Toughness.Earth.BASE, Components.Stats.Toughness.Earth.BUFFED, Components.Stats.Toughness.Earth.ADDITIVE, Components.Stats.Toughness.Earth.ACTIVE);

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
        dispatcher.setHandlers(eventMap.get("PassTurnAction").id(), new PassTurnHandler(eventMap.get("TurnEnd")));
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

        Pokemons pokemons = new Pokemons(data);

        int gameState = data.createEntity();
        data.set(gameState, Components.Arena.SIZE, Coordinates.of(mapWidth, mapHeight));

        int teamA = data.createEntity();
        int teamB = data.createEntity();
        data.set(teamA, Components.NEXT_TEAM, teamB);
        data.set(teamB, Components.NEXT_TEAM, teamA);

        int human1 = 0;
        int human2 = 1;

        int bulbasaur = data.createEntity();
        pokemons.bulbasaur(bulbasaur);
        data.set(bulbasaur, Components.CONTROLLED_BY, human1);
        data.set(bulbasaur, Components.MEMBER_OF, teamA);
        data.set(bulbasaur, Components.POSITION, Coordinates.of(1, 1));
        data.set(bulbasaur, Components.Abilities.WALK, 0);
        data.set(bulbasaur, Components.Abilities.PASS_TURN, 0);
        data.set(bulbasaur, Components.Abilities.RAZORLEAF, 4);

        int testBuff = data.createEntity();
        data.set(testBuff, Components.Stats.Health.ADDITIVE, 700);
        data.set(testBuff, Components.BUFF_ON, bulbasaur);

        int charmander = data.createEntity();
        pokemons.charmander(charmander);
        data.set(charmander, Components.CONTROLLED_BY, human2);
        data.set(charmander, Components.MEMBER_OF, teamB);
        data.set(charmander, Components.POSITION, Coordinates.of(5, 9));
        data.set(charmander, Components.Abilities.WALK, 0);
        data.set(charmander, Components.Abilities.PASS_TURN, 0);

        int squirtle = data.createEntity();
        pokemons.squirtle(squirtle);
        data.set(squirtle, Components.CONTROLLED_BY, human1);
        data.set(squirtle, Components.MEMBER_OF, teamB);
        data.set(squirtle, Components.POSITION, Coordinates.of(3, 5));
        data.set(squirtle, Components.Abilities.WALK, 0);
        data.set(squirtle, Components.Abilities.PASS_TURN, 0);

        for (int y = 4; y < 6; y++) {
            for (int x = 4; x < 6; x++) {
                int obstacle = data.createEntity();
                data.set(obstacle, Components.POSITION, Coordinates.of(x, y));
                data.set(obstacle, Components.Arena.OBSTACLE, 0);
            }
        }

        Map<Integer, Character> actorShortcuts = new MapBuilder<Integer, Character>()
                .with(bulbasaur, 'b')
                .with(charmander, 'c')
                .with(squirtle, 's')
                .build();
        Logger log = loggerFactory.apply(Main.class);
        events.action(eventMap.get("GameStart").id());
        while (true) {
            logState(log, debugMapper, data, actorShortcuts, mapWidth, mapHeight);
            Action action = chooseAction(data, Components.ACTIVE_TURN, actions, eventDefinitions, actorShortcuts);
            events.action(action.eventId, action.eventArgs);
        }

    }

    private static void defaultStatHandlers(String statName, GameEventDispatcher dispatcher, Map<String, EventDefinition> eventMap, int base, int buffed, int additive, int active) {
        dispatcher.setHandlers(eventMap.get("SetActive" + statName).id(), new SetComponentHandler("Active" + statName, active));
        dispatcher.setHandlers(eventMap.get("SetBuffed" + statName).id(), new SetComponentHandler("Buffed" + statName, buffed));
        dispatcher.setHandlers(eventMap.get("SetAdditive" + statName).id(), new SetComponentHandler("Additive" + statName, additive));
        dispatcher.setHandlers(eventMap.get("SetBase" + statName).id(), new SetComponentHandler("Base" + statName, base));
        dispatcher.setHandlers(eventMap.get("UpdateBuffed" + statName).id(), new UpdateBuffedStatHandler(statName, base, additive, eventMap.get("SetBuffed" + statName).id()));
        dispatcher.setHandlers(eventMap.get("ResetActive" + statName).id(), new ResetActiveStatHandler(statName, Components.Stats.Health.BUFFED, eventMap.get("SetActive" + statName).id()));
    }

    private static void logState(Logger log, EntityDebugObjectMapper builder, SimpleEntityData data, Map<Integer, Character> actorShortcuts, int mapWidth, int mapHeight) throws IOException {
        if (log.isDebugEnabled()) {
            log.debug("{}", GSON.toJson(builder.toDebugObjects(data, Components.DEFINITIONS)));
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream(); PrintStream ps = new PrintStream(baos, true, StandardCharsets.UTF_8.name())) {
                new MapPrinter().printMap(actorShortcuts, mapWidth, mapHeight, data, Components.POSITION, ps);
                log.debug("{}", new String(baos.toByteArray(), StandardCharsets.UTF_8));
            }
        }
    }

    private static Action chooseAction(EntityData data, int activeTurn, ActionGenerator actions, EventDefinition[] events, Map<Integer, Character> actorShortcuts) {
        for (int actor : data.query(activeTurn).list()) {
            System.out.println("availableActions of " + actorShortcuts.get(actor) + "(" + actor + "): " + actions.availableActions(actor).stream().map(x -> events[EventDefinition.eventIndex(x.eventId)].lazyString(x.eventArgs)).collect(Collectors.toList()));
        }
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("actor");
            int actor = data.query(activeTurn).list().get(scanner.nextInt());
            System.out.println("action");
            int action = scanner.nextInt();
            return actions.availableActions(actor).get(action);
        }
    }

}
