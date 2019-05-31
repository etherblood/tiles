package com.etherblood.mods.core.game.systems.walk;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.AnimationsController;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalkSkillTriggerSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(WalkSkillTriggerSystem.class);
    private final CoreComponents core;
    private final AnimationsController animations;

    public WalkSkillTriggerSystem(CoreComponents core, AnimationsController animations) {
        this.core = core;
        this.animations = animations;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(effect).ifPresent(skill -> {
                if (core.skill.effect.walkToTargetPosition.has(skill)) {
                    core.effect.walkToTargetPosition.set(effect);
                }
                if (animations.isEnabled() && core.skill.effect.animation.walk.has(skill)) {
                    core.effect.animation.walk.set(effect);
                }
            });
        }
    }
}
