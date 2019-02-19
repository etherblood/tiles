package com.etherblood.jme.client.util;

public class VersionedReference<T> {

    private final VersionedModel<T> model;
    private int version;

    VersionedReference(VersionedModel<T> model, int version) {
        this.model = model;
        this.version = version;
    }

    public boolean update() {
        try {
            return model.getVersion() != version;
        } finally {
            version = model.getVersion();
        }
    }

    public T get() {
        return model.get();
    }

    public void set(T value) {
        model.set(value);
    }

    public VersionedModel<T> getModel() {
        return model;
    }

    public int getVersion() {
        return version;
    }
}
