package com.etherblood.sandbox;

import com.etherblood.entities.ComponentDefinition;
import com.etherblood.entities.EntityData;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Philipp
 */
public class EntityDebugObjectMapper {

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data, ComponentDefinition[] components) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        for (ComponentDefinition componentDetail : components) {
            for (int entity : data.query(componentDetail.id).list()) {
                result.computeIfAbsent(entity, x -> new TreeMap<>()).put(componentDetail.name, data.get(entity, componentDetail.id));
            }
        }
        return result;
    }
}
