package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.ComponentRegistry;

public class ElementStatComponents extends ComponentsBase {

    public final StatComponents fire;
    public final StatComponents water;
    public final StatComponents earth;
    public final StatComponents air;

    public ElementStatComponents(String name, ComponentRegistry registry) {
        fire = new StatComponents("Fire" + name, registry);
        water = new StatComponents("Water" + name, registry);
        earth = new StatComponents("Earth" + name, registry);
        air = new StatComponents("Air" + name, registry);
    }
}
