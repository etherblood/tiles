package com.etherblood.entities;

import com.etherblood.collections.IntToIntMap;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class SimpleEntityQueryTest extends EntityQueryTest {

    @Override
    protected EntityQuery createInstance(Map<Integer, Integer> map) {
        IntToIntMap data = new IntToIntMap();
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            data.set(entry.getKey(), entry.getValue());
        }
        return new SimpleEntityQuery(data);
    }

}
