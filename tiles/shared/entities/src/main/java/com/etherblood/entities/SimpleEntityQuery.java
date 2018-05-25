package com.etherblood.entities;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntToIntMap;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntBinaryOperator;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class SimpleEntityQuery implements EntityQuery {

    private final IntToIntMap source;

    SimpleEntityQuery(IntToIntMap source) {
        this.source = source;
    }

    @Override
    public int count(IntPredicate predicate) {
        AtomicInteger count = new AtomicInteger(0);
        source.foreachKey(x -> {
            if (predicate.test(x)) {
                count.incrementAndGet();
            }
        });
        return count.get();
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
    public OptionalInt aggregate(IntBinaryOperator operator, IntPredicate predicate) {
        AtomicInteger state = new AtomicInteger();
        AtomicBoolean first = new AtomicBoolean(true);
        source.foreach((entity, value) -> {
            if (predicate.test(entity)) {
                if (first.get()) {
                    state.set(value);
                    first.set(false);
                } else {
                    state.accumulateAndGet(value, operator);
                }
            }
        });
        return first.get() ? OptionalInt.empty() : OptionalInt.of(state.get());
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
        AtomicInteger state = new AtomicInteger();
        AtomicBoolean found = new AtomicBoolean(false);
        source.foreach((entity, value) -> {
            if (predicate.test(entity)) {
                if (found.get()) {
                    throw new IllegalStateException("multiple results found for unique query");
                } else {
                    state.set(entity);
                    found.set(true);
                }
            }
        });
        return found.get() ? OptionalInt.of(state.get()) : OptionalInt.empty();
    }
}
