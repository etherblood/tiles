package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class EntityMoveEventMeta extends AbstractEventMeta<EntityMoveEvent> {

    public EntityMoveEventMeta(int id, String name) {
        super(id, name);
    }

    public EntityMoveEvent create(int entity, int from, int to) {
        return new EntityMoveEvent(id(), entity, from, to);
    }

}
