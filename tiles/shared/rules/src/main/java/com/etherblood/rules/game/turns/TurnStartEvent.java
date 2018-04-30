package com.etherblood.rules.game.turns;

import com.etherblood.events.Event;

/**
 *
 * @author Philipp
 */
public class TurnStartEvent extends Event {

    public int team;

    public TurnStartEvent(int team) {
        this.team = team;
    }

    @Override
    public String toString() {
        return "TurnStartEvent{" + "team=" + team + '}';
    }
}
