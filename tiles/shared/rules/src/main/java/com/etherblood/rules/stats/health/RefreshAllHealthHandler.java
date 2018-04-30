package com.etherblood.rules.stats.health;

import com.etherblood.collections.IntSet;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class RefreshAllHealthHandler<T extends Event> implements Consumer<T> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeHealthKey, baseHealthKey, buffedHealthKey, additiveHealthKey, buffOnKey;

    public RefreshAllHealthHandler(Logger log, EventQueue events, SimpleComponentMap activeHealthKey, SimpleComponentMap baseHealthKey, SimpleComponentMap buffedHealthKey, SimpleComponentMap additiveHealthKey, SimpleComponentMap buffOnKey) {
        this.log = log;
        this.events = events;
        this.activeHealthKey = activeHealthKey;
        this.baseHealthKey = baseHealthKey;
        this.buffedHealthKey = buffedHealthKey;
        this.additiveHealthKey = additiveHealthKey;
        this.buffOnKey = buffOnKey;
    }

    @Override
    public void accept(T event) {
        IntSet healthEntities = new IntSet();
        for (int entity : baseHealthKey.entities()) {
            healthEntities.set(entity);
        }
        for (int entity : activeHealthKey.entities()) {
            healthEntities.set(entity);
        }
        for (int entity : buffedHealthKey.entities()) {
            healthEntities.set(entity);
        }
        for (int entity : additiveHealthKey.entities()) {
            healthEntities.set(buffOnKey.get(entity));
        }

        log.info("refreshing health for {}", healthEntities);

        healthEntities.foreach(healthEntity -> {
            events.trigger(new UpdateBuffedHealthEvent(healthEntity));
        });
        healthEntities.foreach(healthEntity -> {
            events.trigger(new ResetActiveHealthEvent(healthEntity));
        });
    }

}
