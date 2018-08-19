package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.EntityValueEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnEndHandler.class);
    private final int setActiveTeamEvent, resetActiveActionPointsEvent, resetActiveMovePointsEvent;

    public TurnEndHandler(int resetActiveActionPointsEvent, int resetActiveMovePointsEvent, int setActiveTeamEvent) {
        this.setActiveTeamEvent = setActiveTeamEvent;
        this.resetActiveActionPointsEvent = resetActiveActionPointsEvent;
        this.resetActiveMovePointsEvent = resetActiveMovePointsEvent;
    }

    public void handle(int team) {
        LOG.info("ended turn turn of {}", team);
        IntArrayList actors = data.query(Components.MEMBER_OF).list(hasValue(Components.MEMBER_OF, team));
        LOG.info("setting active for members of team {}: {}", team, actors);
        actors.forEach(x -> {
            events.sub(new EntityEvent(resetActiveActionPointsEvent, x));
            events.sub(new EntityEvent(resetActiveMovePointsEvent, x));
        });
        events.sub(new EntityValueEvent(setActiveTeamEvent, team, 0));
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
