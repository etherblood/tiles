package com.etherblood.core.to;

public class EntityComponentValue {

    public int entity, component;
    public Object value;

    public EntityComponentValue() {
    }

    public EntityComponentValue(int entity, int component, Object value) {
        this.entity = entity;
        this.component = component;
        this.value = value;
    }

    @Override
    public String toString() {
        return "EntityComponentValue{" + "entity=" + entity + ", component=" + component + ", value=" + value + '}';
    }
}
