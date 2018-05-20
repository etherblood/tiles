package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.Event;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartTurnOfRandomTeamHandler extends GameEventHandler<Event> {

    private static final Logger LOG = LoggerFactory.getLogger(StartTurnOfRandomTeamHandler.class);

    @Override
    public void handle(Event event) {
        IntArrayList teams = data.component(Components.NEXT_TEAM).entities();
        int team = teams.get(random.applyAsInt(teams.size()));
        LOG.info("selected {} as starting team", team);
        events.response(new TurnStartEvent(team));
    }

}
