package com.etherblood.mods.core.game.systems;

import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.mods.core.game.components.StatComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ResetActivatedActorStatSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ResetActivatedActorStatSystem.class);
    private final CoreComponents core;
    private final StatComponents stat;

    public ResetActivatedActorStatSystem(CoreComponents core, StatComponents stat) {
        this.core = core;
        this.stat = stat;
    }

    @Override
    public void update() {
        for (int effect : core.effect.triggered.query().list(core.effect.startTurnOfTargetActor::has)) {
            int actor = core.effect.targetActor.get(effect);
            if (stat.buffed.has(actor)) {
                stat.active.set(actor, stat.buffed.get(actor));
                LOG.debug("Reset {} of #{}.", stat.active.name, actor);
            }
        }
    }
}
