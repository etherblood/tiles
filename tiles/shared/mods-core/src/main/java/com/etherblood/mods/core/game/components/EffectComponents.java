package com.etherblood.mods.core.game.components;

import com.etherblood.core.components.BoolComponent;
import com.etherblood.core.components.ComponentRegistry;
import com.etherblood.core.components.IntComponent;

public class EffectComponents extends ComponentsBase {

    public final IntComponent ofActor;
    public final IntComponent ofSkill;
    public final BoolComponent active;
    public final BoolComponent triggered;
    public final IntComponent targetPosition;
    public final IntComponent targetActor;

    public final AnimationComponents animation;
    public final BoolComponent passTurnOfActor;
    public final BoolComponent walkToTargetPosition;
    public final BoolComponent targetDeath;
    public final ElementComponents damageToTarget;
    public final ElementComponents randomDamageToTarget;

    public EffectComponents(String name, ComponentRegistry registry) {
        ofActor = newIntComponent(name + "OfActor", registry);
        ofSkill = newIntComponent(name + "OfSkill", registry);
        active = newBoolComponent(name + "Active", registry);
        triggered = newBoolComponent(name + "Triggered", registry);
        targetPosition = newIntComponent(name + "TargetPosition", registry);
        targetActor = newIntComponent(name + "TargetActor", registry);
        animation = new AnimationComponents(name + "Animation", registry);
        passTurnOfActor = newBoolComponent("PassTurnOfActor" + name, registry);
        walkToTargetPosition = newBoolComponent("WalkToTarget" + name, registry);
        targetDeath = newBoolComponent("TargetDeath" + name, registry);
        damageToTarget = new ElementComponents("DamageToTarget" + name, registry);
        randomDamageToTarget = new ElementComponents("RandomDamageToTarget" + name, registry);
    }
}
