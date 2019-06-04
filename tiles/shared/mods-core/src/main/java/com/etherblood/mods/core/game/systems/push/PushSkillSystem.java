package com.etherblood.mods.core.game.systems.push;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushSkillSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(PushSkillSystem.class);
    private final CoreComponents core;

    public PushSkillSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(effect).ifPresent(skill -> {
                if (core.skill.effect.push.pushTargetActor.has(skill)) {
                    core.effect.push.amount.set(effect, core.skill.effect.push.strength.getOptional(skill).orElse(1));
                    core.effect.push.collisionDamage.set(effect, 1);
                    int direction = Coordinates.direction(core.actor.position.get(core.skill.ofActor.get(skill)), core.effect.targetPosition.get(effect));
                    core.effect.push.direction.set(effect, direction);
                    LOG.debug("Added push to effect #{}.", effect);
                }
            });
        }
    }

}
