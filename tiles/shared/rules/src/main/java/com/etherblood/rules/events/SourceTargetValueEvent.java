package com.etherblood.rules.events;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class SourceTargetValueEvent extends Event {

    public final int source, target, value;

    public SourceTargetValueEvent(int id, int source, int target, int value) {
        super(id);
        this.source = source;
        this.target = target;
        this.value = value;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + this.source;
        hash = 37 * hash + this.target;
        hash = 37 * hash + this.value;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SourceTargetValueEvent)) {
            return false;
        }
        SourceTargetValueEvent other = (SourceTargetValueEvent) obj;
        return this.source == other.source && this.target == other.target && this.value == other.value;
    }

    @Override
    public String toString() {
        return "SourceTargetValueEvent{" + "source=" + source + ", target=" + target + ", value=" + value + '}';
    }

}
