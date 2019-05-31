package com.etherblood.core.components;

import com.etherblood.entities.IntEntityQuery;
import com.etherblood.entities.util.IntComponentMap;
import com.etherblood.entities.util.IntComponentMapImpl;
import java.util.OptionalInt;

public class IntComponent extends Component<Integer> implements IntComponentMap {

    final IntComponentMap map;
    
    public IntComponent(int id, String name) {
        super(id, name);
        map = new IntComponentMapImpl();
    }
    
    @Override
    public int get(int entity) {
        return map.get(entity);
    }

    @Override
    public OptionalInt getOptional(int entity) {
        return map.getOptional(entity);
    }

    @Override
    public void set(int entity, int value) {
        map.set(entity, value);
    }

    @Override
    public void remove(int entity) {
        map.remove(entity);
    }

    @Override
    public boolean has(int entity) {
        return map.has(entity);
    }

    @Override
    public IntEntityQuery query() {
        return map.query();
    }

    @Override
    public Integer getGeneric(int entity) {
        return get(entity);
    }

    @Override
    public Class<Integer> getComponentType() {
        return int.class;
    }

    @Override
    public void setGeneric(int entity, Integer value) {
        set(entity, value);
    }

    @Override
    public void clear() {
        map.clear();
    }

}
