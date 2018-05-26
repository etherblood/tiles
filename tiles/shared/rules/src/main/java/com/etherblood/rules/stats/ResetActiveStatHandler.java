package com.etherblood.rules.stats;

import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ResetActiveStatHandler extends GameEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResetActiveStatHandler.class);

    private final String statName;
    private final int buffed;
    private final int setActiveSupply;

    public ResetActiveStatHandler(String statName, int buffed, int setActiveEvent) {
        this.statName = statName;
        this.buffed = buffed;
        this.setActiveSupply = setActiveEvent;
    }

    public void handle(int entity) {
        LOG.info("resetting {} of {}", statName, entity);
        events.response(setActiveSupply, entity, data.getOptional(entity, buffed).orElse(0));
    }
}
