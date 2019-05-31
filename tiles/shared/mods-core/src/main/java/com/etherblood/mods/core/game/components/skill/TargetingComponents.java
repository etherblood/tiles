package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class TargetingComponents extends ComponentsBase {

    public final ActorTargetingComponents actor;
    public final PositionTargetingComponents position;

    public TargetingComponents(String name, ComponentRegistry registry) {
        actor = new ActorTargetingComponents(name + "Actor", registry);
        position = new PositionTargetingComponents(name + "Position", registry);
    }

}
