package com.etherblood.mods.core.game.systems;

import com.etherblood.mods.core.game.components.CoreComponents;

public class ResetActivatedActorMovePointsSystem extends ResetActivatedActorStatSystem {

    public ResetActivatedActorMovePointsSystem(CoreComponents core) {
        super(core, core.stats.movePoints);
    }

}
