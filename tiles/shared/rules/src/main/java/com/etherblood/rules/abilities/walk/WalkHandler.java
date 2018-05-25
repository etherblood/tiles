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
        assert data.has(actor, Components.ACTIVE_TURN);
        int movePoints = data.getOptional(actor, Components.Stats.MovePoints.ACTIVE).orElse(0);
        assert movePoints >= 1;
        LOG.info("used 1 mp of {}", actor);
        data.set(actor, Components.Stats.MovePoints.ACTIVE, movePoints - 1);
        events.response(setPosition.id(), actor, to);
    }

}
