package com.etherblood.mods.core.game.systems.core.cost;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MovePointsCostSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(MovePointsCostSystem.class);
    private final CoreComponents core;

    public MovePointsCostSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(trigger).ifPresent(skill -> {
                OptionalInt moveCost = core.skill.cost.movePoints.getOptional(skill);
                moveCost.ifPresent(cost -> {
                    int actor = core.skill.ofActor.get(skill);
                    int movePoints = core.stats.movePoints.active.getOptional(actor).orElse(0);
                    if (cost > movePoints) {
                        throw new IllegalActionException("Not enough move-points.");
                    }
                    int remaining = movePoints - cost;
                    core.stats.movePoints.active.set(actor, remaining);
                    LOG.info("#{} used {} move-points, {} remaining.", actor, cost, remaining);
                });
            });
        }
    }

}
