package com.etherblood.rules;

import com.etherblood.entities.EntityData;
import com.etherblood.events.Event;
import com.etherblood.events.EventQueue;
import java.util.function.IntUnaryOperator;

/**
 *
 * @author Philipp
 */
public abstract class GameEventHandler<T extends Event> {
    
//    public ComponentDefinitions components;
    public EntityData data;
    public EventQueue events;
    public IntUnaryOperator random;

    public abstract void handle(T event);
}
