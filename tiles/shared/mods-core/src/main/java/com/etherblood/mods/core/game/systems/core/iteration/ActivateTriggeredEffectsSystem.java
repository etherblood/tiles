package com.etherblood.mods.core.game.systems.core.iteration;

import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivateTriggeredEffectsSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ActivateTriggeredEffectsSystem.class);
    private final CoreComponents core;

    public ActivateTriggeredEffectsSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            core.effect.active.set(trigger);
            core.effect.triggered.remove(trigger);
            LOG.debug("Activated triggered effect #{}.", trigger);
        }
    }

}
