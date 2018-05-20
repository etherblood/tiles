package com.etherblood.entities.query;

import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class ComponentComparisonEqual implements IntPredicate {

    private final int value;

    public ComponentComparisonEqual(int value) {
        this.value = value;
    }
    
    @Override
    public boolean test(int value) {
        return this.value == value;
    }

}
