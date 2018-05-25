package com.etherblood.entities;

import com.etherblood.collections.IntSet;
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
    
    public static IntSupplier simple1() {
        return linearCongruentialGenerator(0x01e56038, 69069, 1);
    }
    public static IntSupplier simple2() {
        return linearCongruentialGenerator(0x01e56038, 1664525, 1013904223);
    }
    public static IntSupplier simple3() {
        return linearCongruentialGenerator(0x01e56038, 22695477, 1);
    }

    private static IntSupplier linearCongruentialGenerator(int initialState, int multiplier, int increment) {
        return new IntSupplier() {
            private int state = initialState;

            @Override
            public int getAsInt() {
                state = multiplier * state + increment;
                return state;
            }
        };
    }

}
