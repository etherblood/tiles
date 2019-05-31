package com.etherblood.mods.core.game;

import com.etherblood.collections.IntList;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.core.Action;
import com.etherblood.core.ActionController;
import com.etherblood.core.ActionSystem;
import com.etherblood.core.EntityFactory;
import com.etherblood.core.GameSystem;
import com.etherblood.core.IllegalActionException;
import com.etherblood.core.util.Flags;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TurnsGameController implements ActionController {

    private final Logger LOG = LoggerFactory.getLogger(TurnsGameController.class);
    private final List<ActionSystem> actionSystems;
    private final List<GameSystem> systems;
    private final EntityFactory entityFactory;
    private final CoreComponents core;

    public TurnsGameController(List<GameSystem> systems, EntityFactory entityFactory, CoreComponents core, List<ActionSystem> actionSystems) {
        this.systems = systems;
        this.entityFactory = entityFactory;
        this.core = core;
        this.actionSystems = actionSystems;
    }

    @Override
    public void useAction(int player, Action action) {
        LOG.info("Player {} requested action {}.", player, action);
        int skill = action.getTargetSkill();
        int actor = core.skill.ofActor.get(skill);
        int trigger = createTrigger(skill);
        core.effect.ofActor.set(trigger, actor);
        Integer targetActor = null;
        if (action.getTargetPosition() != null) {
            core.effect.targetPosition.set(trigger, action.getTargetPosition());
            IntList list = core.actor.position.query().list(x -> core.actor.position.hasValue(x, action.getTargetPosition()));
            if(list.size() == 1) {
                targetActor = list.get(0);
            }
        } else if (core.skill.targeting.position.required.has(skill)) {
            throw new IllegalActionException("Action does not have the required target position.");
        }
        if (targetActor != null) {
            core.effect.targetActor.set(trigger, targetActor);
        } else if (core.skill.targeting.actor.required.has(skill)) {
            throw new IllegalActionException("Action does not have the required target actor.");
        }
        validateSkillUsagePermitted(player, actor);
        for (ActionSystem actionSystem : actionSystems) {
            actionSystem.update();
        }
        runSystemIterations();
    }

    private int createTrigger(int skill) {
        int trigger = entityFactory.create();
        core.effect.triggered.set(trigger);
        core.effect.ofSkill.set(trigger, skill);
        return trigger;
    }

    private void validateSkillUsagePermitted(int player, int actor) {
        int controlFlags = core.actor.controlledBy.get(actor);
        if (!Flags.containsIndex(controlFlags, player)) {
            throw new IllegalActionException("Player tried to trigger a skill they don't control.");
        }
        if (!core.actor.active.has(actor)) {
            throw new IllegalActionException("Player tried to trigger a skill of an inactive actor.");
        }
    }

    private void runSystemIterations() {
        int iteration = 0;
        while (core.effect.triggered.query().exists()) {
            LOG.debug("Update iteration {} start.", iteration);
            for (GameSystem system : systems) {
                system.update();
            }
            LOG.debug("Update iteration {} end.", iteration);
            iteration++;
        }
    }
}
