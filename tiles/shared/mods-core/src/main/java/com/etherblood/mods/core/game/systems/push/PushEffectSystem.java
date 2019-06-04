package com.etherblood.mods.core.game.systems.push;

import com.etherblood.core.AnimationsController;
import com.etherblood.core.EntityFactory;
import com.etherblood.core.GameSystem;
import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.mods.core.game.systems.walk.animation.PushAnimation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushEffectSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(PushEffectSystem.class);
    private final CoreComponents core;
    private final EntityFactory factory;
    private final AnimationsController animations;

    public PushEffectSystem(CoreComponents core, EntityFactory factory, AnimationsController animations) {
        this.core = core;
        this.factory = factory;
        this.animations = animations;
    }

    @Override
    public void update() {
        for (int effect : core.effect.active.query().list()) {
            core.effect.push.amount.getOptional(effect).ifPresent(amount -> {
                int targetActor = core.effect.targetActor.get(effect);
                int targetPosition = core.actor.position.get(targetActor);//core.effect.targetActor.get(effect);
                int direction = core.effect.push.direction.get(effect);
                int nextPosition = Coordinates.sum(targetPosition, direction);
                int mapSize = core.mapSize.query().uniqueValue().getAsInt();

                boolean occupied = !Coordinates.inBounds(nextPosition, mapSize) || core.actor.position.query().exists(x -> core.actor.position.hasValue(x, nextPosition));
                if (occupied) {
                    //TODO
                } else {
                    LOG.info("Pushed #{} to {}.", targetActor, Coordinates.toString(nextPosition));
                    core.actor.position.set(targetActor, nextPosition);
                    if (animations.isEnabled()) {
                        animations.enqueue(new PushAnimation(targetActor, targetPosition, nextPosition));
                    }
                }
                core.effect.push.amount.remove(effect);
                core.effect.push.collisionDamage.remove(effect);
                core.effect.push.direction.remove(effect);
                if (amount > 1) {
                    int triggered = factory.create();
                    core.effect.triggered.set(triggered);
                    core.effect.targetActor.set(triggered, targetActor);
                    core.effect.targetPosition.set(triggered, nextPosition);
                    core.effect.push.amount.set(triggered, amount - 1);
                    core.effect.push.direction.set(triggered, direction);
                }
            });
        }
    }

}
