package com.etherblood.rules.abilities.walk;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityCoordinatesEventMeta;
import com.etherblood.rules.events.EntityMoveEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import com.etherblood.rules.movement.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class WalkHandler extends AbstractGameEventHandler implements EventHandler<EntityMoveEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(WalkHandler.class);
    private final EntityCoordinatesEventMeta setPosition;
    private final ComponentMeta movePointsComponent;

    public WalkHandler(EntityCoordinatesEventMeta setPosition, ComponentMeta movePoints) {
        this.setPosition = setPosition;
        this.movePointsComponent = movePoints;
    }

    public void handle(int actor, int from, int to) {
        assert Coordinates.manhattenDistance(from, to) == 1;
        int movePoints = data.getOptional(actor, movePointsComponent.id).orElse(0);
        assert movePoints >= 1;
        LOG.info("used 1 mp of {}", actor);
        data.set(actor, movePointsComponent.id, movePoints - 1);
        events.fire(setPosition.create(actor, to));
    }

    @Override
    public void handle(EntityMoveEvent event) {
        handle(event.entity, event.from, event.to);
    }

}
