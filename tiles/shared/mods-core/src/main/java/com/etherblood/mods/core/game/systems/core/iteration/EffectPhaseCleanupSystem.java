package com.etherblood.mods.core.game.systems.core.iteration;

import com.etherblood.core.GameSystem;
import com.etherblood.core.components.Component;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EffectPhaseCleanupSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(EffectPhaseCleanupSystem.class);
    private final List<Component<?>> componentsToRemove;

    public EffectPhaseCleanupSystem(CoreComponents core) {
        componentsToRemove = Arrays.asList(core.effect.active,
                core.effect.ofActor,
                core.effect.ofSkill,
                core.effect.damageToTarget.air,
                core.effect.damageToTarget.earth,
                core.effect.damageToTarget.fire,
                core.effect.damageToTarget.water,
                core.effect.randomDamageToTarget.air,
                core.effect.randomDamageToTarget.earth,
                core.effect.randomDamageToTarget.fire,
                core.effect.randomDamageToTarget.water,
                core.effect.animation.attack,
                core.effect.animation.die,
                core.effect.animation.walk,
                core.effect.walkToTargetPosition,
                core.effect.passTurnOfActor,
                core.effect.targetDeath,
                core.effect.targetActor,
                core.effect.targetPosition);
    }

    @Override
    public void update() {
        for (Component<?> component : componentsToRemove) {
            component.clear();
        }
        LOG.debug("Cleaned up active effects.");
    }

}