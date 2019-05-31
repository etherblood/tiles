package com.etherblood.core;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AnimationsController {

    private boolean enabled = false;
    private final List<AnimationEvent> animationEvents = new ArrayList<>();

    public void enqueue(Supplier<AnimationEvent> eventSupplier) {
        if (enabled) {
            animationEvents.add(eventSupplier.get());
        }
    }

    public void enqueue(AnimationEvent event) {
        if (enabled) {
            animationEvents.add(event);
        }
    }

    public void enable() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public List<AnimationEvent> getAnimationEvents() {
        return animationEvents;
    }
}
