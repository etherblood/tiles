package com.etherblood.rules.components;

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
public class SetValueHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetValueHandler.class);
    private final String name;
    private final ComponentMeta component;

    public SetValueHandler(String name, ComponentMeta component) {
        this.name = name;
        this.component = component;
    }

    public void handle(int entity, int value) {
        data.set(entity, component.id, value);
        LOG.info("setting {} of #{} to {}", name, entity, value);
    }

    @Override
    public void handle(EntityValueEvent event) {
        handle(event.entity, event.value);
    }

}
