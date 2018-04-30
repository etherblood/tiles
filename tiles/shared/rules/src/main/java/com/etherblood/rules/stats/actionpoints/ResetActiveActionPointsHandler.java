package com.etherblood.rules.stats.actionpoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ResetActiveActionPointsHandler implements Consumer<ResetActiveActionPointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap buffedActionPointsKey;

    public ResetActiveActionPointsHandler(Logger log, EventQueue events, SimpleComponentMap buffedActionPointsKey) {
        this.log = log;
        this.events = events;
        this.buffedActionPointsKey = buffedActionPointsKey;
    }

    @Override
    public void accept(ResetActiveActionPointsEvent event) {
        log.info("resetting actionPoints of {}", event.target);
        events.response(new SetActiveActionPointsEvent(event.target, buffedActionPointsKey.getOrElse(event.target, 0)));
    }

}
