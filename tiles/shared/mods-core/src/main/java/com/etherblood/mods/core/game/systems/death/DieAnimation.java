package com.etherblood.mods.core.game.systems.death;

import com.etherblood.core.AnimationEvent;

public class DieAnimation implements AnimationEvent {

    public final int actor, position;

    public DieAnimation(int actor, int position) {
        this.actor = actor;
        this.position = position;
    }

}
