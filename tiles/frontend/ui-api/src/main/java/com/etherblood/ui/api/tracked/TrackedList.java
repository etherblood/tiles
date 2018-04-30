package com.etherblood.ui.api.tracked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Philipp
 */
public class TrackedList<T> {

    private final List<ListTracker<T>> trackers = new ArrayList<>();
    private final List<T> list = new ArrayList<>();

    public T getValue(int key) {
        return list.get(key);
    }

    public void add(T value) {
        list.add(value);
        for (ListTracker<T> tracker : trackers) {
            tracker.onItemAdded(value);
        }
    }

    public void remove(T value) {
        if (list.remove(value)) {
            for (ListTracker<T> tracker : trackers) {
                tracker.onItemRemoved(value);
            }
        }
    }

    public void subscribe(ListTracker<T> tracker, boolean trigger) {
        trackers.add(tracker);
        if (trigger) {
            for (T item : list) {
                tracker.onItemAdded(item);
            }
        }
    }

    public void unsubscribe(ListTracker<T> tracker, boolean trigger) {
        if (trackers.remove(tracker) && trigger) {
            for (T item : list) {
                tracker.onItemRemoved(item);
            }
        }
    }
}
