package com.etherblood.rules.context;

import com.etherblood.entities.ComponentMap;
import java.util.function.IntFunction;

/**
 *
 * @author Philipp
 */
public class ComponentDebugData {

    final ComponentMap map;
    final String name;
    final IntFunction<?> converter;

    public ComponentDebugData(ComponentMap map, String name, IntFunction<?> converter) {
        this.map = map;
        this.name = name;
        this.converter = converter;
    }
    
}
