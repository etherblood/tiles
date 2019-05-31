package com.etherblood.mods.core.game.systems.core.condition;

import com.etherblood.core.ActionSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.core.util.Coordinates;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ManhattanRangeRequiredSystem implements ActionSystem {

    private static final Logger LOG = LoggerFactory.getLogger(ManhattanRangeRequiredSystem.class);
    private final CoreComponents core;

    public ManhattanRangeRequiredSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        for (int trigger : core.effect.triggered.query().list()) {
            int skill = core.effect.ofSkill.get(trigger);
            int actor = core.skill.ofActor.get(skill);
            OptionalInt optionalTargetPosition = core.effect.targetPosition.getOptional(trigger);
            optionalTargetPosition.ifPresent(targetPosition -> {
                OptionalInt rangeOptional = core.skill.targeting.position.manhattanRange.getOptional(skill);
                rangeOptional.ifPresent(range -> {
                    int distance = Coordinates.manhattenDistance(core.actor.position.get(actor), targetPosition);
                    if (distance > range) {
                        throw new IllegalActionException("Target position is too far away.");
                    }
                });
            });
            
            
            OptionalInt optionalTargetActor = core.effect.targetActor.getOptional(trigger);
            optionalTargetActor.ifPresent(targetActor -> {
                OptionalInt rangeOptional = core.skill.targeting.position.manhattanRange.getOptional(skill);
                rangeOptional.ifPresent(range -> {
                    int distance = Coordinates.manhattenDistance(core.actor.position.get(actor), core.actor.position.get(targetActor));
                    if (distance > range) {
                        throw new IllegalActionException("Target actor is too far away.");
                    }
                });
            });
        }
    }

}
