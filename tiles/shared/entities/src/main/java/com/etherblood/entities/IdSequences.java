package com.etherblood.entities;

import com.etherblood.collections.IntHashSet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntSupplier;

/**
 *
 * @author Philipp
 */
public class IdSequences {

    public static IntSupplier backingSet(IntSupplier supply) {
        return new IntSupplier() {
            private final IntHashSet used = new IntHashSet();

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
        return incremental(0);
    }
    
    public static IntSupplier incremental(int start) {
        return new AtomicInteger(start)::getAndIncrement;
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
