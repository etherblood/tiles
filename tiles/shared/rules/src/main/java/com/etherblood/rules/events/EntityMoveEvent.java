package com.etherblood.rules.events;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class EntityMoveEvent extends Event {

    public int entity, from, to;

    public EntityMoveEvent(int id, int entity, int from, int to) {
        super(id);
        this.entity = entity;
        this.from = from;
        this.to = to;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.entity;
        hash = 29 * hash + this.from;
        hash = 29 * hash + this.to;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EntityMoveEvent)) {
            return false;
        }
        EntityMoveEvent other = (EntityMoveEvent) obj;
        return this.entity == other.entity && this.from == other.from && this.to == other.to;
    }

    @Override
    public String toString() {
        return EntityMoveEvent.class.getSimpleName() + "{entity=" + entity + ", from=" + from + ", to=" + to + ", id=" + id + '}';
    }
}
