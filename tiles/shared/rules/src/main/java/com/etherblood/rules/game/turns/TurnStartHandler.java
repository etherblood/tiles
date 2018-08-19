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
public class TurnStartHandler extends AbstractGameEventHandler implements EventHandler<EntityEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(TurnStartHandler.class);

    private final int setActivePlayerEvent, setActiveTeamEvent;

    public TurnStartHandler(int setActivePlayerEvent, int setActiveTeamEvent) {
        this.setActivePlayerEvent = setActivePlayerEvent;
        this.setActiveTeamEvent = setActiveTeamEvent;
    }

    public void handle(int team) {
        events.sub(new EntityValueEvent(setActiveTeamEvent, team, 1));
        IntArrayList actors = data.query(Components.MEMBER_OF).list(hasValue(Components.MEMBER_OF, team));
        LOG.info("setting active for members of team {}: {}", team, actors);
        for (int actor : actors) {
            events.sub(new EntityValueEvent(setActivePlayerEvent, actor, 1));
        }
    }

    @Override
    public void handle(EntityEvent event) {
        handle(event.entity);
    }

}
