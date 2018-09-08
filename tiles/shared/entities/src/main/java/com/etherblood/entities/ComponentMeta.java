package com.etherblood.entities;

import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ComponentMeta {

    public final int id;
    public final String name;
    private final boolean foreignKey;
    private final boolean indexed;
    private final IntFunction<Object> objectify;

    public ComponentMeta(int id, String name, boolean foreignKey, boolean indexed, IntFunction<Object> objectify) {
        this.id = id;
        this.name = name;
        this.foreignKey = foreignKey;
        this.indexed = indexed;
        this.objectify = objectify;
    }

    public static ComponentMetaBuilder builder() {
        return new ComponentMetaBuilder();
    }
    
    public Object toPojo(int value) {
        return objectify.apply(value);
    }
}
