package com.etherblood.rules.abilities.walk;

import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.movement.SetPositionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class WalkHandler extends GameEventHandler<WalkAction> {

    private static final Logger LOG = LoggerFactory.getLogger(WalkHandler.class);

    @Override
    public void handle(WalkAction event) {
        assert data.component(Components.ACTIVE_TURN).has(event.actor);
        int movePoints = data.component(Components.Stats.MovePoints.ACTIVE).getOrElse(event.actor, 0);
        assert movePoints >= 1;
        LOG.info("used 1 mp of {}", event.actor);
        data.component(Components.Stats.MovePoints.ACTIVE).set(event.actor, movePoints - 1);
        events.response(new SetPositionEvent(event.actor, event.to));
    }

}
