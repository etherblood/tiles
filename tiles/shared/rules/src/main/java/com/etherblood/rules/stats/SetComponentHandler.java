package com.etherblood.rules.stats;

import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.rules.GameEventHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetComponentHandler extends GameEventHandler implements BinaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(SetComponentHandler.class);
    private final String statName;
    private final int component;

    public SetComponentHandler(String statName, int component) {
        this.statName = statName;
        this.component = component;
    }

    @Override
    public void handle(int entity, int value) {
        data.component(component).setWithDefault(entity, value, 0);
        LOG.info("setting {} of {} to {}", statName, entity, value);
    }

}