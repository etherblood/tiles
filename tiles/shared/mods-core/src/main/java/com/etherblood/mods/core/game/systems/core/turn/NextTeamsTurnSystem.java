package com.etherblood.mods.core.game.systems.core.turn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.etherblood.core.GameSystem;
import com.etherblood.mods.core.game.components.CoreComponents;

/**
 *
 * @author Philipp
 */
public class NextTeamsTurnSystem implements GameSystem {

    private static final Logger LOG = LoggerFactory.getLogger(NextTeamsTurnSystem.class);
    private final CoreComponents core;

    public NextTeamsTurnSystem(CoreComponents core) {
        this.core = core;
    }

    @Override
    public void update() {
        if (!core.effect.triggered.query().exists() && !core.actor.active.query().exists()) {
            int team = core.activeTeam.query().unique().getAsInt();
            core.activeTeam.remove(team);
            int next = core.nextTeam.get(team);
            core.activeTeam.set(next);
            LOG.info("End turn of team #{}.", team);
            LOG.info("Start turn of team #{}.", next);
            for (int actor : core.actor.memberOf.query().list(x -> core.actor.memberOf.get(x) == next && core.actor.isStatusOk.has(x))) {
                LOG.debug("Activate actor #{}.", actor);
                core.actor.activate.set(actor);
            }
        }
    }

}
