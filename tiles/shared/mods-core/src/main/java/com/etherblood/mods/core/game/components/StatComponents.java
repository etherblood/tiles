package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;

public class StatComponents extends ComponentsBase {

    public final IntComponent base;
    public final IntComponent additive;
    public final IntComponent active;
    public final IntComponent buffed;

    public StatComponents(String statName, ComponentRegistry registry) {
        active = newIntComponent("Active" + statName, registry);
        base = newIntComponent("Base" + statName, registry);
        buffed = newIntComponent("Buffed" + statName, registry);
        additive = newIntComponent("Additive" + statName, registry);
    }

}
