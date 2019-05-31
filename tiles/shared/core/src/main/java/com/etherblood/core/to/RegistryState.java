package com.etherblood.core.to;

import com.etherblood.core.components.Component;
import com.etherblood.core.components.ComponentRegistry;
import java.util.ArrayList;
import java.util.List;

public class RegistryState {

    public List<EntityComponentValue> entries;

    public void toRegistry(ComponentRegistry registry) {
        for (EntityComponentValue entry : entries) {
            Component<Object> component = registry.getComponent(entry.component);
            component.setGeneric(entry.entity, entry.value);
        }
    }

    public void fromRegistry(ComponentRegistry registry) {
        entries = new ArrayList<>();
        for (Component<?> component : registry.getComponents()) {
            for (int entity : component.query().list()) {
                entries.add(new EntityComponentValue(entity, component.id, component.getGeneric(entity)));
            }
        }
    }

    @Override
    public String toString() {
        return "RegistryState{" + "entries=" + entries + '}';
    }
}
