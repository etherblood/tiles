package com.etherblood.entities;

import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ComponentMetaBuilder {

    private Integer id;
    private String name;
    private boolean foreignKey, indexed;
    private IntFunction<Object> objectify;

    ComponentMetaBuilder() {
        id = null;
        name = null;
        foreignKey = false;
        indexed = false;
        objectify = x -> x;
    }

    public ComponentMetaBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ComponentMetaBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ComponentMetaBuilder withForeignKey() {
        foreignKey = true;
        return this;
    }

    public ComponentMetaBuilder withIndex() {
        indexed = true;
        return this;
    }

    public ComponentMetaBuilder withObjectify(IntFunction<Object> objectify) {
        this.objectify = objectify;
        return this;
    }

    public ComponentMeta build() {
        return new ComponentMeta(id, name, foreignKey, indexed, objectify);
    }

}
