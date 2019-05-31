package com.etherblood.mods.core.game.systems.core.condition;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EmptyTargetRequiredSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(EmptyTargetRequiredSystem.class);
    private final CoreComponents core;

    public EmptyTargetRequiredSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            OptionalInt optionalTargetPosition = core.effect.targetPosition.getOptional(trigger);
            optionalTargetPosition.ifPresent(targetPosition -> {
                int skill = core.effect.ofSkill.get(trigger);
                if (core.skill.targeting.position.empty.has(skill) && core.actor.position.query().exists(actor -> core.actor.position.get(actor) == targetPosition)) {
                    throw new IllegalActionException("Target position is not empty.");
                }
            });
        }
    }

}
