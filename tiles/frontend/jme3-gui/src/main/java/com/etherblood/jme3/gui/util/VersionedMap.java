package com.etherblood.jme3.gui.util;

import com.simsilica.lemur.core.VersionedObject;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class VersionedMap<K, V> implements VersionedObject<Map<K, VersionedObject<V>>> {

    private final HashMap<K, VersionedObject<V>> map = new HashMap<>();
    
    @Override
    public long getVersion() {
        try {
            Field field = HashMap.class.getDeclaredField("modCount");
            field.setAccessible(true);
            return field.getInt(map);
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public Map<K, VersionedObject<V>> getObject() {
        return map;
    }

    @Override
    public VersionedMapReference<K, V> createReference() {
        return new VersionedMapReference<>(this);
    }

}
