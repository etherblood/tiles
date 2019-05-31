package com.etherblood.entities.util;

import com.etherblood.entities.EntityQuery;

public interface BoolComponentMap {

    void set(int entity);

    void remove(int entity);

    boolean has(int entity);

    EntityQuery query();
    
    void clear();
    
}
