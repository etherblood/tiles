package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.GeneralComponent;
import com.etherblood.core.components.IntComponent;

public abstract class ComponentsBase {

    protected static IntComponent newIntComponent(String name, ComponentRegistry registry) {
        return registry.newIntComponent(name);
    }

    protected static BoolComponent newBoolComponent(String name, ComponentRegistry registry) {
        return registry.newBoolComponent(name);
    }

    protected static <T> GeneralComponent<T> newGeneralComponent(String name, ComponentRegistry registry, Class<T> clazz) {
        return registry.newGeneralComponent(name, clazz);
    }
}
