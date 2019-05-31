package com.etherblood.mods.core.game.systems.core.cost;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CooldownCostSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(CooldownCostSystem.class);
    private final CoreComponents core;

    public CooldownCostSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(trigger).ifPresent(skill -> {
                if (core.skill.cooldown.active.has(skill)) {
                    throw new IllegalActionException("Can't use skill on cooldown.");
                }
                OptionalInt cooldownCost = core.skill.cooldown.base.getOptional(skill);
                cooldownCost.ifPresent(cost -> {
                    core.skill.cooldown.active.set(skill, cost);
                    LOG.info("Set active cooldown of #{} to {}.", skill, cost);
                });
            });
        }
    }

}
