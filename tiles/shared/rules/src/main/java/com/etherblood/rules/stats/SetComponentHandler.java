package com.etherblood.rules.stats;

import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetComponentHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetComponentHandler.class);
    private final String statName;
    private final int component;

    public SetComponentHandler(String statName, int component) {
        this.statName = statName;
        this.component = component;
    }

    public void handle(int entity, int value) {
        data.setWithDefault(entity, component, value, 0);
        LOG.info("setting {} of {} to {}", statName, entity, value);
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
