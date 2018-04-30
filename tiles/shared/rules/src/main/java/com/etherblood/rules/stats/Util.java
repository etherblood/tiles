package com.etherblood.rules.stats;

import com.etherblood.collections.IntSet;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.rules.context.StatComponentMaps;

/**
 *
 * @author Philipp
 */
public class Util {

    public static void refreshAllForStat(StatComponentMaps stat, SimpleComponentMap buffOn) {
        IntSet entities = new IntSet();
        (stat.base).entities().foreach(entities::set);
        (stat.active).entities().foreach(entities::set);
        (stat.buffed).entities().foreach(entities::set);
        (stat.additive).entities().foreach(x -> entities.set(buffOn.get(x)));
        entities.foreach(x -> {
            Util.refreshBuffedStat(stat, buffOn, x);
            Util.resetActiveStat(stat, x);
        });
    }

    public static void refreshBuffedStat(StatComponentMaps stat, SimpleComponentMap buffOn, int target) {
        int value = (stat.base).getOrElse(target, 0);
        for (int entity : (stat.additive).entities(x -> buffOn.hasValue(x, target))) {
            value += (stat.additive).get(entity);
        }
        (stat.buffed).setWithDefault(target, value, 0);
    }

    public static void resetActiveStat(StatComponentMaps stat, int target) {
        int value = (stat.buffed).getOrElse(target, 0);
        (stat.active).setWithDefault(target, value, 0);
    }
}
