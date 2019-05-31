package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;

public class ElementComponents extends ComponentsBase {

    public final IntComponent fire;
    public final IntComponent water;
    public final IntComponent earth;
    public final IntComponent air;

    public ElementComponents(String name, ComponentRegistry registry) {
        fire = newIntComponent("Fire" + name, registry);
        water = newIntComponent("Water" + name, registry);
        earth = newIntComponent("Earth" + name, registry);
        air = newIntComponent("Air" + name, registry);
    }
}
