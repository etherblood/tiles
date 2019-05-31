package com.etherblood.mods.core.game;

import com.etherblood.collections.IntHashSet;
import com.etherblood.core.GameResultProvider;
import com.etherblood.mods.core.game.components.CoreComponents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SingleTeamRemaining implements GameResultProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SingleTeamRemaining.class);
    private final CoreComponents core;

    public SingleTeamRemaining(CoreComponents core) {
        this.core = core;
    }

    @Override
    public boolean isGameOver() {
        IntHashSet teams = new IntHashSet();
        for (int actor : core.actor.memberOf.query().list(core.actor.isStatusOk::has)) {
            int team = core.actor.memberOf.get(actor);
            teams.set(team);
        }
        return teams.size() <= 1;
    }

}
