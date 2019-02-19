package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.EntityValueEventMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnStartHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnStartHandler.class);

    private final EntityValueEventMeta setActivePlayerEvent, setActiveTeamEvent;
    private final int memberOf, alive;

    public TurnStartHandler(EntityValueEventMeta setActivePlayerEvent, EntityValueEventMeta setActiveTeamEvent, int memberOf, int alive) {
        this.setActivePlayerEvent = setActivePlayerEvent;
        this.setActiveTeamEvent = setActiveTeamEvent;
        this.memberOf = memberOf;
        this.alive = alive;
    }

    public void handle(int team) {
        events.fire(setActiveTeamEvent.create(team, 1));
        IntArrayList actors = data.query(memberOf).list(hasValue(memberOf, team).and(has(alive)));
        LOG.info("setting active for members of team #{}: {}", team, actors);
        for (int actor : actors) {
            events.fire(setActivePlayerEvent.create(actor, 1));
        }
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
