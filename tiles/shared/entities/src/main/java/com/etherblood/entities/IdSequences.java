package com.etherblood.entities;

import com.etherblood.collections.IntSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class IdSequences {

    public static IntSupplier backingSet(IntSupplier supply) {
        return new IntSupplier() {
            private final IntSet used = new IntSet();

            @Override
            public int getAsInt() {
                int id;
                do {
                    id = supply.getAsInt();
                } while (used.hasKey(id));
                used.set(id);
                return id;
            }
        };
    }
    
    public static IntSupplier incremental() {
        return new AtomicInteger(0)::getAndIncrement;
    }

    public static IntSupplier simple1() {
        return linearCongruentialGenerator(69069, 1);
    }

    public static IntSupplier simple2() {
        return linearCongruentialGenerator(1664525, 1013904223);
    }

    public static IntSupplier simple3() {
        return linearCongruentialGenerator(22695477, 1);
    }

    private static IntSupplier linearCongruentialGenerator(int multiplier, int increment) {
        return new IntSupplier() {
            private int state = 0;

            @Override
            public int getAsInt() {
                state = multiplier * state + increment;
                return state;
            }
        };
    }

}
