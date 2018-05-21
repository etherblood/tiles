package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnStartHandler extends GameEventHandler implements UnaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(TurnStartHandler.class);

    @Override
    public void handle(int team) {
        IntArrayList actors = data.component(Components.MEMBER_OF).entities(x -> data.component(Components.MEMBER_OF).hasValue(x, team));
        LOG.info("setting activeTurn for members of team {}: {}", team, actors);
        actors.forEach(x -> data.component(Components.ACTIVE_TURN).set(x, 0));
    }

}
