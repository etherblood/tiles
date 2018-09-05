package com.etherblood.rules.events;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class EntityValueEvent extends Event {

    public int entity, value;

    EntityValueEvent(int id, int target, int value) {
        super(id);
        this.entity = target;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.entity;
        hash = 97 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityValueEvent)) {
            return false;
        }
        EntityValueEvent other = (EntityValueEvent) obj;
        return this.entity == other.entity && this.value == other.value;
    }

    @Override
    public String toString() {
        return EntityValueEvent.class.getSimpleName() + "{entity=" + entity + ", value=" + value + ", id=" + id + '}';
    }
}
