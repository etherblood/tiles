package com.etherblood.rules.battle;

import com.etherblood.events.EventQueue;
import com.etherblood.rules.context.TypeComponentMaps;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ApplyDamageTypeHandler implements Consumer<DamageEvent> {

    private final Logger log;
    private final EventQueue events;
    private final TypeComponentMaps types;

    public ApplyDamageTypeHandler(Logger log, EventQueue events, TypeComponentMaps types) {
        this.log = log;
        this.events = events;
        this.types = types;
    }

    @Override
    public void accept(DamageEvent event) {
        log.debug("applying types to {}", event);
        int power = types.get(event.type).power.active.getOrElse(event.attacker, 0);
        int toughness = types.get(event.type).toughness.active.getOrElse(event.defender, 0);
        float effectiveDamage = event.damage * (100 + power) / 100f;
        float multiplier;
        if (toughness >= 0) {
            multiplier = 100f / (100f + toughness);
        } else {
            multiplier = 2f - (100f / (100f - toughness));
        }

        event.damage = (int) (effectiveDamage * multiplier);
        log.debug("adjusted event is {}", event);
    }

}
