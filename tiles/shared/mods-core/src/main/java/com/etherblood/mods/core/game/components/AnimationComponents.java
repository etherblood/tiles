package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class AnimationComponents extends ComponentsBase {

    public final BoolComponent walk;
    public final BoolComponent attack;
    public final BoolComponent die;

    public AnimationComponents(String name, ComponentRegistry registry) {
        walk = newBoolComponent(name + "Walk", registry);
        attack = newBoolComponent(name + "Attack", registry);
        die = newBoolComponent(name + "Die", registry);
    }
}
