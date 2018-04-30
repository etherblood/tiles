package com.etherblood.rules.game.turns;

import com.etherblood.rules.stats.movepoints.*;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.events.EventQueue;
import java.util.function.Consumer;
import org.slf4j.Logger;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler implements Consumer<TurnEndEvent> {

    private final Logger log;
    private final EventQueue events;
    private final SimpleComponentMap nextTeam;

    public TurnEndHandler(Logger log, EventQueue events, SimpleComponentMap nextTeam) {
        this.log = log;
        this.events = events;
        this.nextTeam = nextTeam;
    }

    @Override
    public void accept(TurnEndEvent event) {
        log.info("ended turn turn of {}", event.team);
        events.trigger(new TurnStartEvent(nextTeam.get(event.team)));
    }

}
