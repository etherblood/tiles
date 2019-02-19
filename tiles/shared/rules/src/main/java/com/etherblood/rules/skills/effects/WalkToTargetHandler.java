package com.etherblood.rules.skills.effects;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.rules.skills.*;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityMoveEventMeta;
import com.etherblood.rules.events.EntityValueEvent;
import com.etherblood.rules.util.Coordinates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class WalkToTargetHandler extends AbstractGameEventHandler implements EventHandler<EntityValueEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(WalkToTargetHandler.class);

    private final ComponentMeta skillOwner, coordinates;
    private final EntityMoveEventMeta walkEvent;

    public WalkToTargetHandler(ComponentMeta skillOwner, ComponentMeta coordinates, EntityMoveEventMeta walkEvent) {
        this.skillOwner = skillOwner;
        this.coordinates = coordinates;
        this.walkEvent = walkEvent;
    }

    @Override
    public void handle(EntityValueEvent event) {
        int skill = event.entity;
        int actor = data.get(skill, skillOwner.id);
        int from = data.get(actor, coordinates.id);
        int to = event.value;
        if(Coordinates.manhattenDistance(from, to) != 1) {
            throw new IllegalStateException();
        }
        LOG.info("#{} walks from {} to {}.", actor, Coordinates.toString(from), Coordinates.toString(to));
        events.fire(walkEvent.create(actor, from, to));
    }

}
