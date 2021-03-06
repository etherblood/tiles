package com.etherblood.mods.core.game;

import com.etherblood.collections.IntList;
import com.etherblood.mods.core.game.components.CoreComponents;
import com.etherblood.core.Action;
import com.etherblood.core.ActionController;
import com.etherblood.core.ActionGenerator;
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
    private final ActionGenerator actionGenerator;

    public TurnsGameController(List<GameSystem> systems, EntityFactory entityFactory, CoreComponents core, List<ActionSystem> actionSystems, ActionGenerator actionGenerator) {
        this.systems = systems;
        this.entityFactory = entityFactory;
        this.core = core;
        this.actionSystems = actionSystems;
        this.actionGenerator = actionGenerator;
    }

    @Override
    public void useAction(int player, Action action) {
        LOG.info("Player {} requested action {}.", player, action);
        if (!actionGenerator.isValidAction(action)) {
            throw new IllegalActionException("Action " + action + " is invalid.");
        }
        int skill = action.getTargetSkill();
        int actor = core.skill.ofActor.get(skill);
        int effect = entityFactory.create();
        core.effect.triggered.set(effect);
        core.effect.ofSkill.set(effect, skill);
        core.effect.ofActor.set(effect, actor);
        if (action.getTargetPosition() != null) {
            core.effect.targetPosition.set(effect, action.getTargetPosition());
            IntList list = core.actor.position.query().list(x -> core.actor.position.hasValue(x, action.getTargetPosition()));
            if (list.size() == 1) {
                core.effect.targetActor.set(effect, list.get(0));
//            } else if (!core.skill.targeting.actor.none.has(skill)) {
//                throw new IllegalActionException("Action does not have the required target actor.");
            }
//        } else if (core.skill.targeting.position.required.has(skill)) {
//            throw new IllegalActionException("Action does not have the required target position.");
        }
        validateSkillUsagePermitted(player, actor);
        for (ActionSystem actionSystem : actionSystems) {
            actionSystem.update();
        }
        runSystemIterations();
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
