package com.etherblood.mods.core.game.systems.death;

import com.etherblood.core.AnimationsController;
import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(DeathSystem.class);
    private final CoreComponents core;
    private final AnimationsController animations;

    public DeathSystem(CoreComponents core, AnimationsController animations) {
        this.core = core;
        this.animations = animations;
    }

    @Override
    public void update() {
        for (int effect : core.effect.active.query().list(core.effect.targetDeath::has)) {
            core.effect.targetActor.getOptional(effect).ifPresent(actor -> {
                if (core.actor.isStatusOk.has(actor)) {
                    LOG.info("#{} died.", actor);
                    core.actor.isStatusOk.remove(actor);
                    core.actor.active.remove(actor);
                    animations.enqueue(new DieAnimation(actor, core.actor.position.get(actor)));
                }
            });
        }
    }

}
