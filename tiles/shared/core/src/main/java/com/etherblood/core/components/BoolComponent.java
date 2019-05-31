package com.etherblood.core.components;

import com.etherblood.entities.EntityQuery;
import com.etherblood.entities.util.BoolComponentMap;
import com.etherblood.entities.util.BoolComponentMapImpl;

public class BoolComponent extends Component<Boolean> implements BoolComponentMap {

    final BoolComponentMap map;

    public BoolComponent(int id, String name) {
        super(id, name);
        map = new BoolComponentMapImpl();
    }

    @Override
    public void set(int entity) {
        map.set(entity);
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
    public EntityQuery query() {
        return map.query();
    }

    @Override
    public Boolean getGeneric(int entity) {
        return has(entity);
    }

    @Override
    public Class<Boolean> getComponentType() {
        return boolean.class;
    }

    @Override
    public void setGeneric(int entity, Boolean value) {
        if (value) {
            set(entity);
        } else {
            remove(entity);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

}
