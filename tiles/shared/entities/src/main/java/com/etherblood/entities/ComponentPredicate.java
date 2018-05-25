package com.etherblood.entities;

import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class ComponentPredicate {

    int component;
    IntPredicate predicate;
    boolean allowNull;

    public ComponentPredicate(int component, IntPredicate predicate, boolean allowNull) {
        this.component = component;
        this.predicate = predicate;
        this.allowNull = allowNull;
    }

}
