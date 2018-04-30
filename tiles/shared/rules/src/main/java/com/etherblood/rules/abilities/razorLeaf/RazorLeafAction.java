package com.etherblood.rules.abilities.razorLeaf;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class RazorLeafAction extends Event {

    public int actor, target;

    public RazorLeafAction(int actor, int target) {
        this.actor = actor;
        this.target = target;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{actor=" + actor + ", target=" + target + '}';
    }
    
    public static int cost(int level) {
        return 3;
    }
    
    public static int attack(int level) {
        return level;
    }
}
