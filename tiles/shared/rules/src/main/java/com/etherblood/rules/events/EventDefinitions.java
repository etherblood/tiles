package com.etherblood.rules.events;

import com.etherblood.events.EventMeta;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class EventDefinitions {

    public final StatEvents health;
    public final StatEvents actionPoints;
    public final StatEvents movePoints;
    public final ElementStatEvents power;
    public final ElementStatEvents toughness;
    public final EntityValueEventMeta setActivePlayer;
    public final EntityValueEventMeta setActiveTeam;
    public final EntityValueEventMeta setAlive;
    public final EntityCoordinatesEventMeta setPosition;
    public final EntityMoveEventMeta walkToTargetEffect;
//    public final EntityEventMeta passTurnAction;
//    public final EntityValueEventMeta razorleafAction;
    public final VoidEventMeta gameStart;
    public final EntityEventMeta turnStart;
    public final EntityEventMeta turnEnd;
    public final VoidEventMeta gameOver;
    public final DamageEvents dealDamage;
    public final ElementAttackEvents attack;
    public final EntityEventMeta useSkill;
    public final EntityCoordinatesEventMeta useTargetedSkill;

    public EventDefinitions(List<EventMeta<?>> events) {
        health = new StatEvents("Health", events);
        actionPoints = new StatEvents("ActionPoints", events);
        movePoints = new StatEvents("MovePoints", events);
        power = new ElementStatEvents("Power", events);
        toughness = new ElementStatEvents("Toughness", events);
        setActivePlayer = entityValueEvent(events, "SetActivePlayer");
        setActiveTeam = entityValueEvent(events, "SetActiveTeam");
        setAlive = entityValueEvent(events, "SetAlive");
        setPosition = entityCoordinatesEvent(events, "SetPosition");
        walkToTargetEffect = entityMoveEvent(events, "WalkToTargetEffect");
//        passTurnAction = entityEvent(events, "PassTurnAction");
//        razorleafAction = entityValueEvent(events, "RazorleafAction");
        gameStart = voidEvent(events, "GameStart");
        turnStart = entityEvent(events, "TurnStart");
        turnEnd = entityEvent(events, "TurnEnd");
        gameOver = voidEvent(events, "GameOver");
        dealDamage = new DamageEvents("DealElementalDamage", events);
        attack = new ElementAttackEvents("Attack", events);
        useSkill = entityEvent(events, "UseSkill");
        useTargetedSkill = entityCoordinatesEvent(events, "UseTargetedSkill");
    }

    private static VoidEventMeta voidEvent(List<EventMeta<?>> events, String name) {
        VoidEventMeta event = new VoidEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    private static EntityEventMeta entityEvent(List<EventMeta<?>> events, String name) {
        EntityEventMeta event = new EntityEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    private static EntityValueEventMeta entityValueEvent(List<EventMeta<?>> events, String name) {
        EntityValueEventMeta event = new EntityValueEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    private static SourceTargetValueEventMeta sourceTargetValueEvent(List<EventMeta<?>> events, String name) {
        SourceTargetValueEventMeta event = new SourceTargetValueEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    private static EntityCoordinatesEventMeta entityCoordinatesEvent(List<EventMeta<?>> events, String name) {
        EntityCoordinatesEventMeta event = new EntityCoordinatesEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    private static EntityMoveEventMeta entityMoveEvent(List<EventMeta<?>> events, String name) {
        EntityMoveEventMeta event = new EntityMoveEventMeta(events.size(), name);
        events.add(event);
        return event;
    }

    public static class StatEvents {

        public final EntityValueEventMeta setActive;
        public final EntityValueEventMeta setBase;
        public final EntityValueEventMeta setBuffed;
        public final EntityValueEventMeta setAdditive;
        public final EntityEventMeta updateBuffed;
        public final EntityEventMeta resetActive;

        public StatEvents(String statName, List<EventMeta<?>> events) {
            this.setActive = entityValueEvent(events, "Active" + statName);
            this.setBase = entityValueEvent(events, "Base" + statName);
            this.setBuffed = entityValueEvent(events, "Buffed" + statName);
            this.setAdditive = entityValueEvent(events, "Additive" + statName);
            this.updateBuffed = entityEvent(events, "UpdateBuffed" + statName);
            this.resetActive = entityEvent(events, "ResetActive" + statName);
        }

    }

    public static class ElementStatEvents {

        public final StatEvents fire;
        public final StatEvents water;
        public final StatEvents air;
        public final StatEvents earth;

        public ElementStatEvents(String statName, List<EventMeta<?>> events) {
            this.fire = new StatEvents("Fire" + statName, events);
            this.water = new StatEvents("Water" + statName, events);
            this.air = new StatEvents("Air" + statName, events);
            this.earth = new StatEvents("Earth" + statName, events);
        }
    }

    public static class DamageEvents {

        public final EntityValueEventMeta fire;
        public final EntityValueEventMeta water;
        public final EntityValueEventMeta air;
        public final EntityValueEventMeta earth;

        public DamageEvents(String statName, List<EventMeta<?>> events) {
            this.fire = entityValueEvent(events, "Fire" + statName);
            this.water = entityValueEvent(events, "Water" + statName);
            this.air = entityValueEvent(events, "Air" + statName);
            this.earth = entityValueEvent(events, "Earth" + statName);
        }
    }

    public static class ElementAttackEvents {

        public final SourceTargetValueEventMeta fire;
        public final SourceTargetValueEventMeta water;
        public final SourceTargetValueEventMeta air;
        public final SourceTargetValueEventMeta earth;

        public ElementAttackEvents(String statName, List<EventMeta<?>> events) {
            this.fire = sourceTargetValueEvent(events, "Fire" + statName);
            this.water = sourceTargetValueEvent(events, "Water" + statName);
            this.air = sourceTargetValueEvent(events, "Air" + statName);
            this.earth = sourceTargetValueEvent(events, "Earth" + statName);
        }
    }

}
