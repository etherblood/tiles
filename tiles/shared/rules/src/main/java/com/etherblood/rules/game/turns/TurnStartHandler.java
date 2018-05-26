package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnStartHandler extends GameEventHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TurnStartHandler.class);

    private final int setActiveEvent;

    public TurnStartHandler(int setActiveEvent) {
        this.setActiveEvent = setActiveEvent;
    }

    public void handle(int team) {
        IntArrayList actors = data.query(Components.MEMBER_OF).list(hasValue(Components.MEMBER_OF, team));
        LOG.info("setting active for members of team {}: {}", team, actors);
        actors.forEach(x -> events.response(setActiveEvent, x, 1));
    }

}
