package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class ActorTargetingComponents extends ComponentsBase {

    public final BoolComponent self;
    public final BoolComponent ally;
    public final BoolComponent enemy;
    public final BoolComponent none;

    public ActorTargetingComponents(String name, ComponentRegistry registry) {
        self = newBoolComponent(name + "Self", registry);
        ally = newBoolComponent(name + "Ally", registry);
        enemy = newBoolComponent(name + "Enemy", registry);
        none = newBoolComponent(name + "None", registry);
    }
}
