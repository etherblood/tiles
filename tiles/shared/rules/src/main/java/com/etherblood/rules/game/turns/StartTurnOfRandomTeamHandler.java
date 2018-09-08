package com.etherblood.rules.game.turns;

import com.etherblood.collections.IntArrayList;
import com.etherblood.entities.ComponentMeta;
import com.etherblood.events.handlers.EventHandler;
import com.etherblood.rules.AbstractGameEventHandler;
import com.etherblood.rules.events.EntityEventMeta;
import com.etherblood.rules.events.VoidEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Philipp
 */
public class StartTurnOfRandomTeamHandler extends AbstractGameEventHandler implements EventHandler<VoidEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(StartTurnOfRandomTeamHandler.class);

    private final EntityEventMeta turnStart;
    private final ComponentMeta nextTeam;

    public StartTurnOfRandomTeamHandler(EntityEventMeta turnStart, ComponentMeta nextTeam) {
        this.turnStart = turnStart;
        this.nextTeam = nextTeam;
    }

    public void handle() {
        IntArrayList teams = data.query(nextTeam.id).list();
        int team = teams.get(random.applyAsInt(teams.size()));
        LOG.info("selected #{} as starting team", team);
        events.fire(turnStart.create(team));
    }

    @Override
    public void handle(VoidEvent event) {
        handle();
    }

}
