package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.ComponentRegistry;

public class StatsComponents extends ComponentsBase {

    public final StatComponents health;
    public final StatComponents actionPoints;
    public final StatComponents movePoints;
    public final ElementStatComponents power;
    public final ElementStatComponents toughness;

    public StatsComponents(String name, ComponentRegistry registry) {
        health = new StatComponents(name + "Health", registry);
        actionPoints = new StatComponents(name + "ActionPoints", registry);
        movePoints = new StatComponents(name + "MovePoints", registry);
        power = new ElementStatComponents(name + "Power", registry);
        toughness = new ElementStatComponents(name + "Toughness", registry);
    }

}
