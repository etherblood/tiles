package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.EventDefinition;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class TurnEndHandler extends GameEventHandler{

    private static final Logger LOG = LoggerFactory.getLogger(TurnEndHandler.class);
    private final int turnStartEvent, resetActiveActionPointsEvent, resetActiveMovePointsEvent;

    public TurnEndHandler(int turnStartEvent, int resetActiveActionPointsEvent, int resetActiveMovePointsEvent) {
        this.turnStartEvent = turnStartEvent;
        this.resetActiveActionPointsEvent = resetActiveActionPointsEvent;
        this.resetActiveMovePointsEvent = resetActiveMovePointsEvent;
    }

    public void handle(int team) {
        LOG.info("ended turn turn of {}", team);
        events.trigger(turnStartEvent, data.get(team, Components.NEXT_TEAM));
        IntArrayList actors = data.query(Components.MEMBER_OF).list(hasValue(Components.MEMBER_OF, team));
        LOG.info("setting active for members of team {}: {}", team, actors);
        actors.forEach(x -> {
            events.response(resetActiveActionPointsEvent, x);
            events.response(resetActiveMovePointsEvent, x);
        });
    }

}
