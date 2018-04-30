package com.etherblood.sandbox;

import java.util.function.Supplier;

/**
 *
 * @author Philipp
 */
public class LazyString {

    private final Supplier<Object> supply;

    public LazyString(Supplier<Object> supply) {
        this.supply = supply;
    }

    @Override
    public String toString() {
        return String.valueOf(supply.get());
    }
}
