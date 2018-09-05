package com.etherblood.rules.events;

import com.etherblood.events.AbstractEventMeta;

/**
 *
 * @author Philipp
 */
public class EntityValueEventMeta extends AbstractEventMeta<EntityValueEvent> {

    public EntityValueEventMeta(int id, String name) {
        super(id, name);
    }

    public EntityValueEvent create(int entity, int value) {
        return new EntityValueEvent(id(), entity, value);
    }

}
