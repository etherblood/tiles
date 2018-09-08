package com.etherblood.sandbox;

import com.etherblood.entities.ComponentMeta;
import com.etherblood.entities.EntityData;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author Philipp
 */
public class EntityDebugObjectMapper {

    public Map<Integer, Map<String, Object>> toDebugObjects(EntityData data, List<ComponentMeta> components) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        for (ComponentMeta componentDetail : components) {
            for (int entity : data.query(componentDetail.id).list()) {
                int value = data.get(entity, componentDetail.id);
                result.computeIfAbsent(entity, x -> new TreeMap<>()).put(componentDetail.name, componentDetail.toPojo(value));
            }
        }
        return result;
    }
}
