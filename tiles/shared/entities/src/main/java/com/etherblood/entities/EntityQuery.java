package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import java.util.OptionalInt;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public interface EntityQuery {

    int count(IntPredicate predicate);

    default int count() {
        return count(x -> true);
    }

    default boolean exists(IntPredicate predicate) {
        return count(predicate) != 0;
    }

    default boolean exists() {
        return exists(x -> true);
    }

    OptionalInt aggregate(IntBinaryOperator operator, IntPredicate predicate);

    default OptionalInt aggregate(IntBinaryOperator operator) {
        return aggregate(operator, x -> true);
    }

    OptionalInt unique(IntPredicate predicate);

    default OptionalInt unique() {
        return unique(x -> true);
    }

    void list(IntArrayList out, IntPredicate predicate);

    default void list(IntArrayList out) {
        list(out, x -> true);
    }

    default IntArrayList list(IntPredicate predicate) {
        IntArrayList result = new IntArrayList();
        list(result, predicate);
        return result;
    }

    default IntArrayList list() {
        IntArrayList result = new IntArrayList();
        list(result);
        return result;
    }
    
}