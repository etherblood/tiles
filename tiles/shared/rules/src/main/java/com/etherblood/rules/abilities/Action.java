package com.etherblood.rules.abilities;

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
}
