package com.etherblood.mods.core.game.systems.attack.animation;

import com.etherblood.core.AnimationsController;
import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttackAnimationSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(AttackAnimationSystem.class);
    private final CoreComponents core;
    private final AnimationsController animations;

    public AttackAnimationSystem(CoreComponents core, AnimationsController animations) {
        this.core = core;
        this.animations = animations;
    }

    @Override
    public void update() {
        if (animations.isEnabled()) {
            for (int effect : core.effect.active.query().list(core.effect.animation.attack::has)) {
                int actor = core.effect.ofActor.get(effect);
                animations.enqueue(new AttackAnimation(actor, core.actor.position.get(actor), core.effect.targetPosition.get(effect)));
            }
        }
    }
}
