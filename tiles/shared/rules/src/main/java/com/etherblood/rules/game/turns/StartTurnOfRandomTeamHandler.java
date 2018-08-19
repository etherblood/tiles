package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.events.EventDefinition;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.components.Components;
import com.etherblood.rules.events.EntityEvent;
import com.etherblood.rules.events.VoidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartTurnOfRandomTeamHandler extends AbstractGameEventHandler implements EventHandler<VoidEvent> {
    
    private static final Logger LOG = LoggerFactory.getLogger(StartTurnOfRandomTeamHandler.class);
    
    private final EventDefinition turnStart;
    
    public StartTurnOfRandomTeamHandler(EventDefinition turnStart) {
        this.turnStart = turnStart;
    }
    
    public void handle() {
        IntArrayList teams = data.query(Components.NEXT_TEAM).list();
        int team = teams.get(random.applyAsInt(teams.size()));
        LOG.info("selected {} as starting team", team);
        events.response(new EntityEvent(turnStart.id(), team));
    }

    @Override
    public void handle(VoidEvent event) {
        handle();
    }
    
}
