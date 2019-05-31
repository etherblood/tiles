package com.etherblood.mods.core.game.systems.pass;

import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PassTurnOfActorSystem implements  GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(PassTurnOfActorSystem.class);
    private final CoreComponents core;

    public PassTurnOfActorSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int effect : core.effect.passTurnOfActor.query().list()) {
            int actor = core.effect.ofActor.get(effect);
            core.actor.active.remove(actor);
            LOG.info("Passed turn of actor #{}.", actor);
        }
    }

}
