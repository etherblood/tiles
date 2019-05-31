package com.etherblood.mods.core.game.systems.death;

import com.etherblood.core.AnimationsController;
import com.etherblood.core.EntityFactory;
import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DieWithoutHealthSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(DieWithoutHealthSystem.class);
    private final CoreComponents core;
    private final EntityFactory factory;
    private final AnimationsController animations;

    public DieWithoutHealthSystem(CoreComponents core, EntityFactory factory, AnimationsController animations) {
        this.core = core;
        this.factory = factory;
        this.animations = animations;
    }

    @Override
    public void update() {
        for (int actor : core.actor.isStatusOk.query().list()) {
            int health = core.stats.health.active.getOptional(actor).orElse(0);
            if (health <= 0) {
                int trigger = factory.create();
                core.effect.triggered.set(trigger);
                core.effect.targetDeath.set(trigger);
                core.effect.targetActor.set(trigger, actor);
                if (animations.isEnabled()) {
                    core.effect.animation.die.set(trigger);
                }
            }
        }
    }

}
