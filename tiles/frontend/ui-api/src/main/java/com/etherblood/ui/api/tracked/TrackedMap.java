package com.etherblood.ui.api.tracked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class TrackedMap<K, V> {

    private final List<MapTracker<K, V>> trackers = new ArrayList<>();
    private final Map<K, V> map = new HashMap<>();

    public V get(K key) {
        return map.get(key);
    }

    public boolean has(K key) {
        return map.containsKey(key);
    }

    public void set(K key, V value) {
        V old = map.put(key, value);
        for (MapTracker<K, V> tracker : trackers) {
            tracker.onItemSet(key, value, old);
        }
    }

    public void subscribe(MapTracker<K, V> tracker, boolean trigger) {
        trackers.add(tracker);
        if (trigger) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                tracker.onItemSet(entry.getKey(), entry.getValue(), null);
            }
        }
    }

    public void unsubscribe(MapTracker<K, V> tracker, boolean trigger) {
        if(trackers.remove(tracker) && trigger) {
            for (Map.Entry<K, V> entry : map.entrySet()) {
                tracker.onItemSet(entry.getKey(), null, entry.getValue());
            }
        }
    }
}
