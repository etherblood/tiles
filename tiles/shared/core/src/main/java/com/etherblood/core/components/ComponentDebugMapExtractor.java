package com.etherblood.core.components;

import com.etherblood.core.EntityDebugMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ComponentDebugMapExtractor {

    public List<EntityDebugMap> extract(ComponentRegistry registry) {
        Map<Integer, Map<String, Object>> map = new TreeMap<>();
        for (Component<?> component : registry.getComponents()) {
            for (int entity : component.query().list()) {
                map.computeIfAbsent(entity, x -> new HashMap<>()).put(component.name, component.getGeneric(entity));
            }
        }
        return map.entrySet().stream().map(x -> new EntityDebugMap(x.getKey(), x.getValue())).collect(Collectors.toList());
    }
}
