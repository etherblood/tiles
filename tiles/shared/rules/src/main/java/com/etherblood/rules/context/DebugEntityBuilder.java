package com.etherblood.rules.context;

import com.etherblood.collections.IntSet;
import com.etherblood.entities.ComponentMap;
import com.etherblood.entities.ComponentMapFactory;
import com.etherblood.entities.SimpleComponentMap;
import com.etherblood.entities.UniqueComponentMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class DebugEntityBuilder {

    private final Map<ComponentMap, ComponentDebugData> debugComponents = new LinkedHashMap<>();
    private final ComponentMapFactory factory = new ComponentMapFactory();

    public StatComponentMaps stats(String name) {
        return new StatComponentMaps(simple(name + "-base"), simple(name + "-additive"), simple(name + "-active"), simple(name + "-buffed"));
    }

    public ElementComponentMaps element(String name) {
        return new ElementComponentMaps(stats(name + "Power"), stats(name + "Toughness"));
    }

    public SimpleComponentMap simple(String name) {
        return simple(name, x -> x);
    }

    public SimpleComponentMap simple(String name, IntFunction<?> converter) {
        return register(factory.simple(), name, converter);
    }

    public UniqueComponentMap unique(String name) {
        return unique(name, x -> x);
    }

    public UniqueComponentMap unique(String name, IntFunction<?> converter) {
        return register(factory.unique(), name, converter);
    }

    private <T extends ComponentMap> T register(T map, String name, IntFunction<?> converter) {
        debugComponents.put(map, new ComponentDebugData(map, name, converter));
        return map;
    }

    public Map<Integer, Map<String, Object>> toDebugObjects(IntSet entities) {
        Map<Integer, Map<String, Object>> result = new TreeMap<>();
        entities.foreach(entity -> {
            result.put(entity, toDebugObject(entity));
        });
        return result;
    }

    private Map<String, Object> toDebugObject(int entity) {
        Map<String, Object> result = new TreeMap<>();
        for (ComponentDebugData data : debugComponents.values()) {
            if (data.map.has(entity)) {
                int value = data.map.get(entity);
                result.put(data.name, data.converter.apply(value));
            }
        }
        return result;
    }
}
