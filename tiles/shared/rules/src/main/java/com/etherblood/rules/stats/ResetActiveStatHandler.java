package com.etherblood.rules.stats;

import com.etherblood.events.Event;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.stats.Util.IntIntFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.etherblood.rules.HasEntity;

/**
 *
 * @author Philipp
 */
public class ResetActiveStatHandler<T extends Event & HasEntity> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(ResetActiveStatHandler.class);

    private final String statName;
    private final int buffed;
    private final IntIntFunction<Event> setActiveSupply;

    public ResetActiveStatHandler(String statName, int buffed, IntIntFunction<Event> setActiveEvent) {
        this.statName = statName;
        this.buffed = buffed;
        this.setActiveSupply = setActiveEvent;
    }

    @Override
    public void handle(T event) {
        LOG.info("resetting {} of {}", statName, event.entity());
        events.response(setActiveSupply.apply(event.entity(), data.component(buffed).getOrElse(event.entity(), 0)));
    }
}
