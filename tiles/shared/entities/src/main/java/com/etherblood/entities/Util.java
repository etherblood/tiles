package com.etherblood.entities;

import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class Util {

    public static boolean all(int entity, IntPredicate... predicates) {
        for (IntPredicate predicate : predicates) {
            if (!predicate.test(entity)) {
                return false;
            }
        }
        return true;
    }

}
