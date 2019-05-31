package com.etherblood.core.components;

import com.etherblood.entities.util.BoolComponentMap;
import com.etherblood.entities.util.IntComponentMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class RegistryMapConverter {

    public void toMap(ComponentRegistry registry, Map<Integer, Map<Integer, Object>> map) {
        for (Component<?> component : registry.getComponents()) {
            for (int entity : component.query().list()) {
                Map<Integer, Object> entityMap = map.computeIfAbsent(entity, x -> new LinkedHashMap<>());
                entityMap.put(component.id, component.getGeneric(entity));
            }
        }
    }

    public void fromMap(Map<Integer, Map<Integer, Object>> map, ComponentRegistry registry) {
        for (Map.Entry<Integer, Map<Integer, Object>> entry : map.entrySet()) {
            for (Map.Entry<Integer, Object> entityEntry : entry.getValue().entrySet()) {
                Component<Object> component = registry.getComponent(entityEntry.getKey());
                component.setGeneric(entry.getKey(), entityEntry.getValue());
            }
        }
    }
}
