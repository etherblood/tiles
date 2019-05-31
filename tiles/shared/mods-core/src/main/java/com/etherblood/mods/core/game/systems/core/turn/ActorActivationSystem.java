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
        for (int actor : core.actor.activate.query().list()) {
            core.actor.active.set(actor);
            core.actor.activate.remove(actor);
            LOG.info("Started turn of actor #{}", actor);
        }
    }

}
