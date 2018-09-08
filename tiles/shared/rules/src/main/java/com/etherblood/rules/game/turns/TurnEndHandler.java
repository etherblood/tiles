package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.EntityEventMeta;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnEndHandler.class);
    private final EntityEventMeta resetActiveActionPointsEvent, resetActiveMovePointsEvent;
    private final EntityValueEventMeta setActiveTeamEvent;
    private final ComponentMeta memberOf;

    public TurnEndHandler(EntityEventMeta resetActiveActionPointsEvent, EntityEventMeta resetActiveMovePointsEvent, EntityValueEventMeta setActiveTeamEvent, ComponentMeta memberOf) {
        this.setActiveTeamEvent = setActiveTeamEvent;
        this.resetActiveActionPointsEvent = resetActiveActionPointsEvent;
        this.resetActiveMovePointsEvent = resetActiveMovePointsEvent;
        this.memberOf = memberOf;
    }

    public void handle(int team) {
        LOG.info("ended turn turn of #{}", team);
        IntArrayList actors = data.query(memberOf.id).list(hasValue(memberOf.id, team));
        LOG.info("setting active for members of team #{}: {}", team, actors);
        for (int actor : actors) {
            events.fire(resetActiveActionPointsEvent.create(actor));
            events.fire(resetActiveMovePointsEvent.create(actor));
        }
        events.fire(setActiveTeamEvent.create(team, 0));
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
