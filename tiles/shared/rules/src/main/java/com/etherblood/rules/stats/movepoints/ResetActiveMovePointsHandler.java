package com.etherblood.rules.stats.movepoints;

import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class ResetActiveMovePointsHandler implements Consumer<ResetActiveMovePointsEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap buffedMovePointsKey;

    public ResetActiveMovePointsHandler(Logger log, EventQueue events, SimpleComponentMap buffedMovePointsKey) {
        this.log = log;
        this.events = events;
        this.buffedMovePointsKey = buffedMovePointsKey;
    }

    @Override
    public void accept(ResetActiveMovePointsEvent event) {
        log.info("resetting movePoints of {}", event.target);
        events.response(new SetActiveMovePointsEvent(event.target, buffedMovePointsKey.getOrElse(event.target, 0)));
    }

}
