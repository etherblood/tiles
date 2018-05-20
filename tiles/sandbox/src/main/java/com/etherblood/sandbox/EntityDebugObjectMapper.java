package com.etherblood.sandbox;

import com.etherblood.entities.ComponentDefinition;
import com.etherblood.entities.ComponentMap;
import com.etherblood.entities.EntityData;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Philipp
 */
public class EntityDebugObjectMapper {

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        for (ComponentDefinition componentDetail : data.getComponentDetails()) {
            ComponentMap component = data.component(componentDetail.id);
            for (int entity : component.entities()) {
                result.computeIfAbsent(entity, x -> new TreeMap<>()).put(componentDetail.name, component.get(entity));
            }
        }
        return result;
    }
}
