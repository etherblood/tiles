package com.etherblood.jme3.gui.state;

import java.util.Set;

public interface StateTransition {

    Set<Object> locks();
    
    void start(TransitionTime time);
    
    void update(TransitionTime current);
    
    void end();
    
    TransitionTime getEnd();
    
}
