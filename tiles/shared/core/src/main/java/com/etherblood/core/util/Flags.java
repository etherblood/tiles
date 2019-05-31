package com.etherblood.core.util;

import java.util.PrimitiveIterator;

public class Flags {

    public static int toFlag(int index) {
        return 1 << index;
    }

    public static boolean containsAll(int flags, int sub) {
        return (flags & sub) == sub;
    }

    public static boolean containsIndex(int flags, int index) {
        return containsAll(flags, toFlag(index));
    }

    public static boolean isEmpty(int flags) {
        return flags == 0;
    }

    public static int union(int flagsA, int flagsB) {
        return flagsA | flagsB;
    }

    public static int intersection(int flagsA, int flagsB) {
        return flagsA & flagsB;
    }

    public static int lowestIndex(int flags) {
        return Integer.numberOfTrailingZeros(flags);
    }
    
    public static int lowestFlag(int flags) {
        return Integer.lowestOneBit(flags);
    }

    public static int inverse(int flags) {
        return ~flags;
    }

    public static PrimitiveIterator.OfInt indexIterator(int flags) {
        return new PrimitiveIterator.OfInt() {
            private int current = flags;

            @Override
            public int nextInt() {
                int index = lowestIndex(current);
                current ^= toFlag(index);
                return index;
            }

            @Override
            public boolean hasNext() {
                return !isEmpty(current);
            }
        };
    }
}
