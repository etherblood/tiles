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
    private IntFunction<String> stringify;

    ComponentMetaBuilder() {
        id = null;
        name = null;
        foreignKey = false;
        indexed = false;
        stringify = Integer::toString;
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

    public ComponentMetaBuilder withStringify(IntFunction<String> stringify) {
        this.stringify = stringify;
        return this;
    }

    public ComponentMeta build() {
        return new ComponentMeta(id, name, foreignKey, indexed, stringify);
    }

}
