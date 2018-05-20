package com.etherblood.entities;

/**
 *
 * @author Philipp
 */
public class EntityData {

    private final ComponentDefinition[] componentDetails;
    private final ComponentMap[] map;

    public EntityData(ComponentDefinition[] componentDetails) {
        this.componentDetails = componentDetails;
        this.map = new ComponentMap[componentDetails.length];
        for (int i = 0; i < map.length; i++) {
            map[i] = new SimpleComponentMap();
        }
    }
    
    public ComponentMap component(int component) {
        return map[component];
    }

    public ComponentDefinition[] getComponentDetails() {
        return componentDetails;
    }
}
