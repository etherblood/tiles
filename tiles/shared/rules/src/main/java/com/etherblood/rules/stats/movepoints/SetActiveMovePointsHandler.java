package com.etherblood.rules.stats.movepoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetActiveMovePointsHandler implements Consumer<SetActiveMovePointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeMovePointsKey;

    public SetActiveMovePointsHandler(Logger log, EventQueue events, SimpleComponentMap activeMovePointsKey) {
        this.log = log;
        this.events = events;
        this.activeMovePointsKey = activeMovePointsKey;
    }

    @Override
    public void accept(SetActiveMovePointsEvent event) {
        activeMovePointsKey.set(event.target, event.movePoints);
        log.info("setting movePoints of {} to {}", event.target, event.movePoints);
    }

}
