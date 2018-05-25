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
public class SimpleAggregator implements Aggregator {

    private final IntToIntMap source;

    SimpleAggregator(IntToIntMap source) {
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
        while(iterator.hasNext()) {
            if(predicate.test(iterator.nextInt())) {
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
    public OptionalInt compute(IntBinaryOperator operator, IntPredicate predicate) {
        AtomicInteger state = new AtomicInteger();
        AtomicBoolean first = new AtomicBoolean();
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
}
