package com.etherblood.mods.core.game.systems;

import com.etherblood.mods.core.game.components.CoreComponents;

public class ResetActivatedActorActionPointsSystem extends ResetActivatedActorStatSystem {

    public ResetActivatedActorActionPointsSystem(CoreComponents core) {
        super(core, core.stats.actionPoints);
    }

}
