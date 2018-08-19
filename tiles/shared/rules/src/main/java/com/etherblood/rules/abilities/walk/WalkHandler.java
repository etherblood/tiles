package com.etherblood.rules.abilities.walk;

import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityMoveEvent;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.movement.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class WalkHandler extends AbstractGameEventHandler implements EventHandler<EntityMoveEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(WalkHandler.class);
    private final EventDefinition setPosition;

    public WalkHandler(EventDefinition setPosition) {
        this.setPosition = setPosition;
    }

    public void handle(int actor, int from, int to) {
        assert data.has(actor, Components.ACTIVE_PLAYER);
        assert Coordinates.manhattenDistance(from, to) == 1;
        int movePoints = data.getOptional(actor, Components.Stats.MovePoints.ACTIVE).orElse(0);
        assert movePoints >= 1;
        LOG.info("used 1 mp of {}", actor);
        data.set(actor, Components.Stats.MovePoints.ACTIVE, movePoints - 1);
        events.response(new EntityValueEvent(setPosition.id(), actor, to));
    }

    @Override
    public void handle(EntityMoveEvent event) {
        handle(event.entity, event.from, event.to);
    }

}
