package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.NullaryHandler;
import com.etherblood.rules.GameEventHandler;
import com.etherblood.rules.components.Components;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartTurnOfRandomTeamHandler extends GameEventHandler implements NullaryHandler {

    private static final Logger LOG = LoggerFactory.getLogger(StartTurnOfRandomTeamHandler.class);

    private final EventDefinition turnStart;

    public StartTurnOfRandomTeamHandler(EventDefinition turnStart) {
        this.turnStart = turnStart;
    }

    @Override
    public void handle() {
        IntArrayList teams = data.component(Components.NEXT_TEAM).entities();
        int team = teams.get(random.applyAsInt(teams.size()));
        LOG.info("selected {} as starting team", team);
        events.response(turnStart.id(), team);
    }

}
