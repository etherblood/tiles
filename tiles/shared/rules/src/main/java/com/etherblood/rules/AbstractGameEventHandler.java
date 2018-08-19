package com.etherblood.rules;

import com.etherblood.entities.EntityData;
import com.etherblood.events.EventQueue;
import java.util.function.IntPredicate;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public abstract class AbstractGameEventHandler {

    public EntityData data;
    public EventQueue events;
    public IntUnaryOperator random;
    
    protected IntPredicate hasValue(int component, int value) {
        return x -> data.hasValue(x, component, value);
    }
    
    protected IntPredicate has(int component) {
        return x -> data.has(x, component);
    }
}
