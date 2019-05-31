package com.etherblood.mods.core.game.systems.pass;

import com.etherblood.core.ActionSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassTurnSkillTriggerSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnSkillTriggerSystem.class);
    private final CoreComponents core;

    public PassTurnSkillTriggerSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(trigger).ifPresent(skill -> {
                if (core.skill.effect.passTurnOfActor.has(skill)) {
                    core.effect.passTurnOfActor.set(trigger);
                }
            });
        }
    }
}
