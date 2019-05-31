package com.etherblood.mods.core.game.systems.walk.animation;

import com.etherblood.core.AnimationEvent;

public class WalkAnimation implements AnimationEvent {

    public final int actor, from, to;

    public WalkAnimation(int actor, int from, int to) {
        this.actor = actor;
        this.from = from;
        this.to = to;
    }
}
