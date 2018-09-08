package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntToIntHashMap;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class SimpleEntityQuery implements EntityQuery {

    private final IntToIntHashMap source;

    SimpleEntityQuery(IntToIntHashMap source) {
        this.source = source;
    }

    @Override
    public int count(IntPredicate predicate) {
        int count = 0;
        PrimitiveIterator.OfInt iterator = source.iterator();
        while (iterator.hasNext()) {
            int key = iterator.nextInt();
            if (predicate.test(key)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public int count() {
        return source.size();
    }

    @Override
    public boolean exists(IntPredicate predicate) {
        PrimitiveIterator.OfInt iterator = source.iterator();
        while (iterator.hasNext()) {
            if (predicate.test(iterator.nextInt())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean exists() {
        return source.size() != 0;
    }

    @Override
    public int sum(IntPredicate predicate) {
        int sum = 0;
        PrimitiveIterator.OfInt iterator = source.iterator();
        while (iterator.hasNext()) {
            int key = iterator.nextInt();
            if (predicate.test(key)) {
                sum += source.get(key);
            }
        }
        return sum;
    }

    @Override
    public void list(IntArrayList out, IntPredicate predicate) {
        out.clear();
        source.foreachKey(x -> {
            if (predicate.test(x)) {
                out.add(x);
            }
        });
        out.sort();
    }

    @Override
    public OptionalInt unique(IntPredicate predicate) {
        int result = -1;
        boolean found = false;
        PrimitiveIterator.OfInt iterator = source.iterator();
        while (iterator.hasNext()) {
            int key = iterator.nextInt();
            if (predicate.test(key)) {
                if (found) {
                    throw new IllegalStateException("multiple results found for unique query");
                } else {
                    result = key;
                    found = true;
                }
            }
        }
        return found ? OptionalInt.of(result) : OptionalInt.empty();
    }
}
