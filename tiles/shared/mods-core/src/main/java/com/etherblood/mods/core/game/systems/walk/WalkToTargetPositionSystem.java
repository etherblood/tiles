package com.etherblood.mods.core.game.systems.walk;

import com.etherblood.core.GameSystem;
import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WalkToTargetPositionSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(WalkToTargetPositionSystem.class);
    private final CoreComponents core;

    public WalkToTargetPositionSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int effect : core.effect.walkToTargetPosition.query().list(x -> core.effect.ofActor.has(x) && core.effect.targetPosition.has(x))) {
            int actor = core.effect.ofActor.get(effect);
            int targetPosition = core.effect.targetPosition.get(effect);
            core.actor.position.set(actor, targetPosition);
            LOG.info("Actor #{} walked to {}.", actor, Coordinates.toString(targetPosition));
        }
    }

}
