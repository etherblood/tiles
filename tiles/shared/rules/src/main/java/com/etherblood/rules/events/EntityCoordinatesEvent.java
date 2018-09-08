package com.etherblood.rules.events;

import com.etherblood.events.Event;
import com.etherblood.rules.movement.Coordinates;

/**
 *
 * @author Philipp
 */
public class EntityCoordinatesEvent extends Event {

    public final int entity, coordinates;

    EntityCoordinatesEvent(int id, int target, int coordinates) {
        super(id);
        this.entity = target;
        this.coordinates = coordinates;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + this.entity;
        hash = 97 * hash + this.coordinates;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EntityCoordinatesEvent)) {
            return false;
        }
        EntityCoordinatesEvent other = (EntityCoordinatesEvent) obj;
        return this.entity == other.entity && this.coordinates == other.coordinates;
    }

    @Override
    public String toString() {
        return EntityCoordinatesEvent.class.getSimpleName() + "{entity=" + entity + ", value=" + Coordinates.toString(coordinates) + ", id=" + id + '}';
    }
}
