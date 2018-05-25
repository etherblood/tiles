package com.etherblood.rules.abilities;

import java.util.Arrays;

/**
 *
 * @author Philipp
 */
public class Action {

    public final int eventId;
    public final int[] eventArgs;

    public Action(int eventId, int... eventArgs) {
        this.eventId = eventId;
        this.eventArgs = eventArgs;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + this.eventId;
        hash = 79 * hash + Arrays.hashCode(this.eventArgs);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Action)) {
            return false;
        }
        Action other = (Action) obj;
        return eventId == other.eventId && Arrays.equals(this.eventArgs, other.eventArgs);
    }
}
