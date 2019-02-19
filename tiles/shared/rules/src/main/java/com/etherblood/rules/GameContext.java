package com.etherblood.rules;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntHashSet;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import com.etherblood.entities.IdSequences;
import com.etherblood.entities.SimpleEntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventMeta;
import com.etherblood.events.EventQueue;
import com.etherblood.events.SimpleEventQueue;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.battle.DeathWithoutHealthHandler;
import com.etherblood.rules.battle.ElementalAttackHandler;
import com.etherblood.rules.battle.ElementalDamageHandler;
import com.etherblood.rules.battle.EndTurnOnDeathHandler;
import com.etherblood.rules.components.ComponentDefinitions;
import com.etherblood.rules.components.GameEventDispatcher;
import com.etherblood.rules.events.EventDefinitions;
import com.etherblood.rules.game.setup.config.GameConfig;
import com.etherblood.rules.game.turns.StartTurnOfRandomTeamHandler;
import com.etherblood.rules.game.turns.TurnEndHandler;
import com.etherblood.rules.game.turns.TurnStartHandler;
import com.etherblood.rules.util.Coordinates;
import com.etherblood.rules.movement.SetCoordinatesHandler;
import com.etherblood.rules.stats.RefreshAllStatHandler;
import com.etherblood.rules.stats.ResetActiveStatHandler;
import com.etherblood.rules.stats.SetStatHandler;
import com.etherblood.rules.stats.UpdateBuffedStatHandler;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
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
//    private final ActionGenerator actions;
    private final EventQueue events;
    private final Event startGameEvent;
    public final List<ComponentMeta> componentMetaList;
    public final List<EventMeta> eventMetaList;
    public final ComponentDefinitions componentDefs;
    public final EventDefinitions eventDefs;
    public GameConfig config;

    public GameContext() {
        this(e -> {
        });
    }

    public GameContext(EventHandler globalHandler) {
        List<ComponentMeta> componentList = new ArrayList<>();
        componentDefs = new ComponentDefinitions(componentList);
        componentMetaList = Collections.unmodifiableList(componentList);
        data = new SimpleEntityData(componentList.size(), IdSequences.incremental());
        List<EventMeta<?>> eventList = new ArrayList<>();
        eventDefs = new EventDefinitions(eventList);
        eventMetaList = Collections.unmodifiableList(eventList);
        LOG.debug("Registered events: {}.", eventList);
        GameEventDispatcher dispatcher = new GameEventDispatcher(data, new SimpleEventQueue(eventList.size(), globalHandler), random::nextInt);
        events = dispatcher.getQueue();
        startGameEvent = eventDefs.gameStart.create();
//        IntPredicate positionAvailability = p -> Coordinates.inBounds(p, mapSize()) && !data.query(componentDefs.position.id).exists(e -> data.hasValue(e, componentDefs.position.id, p));
//        actions = new ActionAggregator(
//                new RazorleafGenerator(data, eventDefs.razorleafAction, componentDefs.razorleafSkill, componentDefs.position, componentDefs.actionPoints.active),
//                new WalkGenerator(data, positionAvailability, eventDefs.walkAction, componentDefs.walkSkill, componentDefs.position, componentDefs.movePoints.active),
//                new PassTurnGenerator(data, eventDefs.passTurnAction, componentDefs.activePlayer));

        defaultStatHandlers("Health", dispatcher, eventDefs.health, componentDefs.health, componentDefs.buffOn);
        dispatcher.addInlineHandlers(eventDefs.health.setActive, dispatcher.init(new DeathWithoutHealthHandler(eventDefs.setAlive, componentDefs.alive.id)));
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

        dispatcher.addInlineHandlers(eventDefs.setPosition, dispatcher.init(new SetCoordinatesHandler("position", componentDefs.position)));
        dispatcher.addInlineHandlers(eventDefs.setActivePlayer, dispatcher.init(new SetStatHandler("activePlayer", componentDefs.activePlayer)));
        dispatcher.addInlineHandlers(eventDefs.setActiveTeam, dispatcher.init(new SetStatHandler("activeTeam", componentDefs.activeTeam)));
        dispatcher.addInlineHandlers(eventDefs.setAlive,
                dispatcher.init(new SetStatHandler("alive", componentDefs.alive)),
                dispatcher.init(new EndTurnOnDeathHandler(eventDefs.setActivePlayer, componentDefs.activePlayer.id)));

        dispatcher.addInlineHandlers(eventDefs.attack.earth,
                dispatcher.init(new ElementalAttackHandler("earth", componentDefs.power.earth.active, componentDefs.toughness.earth.active, eventDefs.dealDamage.earth)));
        dispatcher.addInlineHandlers(eventDefs.attack.water,
                dispatcher.init(new ElementalAttackHandler("water", componentDefs.power.water.active, componentDefs.toughness.water.active, eventDefs.dealDamage.water)));
        dispatcher.addInlineHandlers(eventDefs.attack.fire,
                dispatcher.init(new ElementalAttackHandler("fire", componentDefs.power.fire.active, componentDefs.toughness.fire.active, eventDefs.dealDamage.fire)));
        dispatcher.addInlineHandlers(eventDefs.attack.air,
                dispatcher.init(new ElementalAttackHandler("air", componentDefs.power.air.active, componentDefs.toughness.air.active, eventDefs.dealDamage.air)));

        dispatcher.addInlineHandlers(eventDefs.dealDamage.earth,
                dispatcher.init(new ElementalDamageHandler("earth", componentDefs.health.active, eventDefs.health.setActive)));
        dispatcher.addInlineHandlers(eventDefs.dealDamage.fire,
                dispatcher.init(new ElementalDamageHandler("fire", componentDefs.health.active, eventDefs.health.setActive)));
        dispatcher.addInlineHandlers(eventDefs.dealDamage.air,
                dispatcher.init(new ElementalDamageHandler("air", componentDefs.health.active, eventDefs.health.setActive)));
        dispatcher.addInlineHandlers(eventDefs.dealDamage.water,
                dispatcher.init(new ElementalDamageHandler("water", componentDefs.health.active, eventDefs.health.setActive)));

//        dispatcher.addInlineHandlers(eventDefs.walkAction,
//                dispatcher.init(new WalkHandler(eventDefs.setPosition, componentDefs.movePoints.active)));
//        dispatcher.addInlineHandlers(eventDefs.passTurnAction,
//                dispatcher.init(new PassTurnHandler(componentDefs.activePlayer)));
//        dispatcher.addInlineHandlers(eventDefs.razorleafAction,
//                dispatcher.init(new RazorleafHandler(eventDefs.attack.earth, componentDefs.razorleafSkill, componentDefs.actionPoints.active)));

        dispatcher.addInlineHandlers(eventDefs.gameStart,
                dispatcher.init(createRefreshAllStatHandler("health", eventDefs.health, componentDefs.health, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("movePoints", eventDefs.movePoints, componentDefs.movePoints, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("actionPoints", eventDefs.actionPoints, componentDefs.actionPoints, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("airPower", eventDefs.power.air, componentDefs.power.air, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("earthPower", eventDefs.power.earth, componentDefs.power.earth, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("firePower", eventDefs.power.fire, componentDefs.power.fire, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("waterPower", eventDefs.power.water, componentDefs.power.water, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("airToughness", eventDefs.toughness.air, componentDefs.toughness.air, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("earthToughness", eventDefs.toughness.earth, componentDefs.toughness.earth, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("fireToughness", eventDefs.toughness.fire, componentDefs.toughness.fire, componentDefs.buffOn)),
                dispatcher.init(createRefreshAllStatHandler("waterToughness", eventDefs.toughness.water, componentDefs.toughness.water, componentDefs.buffOn)),
                dispatcher.init(new StartTurnOfRandomTeamHandler(eventDefs.turnStart, componentDefs.nextTeam)));

        dispatcher.addInlineHandlers(eventDefs.turnEnd,
                dispatcher.init(new TurnEndHandler(eventDefs.actionPoints.resetActive, eventDefs.movePoints.resetActive, eventDefs.setActiveTeam, componentDefs.memberOf)));
        dispatcher.addQueueHandlers(eventDefs.turnStart,
                dispatcher.init(new TurnStartHandler(eventDefs.setActivePlayer, eventDefs.setActiveTeam, componentDefs.memberOf.id, componentDefs.alive.id)));

    }

    private IntPredicate aliveMembersOf(int team) {
        return x -> data.hasValue(x, componentDefs.memberOf.id, team) && data.has(x, componentDefs.alive.id);
    }

    private static RefreshAllStatHandler createRefreshAllStatHandler(String name, EventDefinitions.StatEvents statEvents, ComponentDefinitions.StatComponents statComponents, ComponentMeta buffOn) {
        return new RefreshAllStatHandler(name, statComponents.base, statComponents.active, statComponents.buffed, statComponents.additive, buffOn, statEvents.updateBuffed, statEvents.resetActive);
    }

    private static void defaultStatHandlers(String statName, GameEventDispatcher dispatcher, EventDefinitions.StatEvents eventMap, ComponentDefinitions.StatComponents statComponents, ComponentMeta buffOn) {
        dispatcher.addInlineHandlers(eventMap.setActive, dispatcher.init(new SetStatHandler("Active" + statName, statComponents.active)));
        dispatcher.addInlineHandlers(eventMap.setBuffed, dispatcher.init(new SetStatHandler("Buffed" + statName, statComponents.buffed)));
        dispatcher.addInlineHandlers(eventMap.setAdditive, dispatcher.init(new SetStatHandler("Additive" + statName, statComponents.additive)));
        dispatcher.addInlineHandlers(eventMap.setBase, dispatcher.init(new SetStatHandler("Base" + statName, statComponents.base)));
        dispatcher.addInlineHandlers(eventMap.updateBuffed, dispatcher.init(new UpdateBuffedStatHandler(statName, statComponents.base, statComponents.additive, buffOn, eventMap.setBuffed)));
        dispatcher.addInlineHandlers(eventMap.resetActive, dispatcher.init(new ResetActiveStatHandler(statName, statComponents.buffed, eventMap.setActive)));
    }

    public int mapSize() {
        return data.get(data.query(componentDefs.mapSize.id).unique().getAsInt(), componentDefs.mapSize.id);
    }

    public int mapWidth() {
        return Coordinates.x(mapSize());
    }

    public int mapHeight() {
        return Coordinates.y(mapSize());
    }

    public RandomInts getRandom() {
        return random;
    }

    public EntityData getData() {
        return data;
    }

    public void startGame() {
        events.action(startGameEvent);
    }

    public boolean isGameOver() {
        IntArrayList actors = data.query(componentDefs.memberOf.id).list(x -> data.has(x, componentDefs.alive.id));
        IntHashSet teams = new IntHashSet();
        for (int actor : actors) {
            teams.set(data.get(actor, componentDefs.memberOf.id));
        }
        return teams.size() <= 1;
    }

    public void action(Event event) {
        events.action(event);
        while (!isGameOver() && !data.query(componentDefs.activePlayer.id).exists()) {
            int activeTeam = data.query(componentDefs.activeTeam.id).unique().getAsInt();
            events.action(eventDefs.turnEnd.create(activeTeam));
            int nextTeam = data.get(activeTeam, componentDefs.nextTeam.id);
            while (!data.query(componentDefs.memberOf.id).exists(aliveMembersOf(nextTeam))) {
                nextTeam = data.get(nextTeam, componentDefs.nextTeam.id);
            }
            events.action(eventDefs.turnStart.create(nextTeam));
        }
    }
}
