package com.etherblood.entities;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class ComponentMapFactory {

    private final List<ComponentMap> maps = new ArrayList<>();

    public SimpleComponentMap simple() {
        SimpleComponentMap map = new SimpleComponentMap();
        maps.add(map);
        return map;
    }

    public UniqueComponentMap unique() {
        UniqueComponentMap map = new UniqueComponentMap();
        maps.add(map);
        return map;
    }

    public List<ComponentMap> getMaps() {
        return maps;
    }
}
