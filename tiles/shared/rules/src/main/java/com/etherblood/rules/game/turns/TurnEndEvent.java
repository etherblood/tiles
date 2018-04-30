package com.etherblood.rules.game.turns;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class TurnEndEvent extends Event {

    public int team;

    public TurnEndEvent(int team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "TurnEndEvent{team=" + team + '}';
    }
}
