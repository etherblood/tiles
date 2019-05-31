package com.etherblood.core.components;

public class RegistryCloner {

    public ComponentRegistry createClone(ComponentRegistry source) {
        ComponentRegistry result = createEmptyClone(source);
        copyData(source, result);
        return result;
    }

    public ComponentRegistry createEmptyClone(ComponentRegistry source) {
        ComponentRegistry target = new ComponentRegistry();
        for (Component<?> component : source.getComponents()) {
            if (component.getComponentType() == int.class) {
                target.newIntComponent(component.name);
            } else if (component.getComponentType() == boolean.class) {
                target.newBoolComponent(component.name);
            } else {
                target.newGeneralComponent(component.name, component.getComponentType());
            }
        }
        return target;
    }

    public void copyData(ComponentRegistry source, ComponentRegistry target) {
        for (int id = 0; id < source.getComponents().size(); id++) {
            copyData(source.getComponents().get(id), target.getComponents().get(id));
        }
    }

    @SuppressWarnings("unchecked")
    private void copyData(Component<?> source, Component<?> target) {
        if (source instanceof IntComponent) {
            copyData((IntComponent) source, (IntComponent) target);
        } else if (source instanceof BoolComponent) {
            copyData((BoolComponent) source, (BoolComponent) target);
        } else {
            copyData((GeneralComponent) source, (GeneralComponent) target);
        }
    }

    private void copyData(IntComponent source, IntComponent target) {
        for (int entity : source.query().list()) {
            target.set(entity, source.get(entity));
        }
    }

    private void copyData(BoolComponent source, BoolComponent target) {
        for (int entity : source.query().list()) {
            target.set(entity);
        }
    }

    private <T> void copyData(GeneralComponent<T> source, GeneralComponent<T> target) {
        for (int entity : source.query().list()) {
            target.set(entity, source.getGeneric(entity));
        }
    }
}
