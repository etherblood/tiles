package com.etherblood.rules.movement;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityCoordinatesEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class SetCoordinatesHandler extends AbstractGameEventHandler implements EventHandler<EntityCoordinatesEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(SetCoordinatesHandler.class);
    private final String name;
    private final ComponentMeta component;

    public SetCoordinatesHandler(String name, ComponentMeta component) {
        this.name = name;
        this.component = component;
    }

    public void handle(int entity, int coordinates) {
        data.set(entity, component.id, coordinates);
        LOG.info("setting {} of #{} to {}", name, entity, Coordinates.toString(coordinates));
    }

    @Override
    public void handle(EntityCoordinatesEvent event) {
        handle(event.entity, event.coordinates);
    }

}
