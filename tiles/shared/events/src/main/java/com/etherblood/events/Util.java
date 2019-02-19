package com.etherblood.events;

import java.util.Arrays;

class Util {

    @SuppressWarnings("unchecked")
    public static <T> T[] append(T[] first, T... second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }
}
