package com.etherblood.rules.abilities.walk;

import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.BinaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class WalkHandler extends GameEventHandler implements BinaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(WalkHandler.class);
    private final EventDefinition setPosition;

    public WalkHandler(EventDefinition setPosition) {
        this.setPosition = setPosition;
    }

    @Override
    public void handle(int actor, int to) {
        assert data.component(Components.ACTIVE_TURN).has(actor);
        int movePoints = data.component(Components.Stats.MovePoints.ACTIVE).getOrElse(actor, 0);
        assert movePoints >= 1;
        LOG.info("used 1 mp of {}", actor);
        data.component(Components.Stats.MovePoints.ACTIVE).set(actor, movePoints - 1);
        events.response(setPosition.id(), actor, to);
    }

}
