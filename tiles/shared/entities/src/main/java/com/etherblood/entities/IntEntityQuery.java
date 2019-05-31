package com.etherblood.entities;

import java.util.OptionalInt;
import java.util.function.IntPredicate;

public interface IntEntityQuery extends EntityQuery {

    int sumValues(IntPredicate predicate);

    default int sumValues() {
        return sumValues(x -> true);
    }

    OptionalInt uniqueValue(IntPredicate predicate);

    default OptionalInt uniqueValue() {
        return uniqueValue(x -> true);
    }
}
