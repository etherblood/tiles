package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;

public class PushSkillComponents extends ComponentsBase {

    public final BoolComponent pushTargetActor;
    public final IntComponent strength;

    public PushSkillComponents(String name, ComponentRegistry registry) {
        pushTargetActor = newBoolComponent("PushTargetActor" + name, registry);
        strength = newIntComponent("strength" + name, registry);
    }
}
