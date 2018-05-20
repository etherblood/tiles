package com.etherblood.rules.game.turns;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler extends GameEventHandler<TurnEndEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnEndHandler.class);

    @Override
    public void handle(TurnEndEvent event) {
        LOG.info("ended turn turn of {}", event.team);
        events.trigger(new TurnStartEvent(data.component(Components.NEXT_TEAM).get(event.team)));
    }

}
