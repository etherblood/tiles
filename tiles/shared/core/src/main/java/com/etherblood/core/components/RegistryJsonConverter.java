package com.etherblood.core.components;

import com.etherblood.entities.util.BoolComponentMap;
import com.etherblood.entities.util.IntComponentMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

public class RegistryJsonConverter {

    public void serialize(ComponentRegistry registry, JsonWriter writer) throws IOException {
        writer.beginArray();
        for (Component<?> component : registry.getComponents()) {
            writeComponent(component, writer);
        }
        writer.endArray();
    }

    private <T> void writeComponent(Component<T> component, JsonWriter writer) throws IOException {
        writer.beginArray();
        for (int entity : component.query().list()) {
            if (component instanceof BoolComponentMap) {
                writer.value(entity);
            } else if (component instanceof IntComponentMap) {
                writer.beginObject();
                writer.name(Integer.toString(entity));
                writer.value(((IntComponentMap) component).get(entity));
                writer.endObject();
            } else {
                System.err.println("Unable to serialize" + component + ": #" + entity + "->" + component.getGeneric(entity));
            }
        }
        writer.endArray();
    }

    public void deserialize(JsonReader reader, ComponentRegistry registry) throws IOException {
        reader.beginArray();
        for (Component<?> component : registry.getComponents()) {
            readComponent(reader, component);
        }
        reader.endArray();
    }

    private <T> void readComponent(JsonReader reader, Component<T> component) throws IOException {
        reader.beginArray();
        while (reader.hasNext()) {
            if (component instanceof BoolComponentMap) {
                ((BoolComponentMap) component).set(reader.nextInt());
            } else if (component instanceof IntComponentMap) {
                reader.beginObject();
                int entity = Integer.parseInt(reader.nextName());
                ((IntComponentMap) component).set(entity, reader.nextInt());
                reader.endObject();
            } else {
                throw new AssertionError();
            }
        }
        reader.endArray();
    }
}
