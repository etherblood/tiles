package com.etherblood.mods.core.game.systems.attack;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.AnimationsController;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackSkillTriggerSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(AttackSkillTriggerSystem.class);
    private final CoreComponents core;
    private final AnimationsController animations;

    public AttackSkillTriggerSystem(CoreComponents core, AnimationsController animations) {
        this.core = core;
        this.animations = animations;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list()) {
            int skill = core.effect.ofSkill.get(effect);
            OptionalInt earthDamage = core.skill.effect.damageToTarget.earth.getOptional(skill);
            earthDamage.ifPresent(damage -> {
                core.effect.damageToTarget.earth.set(effect, damage);
            });
            OptionalInt randomEarthDamage = core.skill.effect.randomDamageToTarget.earth.getOptional(skill);
            randomEarthDamage.ifPresent(randomDamage -> {
                core.effect.randomDamageToTarget.earth.set(effect, randomDamage);
            });
            if (animations.isEnabled() && core.skill.effect.animation.attack.has(skill)) {
                core.effect.animation.attack.set(effect);
            }
        }
    }
}
