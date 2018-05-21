package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public class ArgumentDefinition {

    private final String name;
    private final boolean isEntity;

    public ArgumentDefinition(String name, boolean isEntity) {
        this.name = name;
        this.isEntity = isEntity;
    }

    public String getName() {
        return name;
    }

    public boolean isIsEntity() {
        return isEntity;
    }

}
