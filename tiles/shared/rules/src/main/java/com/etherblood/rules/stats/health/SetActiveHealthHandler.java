package com.etherblood.rules.stats.health;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetActiveHealthHandler implements Consumer<SetActiveHealthEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeHealthKey;

    public SetActiveHealthHandler(Logger log, EventQueue events, SimpleComponentMap activeHealthKey) {
        this.log = log;
        this.events = events;
        this.activeHealthKey = activeHealthKey;
    }

    @Override
    public void accept(SetActiveHealthEvent event) {
        activeHealthKey.set(event.target, event.health);
        log.info("setting health of {} to {}", event.target, event.health);
    }

}
