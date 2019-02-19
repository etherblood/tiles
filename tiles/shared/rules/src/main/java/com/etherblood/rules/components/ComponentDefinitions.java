package com.etherblood.rules.components;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.rules.util.Coordinates;
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
    public final ComponentMeta mapSize;
    public final ComponentMeta obstacle;
    public final ComponentMeta blocksPath;
    public final ComponentMeta blocksSight;
    public final ComponentMeta alive;
    public final ComponentMeta controlledBy;
    public final ComponentMeta nextTeam;
    public final ComponentMeta position;
    public final ComponentMeta buffOn;
    public final ComponentMeta activePlayer;
    public final ComponentMeta activeTeam;
    public final ComponentMeta sprite;
    public final ComponentMeta memberOf;
    public final SkillComponents skill;

    public ComponentDefinitions(List<ComponentMeta> components) {
        Object empty = new Object();
        health = new StatComponents("Health", components);
        actionPoints = new StatComponents("ActionPoints", components);
        movePoints = new StatComponents("MovePoints", components);
        power = new ElementStatComponents("Power", components);
        toughness = new ElementStatComponents("Toughness", components);
        mapSize = create("MapSize", Coordinates::toString, components);
        obstacle = create("Obstacle", x -> empty, components);
        blocksPath = create("BlocksPath", x -> empty, components);
        blocksSight = create("BlocksSight", x -> empty, components);
        alive = create("Alive", components);
        controlledBy = create("ControlledBy", components);
        nextTeam = create("NextTeam", components);
        position = create("Position", Coordinates::toString, components);
        buffOn = create("BuffOn", components);
        activePlayer = create("ActivePlayer", components);
        activeTeam = create("ActiveTeam", components);
        sprite = create("Sprite", components);
        memberOf = create("MemberOf", components);
        skill = new SkillComponents("Skill", components);
    }

    private static ComponentMeta create(String name, List<ComponentMeta> components) {
        return create(name, x -> x, components);
    }

    private static ComponentMeta create(String name, IntFunction<Object> objectify, List<ComponentMeta> components) {
        ComponentMeta result = ComponentMeta.builder().withName(name).withId(components.size()).withObjectify(objectify).build();
        components.add(result);
        return result;
    }

    public static class SkillComponents {

        public final ComponentMeta owner;
        public final StatComponents actionPointsCost;
        public final StatComponents movePointsCost;
        public final StatComponents healthCost;
        public final StatComponents cooldown;

        public final TargetingComponents targeting;
        public final EffectComponents effect;

        public SkillComponents(String name, List<ComponentMeta> components) {
            owner = create(name + "Owner", components);
            actionPointsCost = new StatComponents(name + "ActionPointsCost", components);
            movePointsCost = new StatComponents(name + "MovePointsCost", components);
            healthCost = new StatComponents(name + "HealthCost", components);
            cooldown = new StatComponents(name + "Cooldown", components);

            targeting = new TargetingComponents(name + "Targeting", components);
            effect = new EffectComponents(name + "Effect", components);
        }

        public static class TargetingComponents {

            public final ComponentMeta manhattanRange;
            public final ComponentMeta requiresSight;
            public final ComponentMeta requiresPath;
            public final ComponentMeta self;
            public final ComponentMeta ally;
            public final ComponentMeta enemy;
            public final ComponentMeta empty;

            public TargetingComponents(String name, List<ComponentMeta> components) {
                manhattanRange = create(name + "ManhattanRange", components);
                requiresSight = create(name + "RequiresSight", components);
                requiresPath = create(name + "RequiresPath", components);
                self = create(name + "Self", components);
                ally = create(name + "Ally", components);
                enemy = create(name + "Enemy", components);
                empty = create(name + "Empty", components);
            }

        }

        public static class EffectComponents {

            public final ComponentMeta passTurn;
            public final ComponentMeta walkToTarget;
            public final ElementComponents damageToTarget;

            public EffectComponents(String name, List<ComponentMeta> components) {
                passTurn = create("EndTurn" + name, components);
                walkToTarget = create("WalkToTarget" + name, components);
                damageToTarget = new ElementComponents("DamageToTarget" + name, components);
            }

        }

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

    public static class ElementComponents {

        public final ComponentMeta fire;
        public final ComponentMeta water;
        public final ComponentMeta earth;
        public final ComponentMeta air;

        public ElementComponents(String name, List<ComponentMeta> components) {
            fire = create("Fire" + name, components);
            water = create("Water" + name, components);
            earth = create("Earth" + name, components);
            air = create("Air" + name, components);
        }
    }
}
