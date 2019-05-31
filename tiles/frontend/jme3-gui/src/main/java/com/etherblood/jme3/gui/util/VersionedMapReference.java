package com.etherblood.jme3.gui.util;

import com.simsilica.lemur.core.VersionedObject;
import com.simsilica.lemur.core.VersionedReference;
import java.util.HashMap;
import java.util.Map;

public class VersionedMapReference<K, V> extends VersionedReference<Map<K, VersionedObject<V>>> {

    private final Map<K, VersionedReference<V>> references = new HashMap<>();

    public VersionedMapReference(VersionedMap<K, V> map) {
        super(map);
    }

    @Override
    public boolean update() {
        boolean update = super.update();
        if (update) {
            Utils.updateEntries(get(), references, VersionedObject::createReference);
        }
        return update;
    }

    public Map<K, VersionedReference<V>> getReferences() {
        return references;
    }

    public VersionedReference<V> get(K key) {
        return references.get(key);
    }

}
