package com.etherblood.mods.core.game.systems.attack;

import com.etherblood.core.GameSystem;
import com.etherblood.core.random.HistoryRandom;
import com.etherblood.mods.core.game.components.CoreComponents;
import java.util.OptionalInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarthDamageToTargetSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(EarthDamageToTargetSystem.class);
    private final CoreComponents core;
    private final HistoryRandom random;
    private final DamageCalculator damageCalculator = new DamageCalculator();

    public EarthDamageToTargetSystem(CoreComponents core, HistoryRandom random) {
        this.core = core;
        this.random = random;
    }

    @Override
    public void update() {
        for (int effect : core.effect.damageToTarget.earth.query().list(x -> core.effect.targetActor.has(x))) {
            int targetActor = core.effect.targetActor.get(effect);
            int earthDamage = core.effect.damageToTarget.earth.get(effect);
            OptionalInt randomDamage = core.effect.randomDamageToTarget.earth.getOptional(effect);
            if (randomDamage.isPresent()) {
                earthDamage += random.next(randomDamage.getAsInt());
            }
            int power = core.stats.power.earth.active.getOptional(targetActor).orElse(0);
            int toughness = core.stats.toughness.earth.active.getOptional(targetActor).orElse(0);

            int calculatedDamage = damageCalculator.calculateFinalDamage(earthDamage, power, toughness);

            int health = core.stats.health.active.getOptional(targetActor).orElse(0);
            int newHealth = health - calculatedDamage;
            core.stats.health.active.set(targetActor, newHealth);
            LOG.info("Effect #{} dealt {} earth damage to #{} .", effect, calculatedDamage, targetActor);
        }
    }

}
