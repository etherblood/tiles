package com.etherblood.mods.core.game.systems.core.turn;

import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActorActivationSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ActorActivationSystem.class);
    private final CoreComponents core;

    public ActorActivationSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list(core.effect.startTurnOfTargetActor::has)) {
            int actor = core.effect.targetActor.get(effect);
            if (core.actor.isStatusOk.has(actor)) {
                core.actor.active.set(actor);
                LOG.info("Started turn of actor #{}", actor);
            } else {
                LOG.info("Failed to start turn of actor #{}, status is not OK.", actor);
            }
            core.effect.startTurnOfTargetActor.remove(effect);
        }
    }

}
