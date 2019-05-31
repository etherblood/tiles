package com.etherblood.mods.core.game.components.skill;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;
import com.etherblood.mods.core.game.components.ComponentsBase;
import com.etherblood.mods.core.game.components.ElementComponents;
import com.etherblood.mods.core.game.components.AnimationComponents;

public class SkillEffectComponents extends ComponentsBase {

    public final IntComponent targetPosition;
    public final IntComponent targetActor;

    public final AnimationComponents animation;
    public final BoolComponent passTurnOfActor;
    public final BoolComponent walkToTargetPosition;
    public final ElementComponents damageToTarget;
    public final ElementComponents randomDamageToTarget;

    public SkillEffectComponents(String name, ComponentRegistry registry) {
        targetPosition = newIntComponent(name + "TargetPosition", registry);
        targetActor = newIntComponent(name + "TargetActor", registry);
        animation = new AnimationComponents(name + "Animation", registry);
        passTurnOfActor = newBoolComponent("PassTurnOfActor" + name, registry);
        walkToTargetPosition = newBoolComponent("WalkToTarget" + name, registry);
        damageToTarget = new ElementComponents("DamageToTarget" + name, registry);
        randomDamageToTarget = new ElementComponents("RandomDamageToTarget" + name, registry);
    }
}
