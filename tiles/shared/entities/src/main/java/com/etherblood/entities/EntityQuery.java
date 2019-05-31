package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntList;
import java.util.OptionalInt;
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

    OptionalInt unique(IntPredicate predicate);

    default OptionalInt unique() {
        return unique(x -> true);
    }

    void list(IntList out, IntPredicate predicate);

    default void list(IntList out) {
        list(out, x -> true);
    }

    default IntList list(IntPredicate predicate) {
        IntList result = new IntArrayList();
        list(result, predicate);
        return result;
    }

    default IntList list() {
        IntList result = new IntArrayList();
        list(result);
        return result;
    }
    
}
