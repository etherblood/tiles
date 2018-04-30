package com.etherblood.rules.movement;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetPositionHandler implements Consumer<SetPositionEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap positionKey;

    public SetPositionHandler(Logger log, EventQueue events, SimpleComponentMap positionKey) {
        this.log = log;
        this.events = events;
        this.positionKey = positionKey;
    }

    @Override
    public void accept(SetPositionEvent event) {
        log.info("setting position of {} to {}", event.target, Coordinates.toString(event.position));
        positionKey.set(event.target, event.position);
    }

}
