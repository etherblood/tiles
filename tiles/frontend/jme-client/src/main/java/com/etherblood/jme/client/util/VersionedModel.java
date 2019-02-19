package com.etherblood.jme.client.util;

import java.util.Objects;

public class VersionedModel<T> {

    private T value;
    private int version;

    public VersionedModel() {
    }

    public VersionedModel(T value) {
        this.value = value;
    }

    public VersionedReference<T> createReference() {
        return new VersionedReference<>(this, version - 1);
    }

    public T get() {
        return value;
    }

    public int getVersion() {
        return version;
    }

    public void set(T value) {
        if (!Objects.equals(this.value, value)) {
            this.value = value;
            version++;
        }
    }
}
