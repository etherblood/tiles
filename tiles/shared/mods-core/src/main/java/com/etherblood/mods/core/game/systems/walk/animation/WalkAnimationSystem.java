package com.etherblood.mods.core.game.systems.walk.animation;

import com.etherblood.core.AnimationsController;
import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalkAnimationSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(WalkAnimationSystem.class);
    private final CoreComponents core;
    private final AnimationsController animations;

    public WalkAnimationSystem(CoreComponents core, AnimationsController animations) {
        this.core = core;
        this.animations = animations;
    }

    @Override
    public void update() {
        if (animations.isEnabled()) {
            for (int effect : core.effect.active.query().list(core.effect.animation.walk::has)) {
                int actor = core.effect.ofActor.get(effect);
                animations.enqueue(new WalkAnimation(actor, core.actor.position.get(actor), core.effect.targetPosition.get(effect)));
            }
        }
    }
}
