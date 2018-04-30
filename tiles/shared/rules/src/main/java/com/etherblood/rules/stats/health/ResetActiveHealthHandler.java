package com.etherblood.rules.stats.health;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ResetActiveHealthHandler implements Consumer<ResetActiveHealthEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap buffedHealthKey;

    public ResetActiveHealthHandler(Logger log, EventQueue events, SimpleComponentMap buffedHealthKey) {
        this.log = log;
        this.events = events;
        this.buffedHealthKey = buffedHealthKey;
    }

    @Override
    public void accept(ResetActiveHealthEvent event) {
        log.info("resetting health of {}", event.target);
        events.response(new SetActiveHealthEvent(event.target, buffedHealthKey.getOrElse(event.target, 0)));
    }

}
