package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class EntityEventMeta extends AbstractEventMeta<EntityEvent> {

    public EntityEventMeta(int id, String name) {
        super(id, name);
    }

    public EntityEvent create(int entity) {
        return new EntityEvent(id(), entity);
    }

}
