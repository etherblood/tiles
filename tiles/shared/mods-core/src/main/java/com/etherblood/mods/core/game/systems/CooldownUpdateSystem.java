package com.etherblood.mods.core.game.systems;

import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CooldownUpdateSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(CooldownUpdateSystem.class);
    private final CoreComponents core;

    public CooldownUpdateSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list(core.effect.startTurnOfTargetActor::has)) {
            int actor = core.effect.targetActor.get(effect);
            for (int skill : core.skill.cooldown.active.query().list(x -> core.skill.ofActor.hasValue(x, actor))) {
                int cooldown = core.skill.cooldown.active.get(skill);
                cooldown--;
                if (cooldown <= 0) {
                    core.skill.cooldown.active.remove(skill);
                    LOG.info("Skill #{} no longer on cooldown.", skill);
                } else {
                    core.skill.cooldown.active.set(skill, cooldown);
                    LOG.info("Skill #{} cooldown {} turns remaining.", skill, cooldown);
                }
            }
        }
    }
    
}
