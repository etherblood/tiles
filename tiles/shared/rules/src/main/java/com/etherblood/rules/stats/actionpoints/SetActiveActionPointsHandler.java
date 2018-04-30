package com.etherblood.rules.stats.actionpoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class SetActiveActionPointsHandler implements Consumer<SetActiveActionPointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap activeActionPointsKey;

    public SetActiveActionPointsHandler(Logger log, EventQueue events, SimpleComponentMap activeActionPointsKey) {
        this.log = log;
        this.events = events;
        this.activeActionPointsKey = activeActionPointsKey;
    }

    @Override
    public void accept(SetActiveActionPointsEvent event) {
        activeActionPointsKey.set(event.target, event.actionPoints);
        log.info("setting actionPoints of {} to {}", event.target, event.actionPoints);
    }

}
