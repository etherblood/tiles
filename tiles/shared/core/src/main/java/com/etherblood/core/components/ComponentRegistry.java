package com.etherblood.core.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ComponentRegistry {

    private static final Logger LOG = LoggerFactory.getLogger(ComponentRegistry.class);
    private final List<Component<?>> components = new ArrayList<>();

    public IntComponent newIntComponent(String name) {
        return registerComponent(new IntComponent(components.size(), name));
    }

    public BoolComponent newBoolComponent(String name) {
        return registerComponent(new BoolComponent(components.size(), name));
    }

    public <T> GeneralComponent<T> newGeneralComponent(String name, Class<T> clazz) {
        return registerComponent(new GeneralComponent<>(components.size(), name, clazz));
    }

    private <T extends Component<?>> T registerComponent(T component) {
        LOG.debug("Registered {}.", component);
        components.add(component);
        return component;
    }

    @SuppressWarnings("unchecked")
    public <T extends Component<?>> T getComponent(int id) {
        return (T) components.get(id);
    }

    public List<Component<?>> getComponents() {
        return Collections.unmodifiableList(components);
    }

}
