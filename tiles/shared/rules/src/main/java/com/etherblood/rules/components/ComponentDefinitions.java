package com.etherblood.rules.components;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.rules.movement.Coordinates;
import java.util.List;
import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ComponentDefinitions {

    public final StatComponents health;
    public final StatComponents actionPoints;
    public final StatComponents movePoints;
    public final ElementStatComponents power;
    public final ElementStatComponents toughness;
    public final ComponentMeta passTurnAbility;
    public final ComponentMeta walkAbility;
    public final ComponentMeta razorleafAbility;
    public final ComponentMeta arenaSize;
    public final ComponentMeta arenaObstacle;
    public final ComponentMeta controlledBy;
    public final ComponentMeta nextTeam;
    public final ComponentMeta position;
    public final ComponentMeta buffOn;
    public final ComponentMeta activePlayer;
    public final ComponentMeta activeTeam;
    public final ComponentMeta sprite;
    public final ComponentMeta memberOf;

    public ComponentDefinitions(List<ComponentMeta> components) {
        Object empty = new Object();
        health = new StatComponents("Health", components);
        actionPoints = new StatComponents("ActionPoints", components);
        movePoints = new StatComponents("MovePoints", components);
        power = new ElementStatComponents("Power", components);
        toughness = new ElementStatComponents("Toughness", components);
        passTurnAbility = create("PassTurnAbility", components);
        walkAbility = create("WalkAbility", components);
        razorleafAbility = create("RazorleafAbility", components);
        arenaSize = create("ArenaSize", Coordinates::toString, components);
        arenaObstacle = create("ArenaObstacle", x -> empty, components);
        controlledBy = create("ControlledBy", components);
        nextTeam = create("NextTeam", components);
        position = create("Position", Coordinates::toString, components);
        buffOn = create("BuffOn", components);
        activePlayer = create("ActivePlayer", components);
        activeTeam = create("ActiveTeam", components);
        sprite = create("Sprite", components);
        memberOf = create("MemberOf", components);
    }

    private static ComponentMeta create(String name, List<ComponentMeta> components) {
        return create(name, x -> x, components);
    }

    private static ComponentMeta create(String name, IntFunction<Object> objectify, List<ComponentMeta> components) {
        ComponentMeta result = ComponentMeta.builder().withName(name).withId(components.size()).withObjectify(objectify).build();
        components.add(result);
        return result;
    }

    public static class StatComponents {

        public final ComponentMeta base;
        public final ComponentMeta additive;
        public final ComponentMeta active;
        public final ComponentMeta buffed;

        public StatComponents(String statName, List<ComponentMeta> components) {
            active = create("Active" + statName, components);
            base = create("Base" + statName, components);
            buffed = create("Buffed" + statName, components);
            additive = create("Additive" + statName, components);
        }
    }

    public static class ElementStatComponents {

        public final StatComponents fire;
        public final StatComponents water;
        public final StatComponents earth;
        public final StatComponents air;

        public ElementStatComponents(String name, List<ComponentMeta> components) {
            fire = new StatComponents("Fire" + name, components);
            water = new StatComponents("Water" + name, components);
            earth = new StatComponents("Earth" + name, components);
            air = new StatComponents("Air" + name, components);
        }
    }
}
