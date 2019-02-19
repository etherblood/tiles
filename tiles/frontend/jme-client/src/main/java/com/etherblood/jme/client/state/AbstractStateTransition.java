package com.etherblood.jme.client.state;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractStateTransition implements StateTransition {

    protected final float duration;
    protected final Set<Object> locks;
    protected TransitionTime start;

    public AbstractStateTransition(float duration, Collection<Object> locks) {
        this.duration = duration;
        this.locks = Collections.unmodifiableSet(new HashSet<>(locks));
    }

    @Override
    public Set<Object> locks() {
        return locks;
    }

    @Override
    public void start(TransitionTime time) {
        this.start = time;
    }

    @Override
    public void update(TransitionTime current) {
    }

    @Override
    public void end() {
    }

    @Override
    public TransitionTime getEnd() {
        return start.plusSeconds(duration);
    }

}
