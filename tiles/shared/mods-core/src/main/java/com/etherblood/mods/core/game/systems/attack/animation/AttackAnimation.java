package com.etherblood.mods.core.game.systems.attack.animation;

import com.etherblood.core.AnimationEvent;

public class AttackAnimation implements AnimationEvent {

    public final int actor, from, to;

    public AttackAnimation(int actor, int from, int to) {
        this.actor = actor;
        this.from = from;
        this.to = to;
    }

}
