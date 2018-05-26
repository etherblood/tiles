package com.etherblood.events;

import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ArgumentDefinition {

    private final String name;
    private final boolean isEntity;
    private final IntFunction<String> stringify;

    public ArgumentDefinition(String name, boolean isEntity) {
        this(name, isEntity, Integer::toString);
    }

    public ArgumentDefinition(String name, boolean isEntity, IntFunction<String> stringify) {
        this.name = name;
        this.isEntity = isEntity;
        this.stringify = stringify;
    }

    public String getName() {
        return name;
    }

    public boolean isIsEntity() {
        return isEntity;
    }

    public String toReadable(int value) {
        return stringify.apply(value);
    }

}
