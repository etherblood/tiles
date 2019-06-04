package com.etherblood.mods.core.game.systems.walk.animation;

import com.etherblood.core.AnimationEvent;

public class PushAnimation implements AnimationEvent {

    public final int actor, from, to;

    public PushAnimation(int actor, int from, int to) {
        this.actor = actor;
        this.from = from;
        this.to = to;
    }
}
