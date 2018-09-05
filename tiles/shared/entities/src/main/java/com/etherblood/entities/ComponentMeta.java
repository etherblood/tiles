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
    private final IntFunction<String> stringify;

    public ComponentMeta(int id, String name, boolean foreignKey, boolean indexed, IntFunction<String> stringify) {
        this.id = id;
        this.name = name;
        this.foreignKey = foreignKey;
        this.indexed = indexed;
        this.stringify = stringify;
    }

    public static ComponentMetaBuilder builder() {
        return new ComponentMetaBuilder();
    }
    
    public String toString(int value) {
        return stringify.apply(value);
    }
}
