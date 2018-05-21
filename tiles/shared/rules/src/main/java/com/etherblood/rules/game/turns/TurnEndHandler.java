package com.etherblood.rules.game.turns;

import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.UnaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler extends GameEventHandler implements UnaryHandler{

    private static final Logger LOG = LoggerFactory.getLogger(TurnEndHandler.class);
    private final EventDefinition turnStart;

    public TurnEndHandler(EventDefinition turnStart) {
        this.turnStart = turnStart;
    }

    @Override
    public void handle(int team) {
        LOG.info("ended turn turn of {}", team);
        events.trigger(turnStart.id(), data.component(Components.NEXT_TEAM).get(team));
    }

}
