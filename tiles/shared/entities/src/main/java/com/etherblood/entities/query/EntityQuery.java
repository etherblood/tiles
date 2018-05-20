package com.etherblood.entities.query;

import com.etherblood.collections.IntArrayList;
import com.etherblood.collections.IntToIntMap;
import com.etherblood.entities.ComponentMapView;
import com.etherblood.entities.Util;
import java.util.function.IntPredicate;

/**
 *
 * @author Philipp
 */
public class EntityQuery {

    private final IntArrayList resultCache = new IntArrayList();
    private final IntToIntMap baseMap;
    private final IntPredicate baseFilter;
    private final IntPredicate[] filters;

    public EntityQuery(IntToIntMap baseMap, IntPredicate baseFilter, IntPredicate[] filters) {
        this.baseMap = baseMap;
        this.baseFilter = baseFilter;
        this.filters = filters;
    }
    
    public IntArrayList execute() {
        resultCache.clear();
        baseMap.foreach((entity, value) -> {
            if(baseFilter.test(value) && Util.all(entity, filters)) {
                resultCache.add(entity);
            }
        });
        resultCache.sort();
        return resultCache;
    }
    
}
