package com.etherblood.rules;

import com.etherblood.entities.EntityData;
import com.etherblood.entities.IdSequences;
import com.etherblood.entities.SimpleEntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.EventQueue;
import com.etherblood.events.SimpleEventQueue;
import com.etherblood.rules.abilities.ActionAggregator;
import com.etherblood.rules.abilities.ActionGenerator;
import com.etherblood.rules.abilities.passTurn.PassTurnGenerator;
import com.etherblood.rules.abilities.passTurn.PassTurnHandler;
import com.etherblood.rules.abilities.razorleaf.RazorleafGenerator;
import com.etherblood.rules.abilities.razorleaf.RazorleafHandler;
import com.etherblood.rules.abilities.walk.WalkGenerator;
import com.etherblood.rules.abilities.walk.WalkHandler;
import com.etherblood.rules.battle.DamageHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.components.GameEventDispatcher;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.EntityMoveEvent;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.events.VoidEvent;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.movement.Coordinates;
import com.etherblood.rules.stats.RefreshAllStatHandler;
import com.etherblood.rules.stats.ResetActiveStatHandler;
import com.etherblood.rules.stats.SetComponentHandler;
import com.etherblood.rules.stats.UpdateBuffedStatHandler;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.function.IntPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class GameContext {

    private static final Logger LOG = LoggerFactory.getLogger(GameContext.class);
    private final RandomInts random = new RandomTracker(new Random(453));
    private final EntityData data = new SimpleEntityData(Components.DEFINITIONS, IdSequences.simple2());
    private final ActionGenerator actions;
    private final EventQueue events;
    private final Event startGameEvent;
    private final Runnable turnManager;
    public final int mapWidth = 10;
    public final int mapHeight = 10;
    private final EventDefinition[] eventDefinitions;

    public GameContext() {
        Map<String, EventDefinition> eventMap = new EventDefGenerator().generate();
        eventDefinitions = new EventDefinition[eventMap.size()];
        for (EventDefinition value : eventMap.values()) {
            eventDefinitions[value.id()] = value;
        }
        events = new SimpleEventQueue(eventDefinitions.length);
        GameEventDispatcher dispatcher = new GameEventDispatcher(data, events, random::nextInt);
        startGameEvent = new VoidEvent(eventMap.get("GameStart").id());
        IntPredicate positionAvailability = p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !data.query(Components.POSITION).exists(e -> data.hasValue(e, Components.POSITION, p));
        actions = new ActionAggregator(
                new RazorleafGenerator(data, eventMap.get("RazorleafAction").id()),
                new WalkGenerator(data, positionAvailability, eventMap.get("WalkAction").id()),
                new PassTurnGenerator(data, eventMap.get("PassTurnAction").id()));
        final int turnEndId = eventMap.get("TurnEnd").id();
        final int turnStartId = eventMap.get("TurnStart").id();
        turnManager = () -> {
            while (!isGameOver() && !data.query(Components.ACTIVE_PLAYER).exists()) {
                int activeTeam = data.query(Components.ACTIVE_TEAM).unique().getAsInt();
                events.action(new EntityEvent(turnEndId, activeTeam));
                int nextTeam = data.get(activeTeam, Components.NEXT_TEAM);
                events.action(new EntityEvent(turnStartId, nextTeam));
            }
        };

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

        Function<String, EventDefinition<VoidEvent>> voidEvent = eventMap::get;
        Function<String, EventDefinition<EntityEvent>> entityEvent = eventMap::get;
        Function<String, EventDefinition<EntityMoveEvent>> entityMoveEvent = eventMap::get;
        Function<String, EventDefinition<EntityValueEvent>> entityValueEvent = eventMap::get;

        dispatcher.setHandlers(entityValueEvent.apply("SetPosition"),
                dispatcher.init(new SetComponentHandler("position", Components.POSITION))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("SetActivePlayer"),
                dispatcher.init(new SetComponentHandler("activePlayer", Components.ACTIVE_PLAYER))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("SetActiveTeam"),
                dispatcher.init(new SetComponentHandler("activeTeam", Components.ACTIVE_TEAM))::handle);

        dispatcher.setHandlers(entityValueEvent.apply("EarthDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setHandlers(entityValueEvent.apply("FireDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setHandlers(entityValueEvent.apply("AirDamage"),
                dispatcher.init(new DamageHandler())::handle);
        dispatcher.setHandlers(entityValueEvent.apply("WaterDamage"),
                dispatcher.init(new DamageHandler())::handle);

        dispatcher.setHandlers(entityMoveEvent.apply("WalkAction"),
                dispatcher.init(new WalkHandler(eventMap.get("SetPosition")))::handle);
        dispatcher.setHandlers(entityEvent.apply("PassTurnAction"),
                dispatcher.init(new PassTurnHandler(eventMap.get("TurnEnd")))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("RazorleafAction"),
                dispatcher.init(new RazorleafHandler(eventMap.get("EarthDamage").id()))::handle);

        dispatcher.setHandlers(voidEvent.apply("GameStart"),
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
        dispatcher.setHandlers(entityEvent.apply("TurnEnd"),
                dispatcher.init(new TurnEndHandler(
                        eventMap.get("ResetActiveActionPoints").id(),
                        eventMap.get("ResetActiveMovePoints").id(),
                        eventMap.get("SetActiveTeam").id()))::handle);

        dispatcher.setHandlers(entityEvent.apply("TurnStart"),
                dispatcher.init(new TurnStartHandler(
                        eventMap.get("SetActivePlayer").id(),
                        eventMap.get("SetActiveTeam").id()))::handle);

    }

    private static void defaultStatHandlers(String statName, GameEventDispatcher dispatcher, Map<String, EventDefinition> eventMap, int base, int buffed, int additive, int active) {

        Function<String, EventDefinition<VoidEvent>> voidEvent = eventMap::get;
        Function<String, EventDefinition<EntityEvent>> entityEvent = eventMap::get;
        Function<String, EventDefinition<EntityMoveEvent>> entityMoveEvent = eventMap::get;
        Function<String, EventDefinition<EntityValueEvent>> entityValueEvent = eventMap::get;

        dispatcher.setHandlers(entityValueEvent.apply("SetActive" + statName),
                dispatcher.init(new SetComponentHandler("Active" + statName, active))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("SetBuffed" + statName),
                dispatcher.init(new SetComponentHandler("Buffed" + statName, buffed))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("SetAdditive" + statName),
                dispatcher.init(new SetComponentHandler("Additive" + statName, additive))::handle);
        dispatcher.setHandlers(entityValueEvent.apply("SetBase" + statName),
                dispatcher.init(new SetComponentHandler("Base" + statName, base))::handle);
        dispatcher.setHandlers(entityEvent.apply("UpdateBuffed" + statName),
                dispatcher.init(new UpdateBuffedStatHandler(statName, base, additive, eventMap.get("SetBuffed" + statName).id()))::handle);
        dispatcher.setHandlers(entityEvent.apply("ResetActive" + statName),
                dispatcher.init(new ResetActiveStatHandler(statName, buffed, eventMap.get("SetActive" + statName).id()))::handle);
    }

    public RandomInts getRandom() {
        return random;
    }

    public EntityData getData() {
        return data;
    }

    public ActionGenerator getActions() {
        return actions;
    }

    public void startGame() {
        events.action(startGameEvent);
    }

    public boolean isGameOver() {
        LOG.warn("game over not implemented");
        return false;
    }

    public void action(Event event) {
        events.action(event);
        turnManager.run();
    }

    public EventDefinition getEventDefinition(int id) {
        return eventDefinitions[id];
    }
}
