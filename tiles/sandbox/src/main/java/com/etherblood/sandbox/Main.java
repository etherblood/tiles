package com.etherblood.sandbox;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.MapBuilder;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.IdSequences;
import com.etherblood.entities.SimpleEntityData;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventQueueImpl;
import com.etherblood.rules.RandomTracker;
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
    public static void main(String[] args) throws IllegalArgumentException, IOException {
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

        dispatcher.setBinaryHandlers(eventMap.get("SetPosition"),
                dispatcher.init(new SetComponentHandler("position", Components.POSITION))::handle);
        dispatcher.setBinaryHandlers(eventMap.get("SetActive"),
                dispatcher.init(new SetComponentHandler("active", Components.ACTIVE_TURN))::handle);

        dispatcher.setBinaryHandlers(eventMap.get("EarthDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setBinaryHandlers(eventMap.get("FireDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setBinaryHandlers(eventMap.get("AirDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setBinaryHandlers(eventMap.get("WaterDamage"),
                dispatcher.init(new DamageHandler())::handle);

        dispatcher.setTernaryHandlers(eventMap.get("WalkAction"),
                dispatcher.init(new WalkHandler(eventMap.get("SetPosition")))::handle);
        dispatcher.setUnaryHandlers(eventMap.get("PassTurnAction"),
                dispatcher.init(new PassTurnHandler(eventMap.get("TurnEnd")))::handle);
        dispatcher.setBinaryHandlers(eventMap.get("RazorleafAction"),
                dispatcher.init(new RazorleafHandler(eventMap.get("EarthDamage").id()))::handle);

        dispatcher.setNullaryHandlers(eventMap.get("GameStart"),
                dispatcher.init(new RefreshAllStatHandler("health", Components.Stats.Health.BASE, Components.Stats.Health.ACTIVE, Components.Stats.Health.BUFFED, Components.Stats.Health.ADDITIVE, eventMap.get("UpdateBuffedHealth").id(), eventMap.get("ResetActiveHealth").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("movePoints", Components.Stats.MovePoints.BASE, Components.Stats.MovePoints.ACTIVE, Components.Stats.MovePoints.BUFFED, Components.Stats.MovePoints.ADDITIVE, eventMap.get("UpdateBuffedMovePoints").id(), eventMap.get("ResetActiveMovePoints").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("actionPoints", Components.Stats.ActionPoints.BASE, Components.Stats.ActionPoints.ACTIVE, Components.Stats.ActionPoints.BUFFED, Components.Stats.ActionPoints.ADDITIVE, eventMap.get("UpdateBuffedActionPoints").id(), eventMap.get("ResetActiveActionPoints").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("airPower", Components.Stats.Power.Air.BASE, Components.Stats.Power.Air.ACTIVE, Components.Stats.Power.Air.BUFFED, Components.Stats.Power.Air.ADDITIVE, eventMap.get("UpdateBuffedAirPower").id(), eventMap.get("ResetActiveAirPower").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("earthPower", Components.Stats.Power.Earth.BASE, Components.Stats.Power.Earth.ACTIVE, Components.Stats.Power.Earth.BUFFED, Components.Stats.Power.Earth.ADDITIVE, eventMap.get("UpdateBuffedEarthPower").id(), eventMap.get("ResetActiveEarthPower").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("firePower", Components.Stats.Power.Fire.BASE, Components.Stats.Power.Fire.ACTIVE, Components.Stats.Power.Fire.BUFFED, Components.Stats.Power.Fire.ADDITIVE, eventMap.get("UpdateBuffedFirePower").id(), eventMap.get("ResetActiveFirePower").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("waterPower", Components.Stats.Power.Water.BASE, Components.Stats.Power.Water.ACTIVE, Components.Stats.Power.Water.BUFFED, Components.Stats.Power.Water.ADDITIVE, eventMap.get("UpdateBuffedWaterPower").id(), eventMap.get("ResetActiveWaterPower").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("airToughness", Components.Stats.Toughness.Air.BASE, Components.Stats.Toughness.Air.ACTIVE, Components.Stats.Toughness.Air.BUFFED, Components.Stats.Toughness.Air.ADDITIVE, eventMap.get("UpdateBuffedAirToughness").id(), eventMap.get("ResetActiveAirToughness").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("earthToughness", Components.Stats.Toughness.Earth.BASE, Components.Stats.Toughness.Earth.ACTIVE, Components.Stats.Toughness.Earth.BUFFED, Components.Stats.Toughness.Earth.ADDITIVE, eventMap.get("UpdateBuffedEarthToughness").id(), eventMap.get("ResetActiveEarthToughness").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("fireToughness", Components.Stats.Toughness.Fire.BASE, Components.Stats.Toughness.Fire.ACTIVE, Components.Stats.Toughness.Fire.BUFFED, Components.Stats.Toughness.Fire.ADDITIVE, eventMap.get("UpdateBuffedFireToughness").id(), eventMap.get("ResetActiveFireToughness").id()))::handle,
                dispatcher.init(new RefreshAllStatHandler("waterToughness", Components.Stats.Toughness.Water.BASE, Components.Stats.Toughness.Water.ACTIVE, Components.Stats.Toughness.Water.BUFFED, Components.Stats.Toughness.Water.ADDITIVE, eventMap.get("UpdateBuffedWaterToughness").id(), eventMap.get("ResetActiveWaterToughness").id()))::handle,
                dispatcher.init(new StartTurnOfRandomTeamHandler(eventMap.get("TurnStart")))::handle);
        dispatcher.setUnaryHandlers(eventMap.get("TurnEnd"),
                dispatcher.init(new TurnEndHandler(eventMap.get("TurnStart").id(), eventMap.get("ResetActiveActionPoints").id(), eventMap.get("ResetActiveMovePoints").id()))::handle);

        dispatcher.setUnaryHandlers(eventMap.get("TurnStart"),
                dispatcher.init(new TurnStartHandler(
                        eventMap.get("SetActive").id()))::handle);

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
        dispatcher.setBinaryHandlers(eventMap.get("SetActive" + statName),
                dispatcher.init(new SetComponentHandler("Active" + statName, active))::handle);
        dispatcher.setBinaryHandlers(eventMap.get("SetBuffed" + statName),
                dispatcher.init(new SetComponentHandler("Buffed" + statName, buffed))::handle);
        dispatcher.setBinaryHandlers(eventMap.get("SetAdditive" + statName),
                dispatcher.init(new SetComponentHandler("Additive" + statName, additive))::handle);
        dispatcher.setBinaryHandlers(eventMap.get("SetBase" + statName),
                dispatcher.init(new SetComponentHandler("Base" + statName, base))::handle);
        dispatcher.setUnaryHandlers(eventMap.get("UpdateBuffed" + statName),
                dispatcher.init(new UpdateBuffedStatHandler(statName, base, additive, eventMap.get("SetBuffed" + statName).id()))::handle);
        dispatcher.setUnaryHandlers(eventMap.get("ResetActive" + statName),
                dispatcher.init(new ResetActiveStatHandler(statName, buffed, eventMap.get("SetActive" + statName).id()))::handle);
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

    private static Action chooseAction(EntityData data, int active, ActionGenerator actions, EventDefinition[] events, Map<Integer, Character> actorShortcuts) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            IntArrayList actors = data.query(active).list();
            System.out.println("choose actor:");
            for (int i = 0; i < actors.size(); i++) {
                int actor = actors.get(i);
                System.out.println(i + ": " + actorShortcuts.get(actor) + "(" + actor + ")");
            }
            int actor = actors.get(scanner.nextInt());
            List<Action> availableActions = actions.availableActions(actor);
            System.out.println("choose action:");
            for (int i = 0; i < availableActions.size(); i++) {
                Action action = availableActions.get(i);
                System.out.println(i + ": " + events[EventDefinition.eventIndex(action.eventId)].lazyString(action.eventArgs));
            }
            int action = scanner.nextInt();
            return availableActions.get(action);
        }
    }

}
