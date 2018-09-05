package com.etherblood.rules;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.rules.events.EventDefinitions;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.IdSequences;
import com.etherblood.entities.SimpleEntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventMeta;
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
import com.etherblood.rules.components.ComponentDefinitions;
import com.etherblood.rules.components.GameEventDispatcher;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.movement.Coordinates;
import com.etherblood.rules.stats.RefreshAllStatHandler;
import com.etherblood.rules.stats.ResetActiveStatHandler;
import com.etherblood.rules.stats.SetComponentHandler;
import com.etherblood.rules.stats.UpdateBuffedStatHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final EntityData data;
    private final ActionGenerator actions;
    private final EventQueue events;
    private final Event startGameEvent;
    private final Runnable turnManager;
    public final int mapWidth = 10;
    public final int mapHeight = 10;
    public final List<ComponentMeta> componentMetaList;
    public final List<EventMeta> eventMetaList;
    public final ComponentDefinitions componentDefs;
    public final EventDefinitions eventDefs;

    public GameContext() {
        List<ComponentMeta> componentList = new ArrayList<>();
        componentDefs = new ComponentDefinitions(componentList);
        componentMetaList = Collections.unmodifiableList(componentList);
        data = new SimpleEntityData(componentList.size(), IdSequences.incremental());
        List<EventMeta<?>> eventList = new ArrayList<>();
        eventDefs = new EventDefinitions(eventList);
        eventMetaList = Collections.unmodifiableList(eventList);
        events = new SimpleEventQueue(eventList.size());
        GameEventDispatcher dispatcher = new GameEventDispatcher(data, events, random::nextInt);
        startGameEvent = eventDefs.gameStart.create();
        IntPredicate positionAvailability = p -> Coordinates.inBounds(p, Coordinates.of(mapWidth, mapHeight)) && !data.query(componentDefs.position.id).exists(e -> data.hasValue(e, componentDefs.position.id, p));
        actions = new ActionAggregator(
                new RazorleafGenerator(data, eventDefs.razorleafAction, componentDefs.razorleafAbility, componentDefs.position, componentDefs.actionPoints.active),
                new WalkGenerator(data, positionAvailability, eventDefs.walkAction, componentDefs.walkAbility, componentDefs.position, componentDefs.movePoints.active),
                new PassTurnGenerator(data, eventDefs.passTurnAction, componentDefs.passTurnAbility));
        turnManager = () -> {
            while (!isGameOver() && !data.query(componentDefs.activePlayer.id).exists()) {
                int activeTeam = data.query(componentDefs.activeTeam.id).unique().getAsInt();
                events.action(eventDefs.turnEnd.create(activeTeam));
                int nextTeam = data.get(activeTeam, componentDefs.nextTeam.id);
                events.action(eventDefs.turnStart.create(nextTeam));
            }
        };

        defaultStatHandlers("Health", dispatcher, eventDefs.health, componentDefs.health, componentDefs.buffOn);
        defaultStatHandlers("ActionPoints", dispatcher, eventDefs.actionPoints, componentDefs.actionPoints, componentDefs.buffOn);
        defaultStatHandlers("MovePoints", dispatcher, eventDefs.movePoints, componentDefs.movePoints, componentDefs.buffOn);

        defaultStatHandlers("AirPower", dispatcher, eventDefs.power.air, componentDefs.power.air, componentDefs.buffOn);
        defaultStatHandlers("FirePower", dispatcher, eventDefs.power.fire, componentDefs.power.fire, componentDefs.buffOn);
        defaultStatHandlers("WaterPower", dispatcher, eventDefs.power.water, componentDefs.power.water, componentDefs.buffOn);
        defaultStatHandlers("EarthPower", dispatcher, eventDefs.power.earth, componentDefs.power.earth, componentDefs.buffOn);

        defaultStatHandlers("AirToughness", dispatcher, eventDefs.toughness.air, componentDefs.toughness.air, componentDefs.buffOn);
        defaultStatHandlers("FireToughness", dispatcher, eventDefs.toughness.fire, componentDefs.toughness.fire, componentDefs.buffOn);
        defaultStatHandlers("WaterToughness", dispatcher, eventDefs.toughness.water, componentDefs.toughness.water, componentDefs.buffOn);
        defaultStatHandlers("EarthToughness", dispatcher, eventDefs.toughness.earth, componentDefs.toughness.earth, componentDefs.buffOn);

        dispatcher.setHandlers(eventDefs.setPosition, dispatcher.init(new SetComponentHandler("position", componentDefs.position))::handle);
        dispatcher.setHandlers(eventDefs.setActivePlayer, dispatcher.init(new SetComponentHandler("activePlayer", componentDefs.activePlayer))::handle);
        dispatcher.setHandlers(eventDefs.setActiveTeam, dispatcher.init(new SetComponentHandler("activeTeam", componentDefs.activeTeam))::handle);

        DamageHandler damageHandler = new DamageHandler(componentDefs.health.active);
        dispatcher.setHandlers(eventDefs.damage.earth, dispatcher.init(damageHandler)::handle);
        dispatcher.setHandlers(eventDefs.damage.fire, dispatcher.init(damageHandler)::handle);
        dispatcher.setHandlers(eventDefs.damage.air, dispatcher.init(damageHandler)::handle);
        dispatcher.setHandlers(eventDefs.damage.water, dispatcher.init(damageHandler)::handle);

        dispatcher.setHandlers(eventDefs.walkAction,
                dispatcher.init(new WalkHandler(eventDefs.setPosition, componentDefs.movePoints.active))::handle);
        dispatcher.setHandlers(eventDefs.passTurnAction,
                dispatcher.init(new PassTurnHandler(componentDefs.activePlayer))::handle);
        dispatcher.setHandlers(eventDefs.razorleafAction,
                dispatcher.init(new RazorleafHandler(eventDefs.damage.earth, componentDefs.razorleafAbility, componentDefs.actionPoints.active))::handle);

        dispatcher.setHandlers(eventDefs.gameStart,
                dispatcher.init(createRefreshAllStatHandler("health", eventDefs.health, componentDefs.health, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("movePoints", eventDefs.movePoints, componentDefs.movePoints, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("actionPoints", eventDefs.actionPoints, componentDefs.actionPoints, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("airPower", eventDefs.power.air, componentDefs.power.air, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("earthPower", eventDefs.power.earth, componentDefs.power.earth, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("firePower", eventDefs.power.fire, componentDefs.power.fire, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("waterPower", eventDefs.power.water, componentDefs.power.water, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("airToughness", eventDefs.toughness.air, componentDefs.toughness.air, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("earthToughness", eventDefs.toughness.earth, componentDefs.toughness.earth, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("fireToughness", eventDefs.toughness.fire, componentDefs.toughness.fire, componentDefs.buffOn))::handle,
                dispatcher.init(createRefreshAllStatHandler("waterToughness", eventDefs.toughness.water, componentDefs.toughness.water, componentDefs.buffOn))::handle,
                dispatcher.init(new StartTurnOfRandomTeamHandler(eventDefs.turnStart, componentDefs.nextTeam))::handle);

        dispatcher.setHandlers(eventDefs.turnEnd,
                dispatcher.init(new TurnEndHandler(eventDefs.actionPoints.resetActive, eventDefs.movePoints.resetActive, eventDefs.setActiveTeam, componentDefs.memberOf))::handle);
        dispatcher.setHandlers(eventDefs.turnStart,
                dispatcher.init(new TurnStartHandler(eventDefs.setActivePlayer, eventDefs.setActiveTeam, componentDefs.memberOf))::handle);

    }

    private static RefreshAllStatHandler createRefreshAllStatHandler(String name, EventDefinitions.StatEvents statEvents, ComponentDefinitions.StatComponents statComponents, ComponentMeta buffOn) {
        return new RefreshAllStatHandler(name, statComponents.base, statComponents.active, statComponents.buffed, statComponents.additive, buffOn, statEvents.updateBuffed, statEvents.resetActive);
    }

    private static void defaultStatHandlers(String statName, GameEventDispatcher dispatcher, EventDefinitions.StatEvents eventMap, ComponentDefinitions.StatComponents statComponents, ComponentMeta buffOn) {
        dispatcher.setHandlers(eventMap.setActive, dispatcher.init(new SetComponentHandler("Active" + statName, statComponents.active))::handle);
        dispatcher.setHandlers(eventMap.setBuffed, dispatcher.init(new SetComponentHandler("Buffed" + statName, statComponents.buffed))::handle);
        dispatcher.setHandlers(eventMap.setAdditive, dispatcher.init(new SetComponentHandler("Additive" + statName, statComponents.additive))::handle);
        dispatcher.setHandlers(eventMap.setBase, dispatcher.init(new SetComponentHandler("Base" + statName, statComponents.base))::handle);
        dispatcher.setHandlers(eventMap.updateBuffed, dispatcher.init(new UpdateBuffedStatHandler(statName, statComponents.base, statComponents.additive, buffOn, eventMap.setBuffed))::handle);
        dispatcher.setHandlers(eventMap.resetActive, dispatcher.init(new ResetActiveStatHandler(statName, statComponents.buffed, eventMap.setActive))::handle);
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
        LOG.warn("isGameOver() not implemented");
        return false;
    }

    public void action(Event event) {
        events.action(event);
        turnManager.run();
    }
}
