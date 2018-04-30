package com.etherblood.rules.stats.health;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class UpdateBuffedHealthHandler implements Consumer<UpdateBuffedHealthEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap baseHealthKey, additiveHealthKey, buffedHealthKey, buffOnKey;

    public UpdateBuffedHealthHandler(Logger log, EventQueue events, SimpleComponentMap baseHealthKey, SimpleComponentMap additiveHealthKey, SimpleComponentMap buffedHealthKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.baseHealthKey = baseHealthKey;
        this.additiveHealthKey = additiveHealthKey;
        this.buffedHealthKey = buffedHealthKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(UpdateBuffedHealthEvent event) {
        int buffedHealth = baseHealthKey.getOrElse(event.target, 0);
        log.debug("updating buffedHealth of {}, baseHealth is {}", event.target, buffedHealth);
        for (int buff : additiveHealthKey.entities(x -> buffOnKey.hasValue(x, event.target))) {
            int additiveHealth = additiveHealthKey.get(buff);
            log.debug("adding {} from additiveHealth-buff {}", additiveHealthKey, buff);
            buffedHealth += additiveHealth;
        }
        log.info("setting buffed health of {} to {}", event.target, buffedHealth);
        buffedHealthKey.set(event.target, buffedHealth);
        event.health = buffedHealth;
    }

}
