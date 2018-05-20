package com.etherblood.rules.stats;

import com.etherblood.events.Event;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.HasEntity;
import com.etherblood.rules.HasValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetComponentHandler<T extends Event & HasEntity & HasValue> extends GameEventHandler<T> {

    private static final Logger LOG = LoggerFactory.getLogger(SetComponentHandler.class);
    private final String statName;
    private final int component;

    public SetComponentHandler(String statName, int component) {
        this.statName = statName;
        this.component = component;
    }

    @Override
    public void handle(T event) {
        data.component(component).set(event.entity(), event.value());
        LOG.info("setting {} of {} to {}", statName, event.entity(), event.value());
    }

}
