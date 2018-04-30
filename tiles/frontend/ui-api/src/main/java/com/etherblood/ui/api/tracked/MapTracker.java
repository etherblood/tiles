package com.etherblood.ui.api.tracked;

/**
 *
 * @author Philipp
 */
public interface MapTracker<K, V> {
    void onItemSet(K key, V newItem, V oldItem);
}
