package com.etherblood.entities;

import com.etherblood.collections.CollectionUtil;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class SimpleEntityData implements EntityData {

    private final IntSupplier idSequence;
    private final ComponentMap[] map;

    public SimpleEntityData(ComponentDefinition[] componentDetails, IntSupplier idSequence) {
        this.idSequence = idSequence;
        this.map = new SimpleComponentMap[componentDetails.length];
        for (int component = 0; component < map.length; component++) {
            map[component] = new SimpleComponentMap();
        }
    }

    @Override
    public int createEntity() {
        return idSequence.getAsInt();
    }

    @Override
    public boolean has(int entity, int component) {
        return component(component).has(entity);
    }

    @Override
    public int get(int entity, int component) {
        return component(component).get(entity);
    }

    @Override
    public void set(int entity, int component, int value) {
        component(component).set(entity, value);
    }

    @Override
    public void remove(int entity, int component) {
        component(component).remove(entity);
    }

    @Override
    public EntityQuery query(int component) {
        return component(component).query();
    }

    private ComponentMap component(int component) {
        return map[component];
    }

    @Override
    public void assertEmpty(int entity) {
        for (ComponentMap componentMap : map) {
            assert !componentMap.has(entity) : "entity " + entity + " still had a component of type " + CollectionUtil.indexOf(map, componentMap);
        }
    }
}
