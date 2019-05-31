package com.etherblood.entities;

import com.etherblood.collections.IntToIntHashMap;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class SimpleEntityQueryTest extends AbstractEntityQueryTest {

    @Override
    protected IntEntityQuery createInstance(Map<Integer, Integer> map) {
        IntToIntHashMap data = new IntToIntHashMap();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            data.set(entry.getKey(), entry.getValue());
        }
        return new IntEntityQueryImpl(data);
    }

}
