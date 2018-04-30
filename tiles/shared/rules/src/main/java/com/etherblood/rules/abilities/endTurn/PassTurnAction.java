package com.etherblood.rules.abilities.endTurn;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class PassTurnAction extends Event {

    public int actor;

    public PassTurnAction(int actor) {
        this.actor = actor;
    }

    @Override
    public String toString() {
        return "EndTurnAction{actor=" + actor + '}';
    }
}
