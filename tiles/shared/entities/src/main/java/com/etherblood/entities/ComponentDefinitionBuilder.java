package com.etherblood.entities;

/**
 *
 * @author Philipp
 */
public class ComponentDefinitionBuilder {

    private Integer id;
    private String name;
    private boolean foreignKey, indexed;

    ComponentDefinitionBuilder() {
        id = null;
        name = null;
        foreignKey = false;
        indexed = false;
    }

    public ComponentDefinitionBuilder withId(int id) {
        this.id = id;
        return this;
    }

    public ComponentDefinitionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public ComponentDefinitionBuilder withForeignKey() {
        foreignKey = true;
        return this;
    }

    public ComponentDefinitionBuilder withIndex() {
        indexed = true;
        return this;
    }

    public ComponentDefinition build() {
        return new ComponentDefinition(id, name, foreignKey, indexed);
    }

}
