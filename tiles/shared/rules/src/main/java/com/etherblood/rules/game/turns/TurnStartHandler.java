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
public class TurnStartHandler extends GameEventHandler<TurnStartEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnStartHandler.class);

    @Override
    public void handle(TurnStartEvent event) {
        IntArrayList actors = data.component(Components.MEMBER_OF).entities(x -> data.component(Components.MEMBER_OF).hasValue(x, event.team));
        LOG.info("setting activeTurn for members of team {}: {}", event.team, actors);
        actors.forEach(x -> data.component(Components.ACTIVE_TURN).set(x, 0));
    }

}
