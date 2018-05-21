package com.etherblood.rules.stats;

import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class ResetActiveStatHandler extends GameEventHandler implements UnaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ResetActiveStatHandler.class);

    private final String statName;
    private final int buffed;
    private final int setActiveSupply;

    public ResetActiveStatHandler(String statName, int buffed, int setActiveEvent) {
        this.statName = statName;
        this.buffed = buffed;
        this.setActiveSupply = setActiveEvent;
    }

    @Override
    public void handle(int entity) {
        LOG.info("resetting {} of {}", statName, entity);
        events.response(setActiveSupply, entity, data.component(buffed).getOrElse(entity, 0));
    }
}
