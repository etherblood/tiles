package com.etherblood.entities;

/**
 *
 * @author Philipp
 */
public class ComponentDefinition {

    public final int id;
    public final String name;
    private final boolean foreignKey;
    private final boolean indexed;

    ComponentDefinition(int id, String name, boolean foreignKey, boolean indexed) {
        this.id = id;
        this.name = name;
        this.foreignKey = foreignKey;
        this.indexed = indexed;
    }

    public static ComponentDefinitionBuilder builder() {
        return new ComponentDefinitionBuilder();
    }
}
