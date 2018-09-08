package com.etherblood.rules.stats;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetStatHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetStatHandler.class);
    private final String statName;
    private final ComponentMeta component;

    public SetStatHandler(String statName, ComponentMeta component) {
        this.statName = statName;
        this.component = component;
    }

    public void handle(int entity, int value) {
        if (value == 0) {
            data.remove(entity, component.id);
            LOG.info("removed {} of #{}", statName, entity, value);
        } else {
            data.set(entity, component.id, value);
            LOG.info("set {} of #{} to {}", statName, entity, value);
        }
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
