package com.etherblood.core;

import java.util.Map;
import java.util.TreeMap;

public class EntityDebugMap {

    private final int entity;
    private final Map<String, Object> components;

    public EntityDebugMap(int entity, Map<String, Object> components) {
        this.entity = entity;
        this.components = new TreeMap<>(components);
    }

    public int getEntity() {
        return entity;
    }

    public Map<String, Object> getComponents() {
        return components;
    }

}
