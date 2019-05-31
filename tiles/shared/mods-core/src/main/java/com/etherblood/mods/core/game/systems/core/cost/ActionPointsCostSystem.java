package com.etherblood.mods.core.game.systems.core.cost;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActionPointsCostSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ActionPointsCostSystem.class);
    private final CoreComponents core;

    public ActionPointsCostSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            core.effect.ofSkill.getOptional(trigger).ifPresent(skill -> {
                OptionalInt actionCost = core.skill.cost.actionPoints.getOptional(skill);
                actionCost.ifPresent(cost -> {
                    int actor = core.skill.ofActor.get(skill);
                    int actionPoints = core.stats.actionPoints.active.getOptional(actor).orElse(0);
                    if (cost > actionPoints) {
                        throw new IllegalActionException("Not enough action-points.");
                    }
                    int remaining = actionPoints - cost;
                    core.stats.actionPoints.active.set(actor, remaining);
                    LOG.info("#{} used {} action-points, {} remaining.", actor, cost, remaining);
                });
            });
        }
    }

}
