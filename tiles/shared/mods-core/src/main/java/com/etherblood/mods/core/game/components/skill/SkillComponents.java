package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;
import com.etherblood.mods.core.game.components.StatComponents;

public class SkillComponents extends ComponentsBase {

    public final IntComponent ofActor;
    public final IntComponent id;
    public final StatComponents cooldown;

    public final TargetingComponents targeting;
    public final SkillEffectComponents effect;
    public final CostComponents cost;

    public SkillComponents(String name, ComponentRegistry registry) {
        ofActor = newIntComponent(name + "OfActor", registry);
        id = newIntComponent(name + "Id", registry);
        cooldown = new StatComponents(name + "Cooldown", registry);

        targeting = new TargetingComponents(name + "Targeting", registry);
        effect = new SkillEffectComponents(name + "Effect", registry);
        cost = new CostComponents(name + "Cost", registry);
    }
}
