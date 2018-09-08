package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class EntityCoordinatesEventMeta extends AbstractEventMeta<EntityCoordinatesEvent> {

    public EntityCoordinatesEventMeta(int id, String name) {
        super(id, name);
    }

    public EntityCoordinatesEvent create(int entity, int coordinates) {
        return new EntityCoordinatesEvent(id(), entity, coordinates);
    }

}
