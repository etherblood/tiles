package com.etherblood.ui.api.tracked;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Philipp
 */
public class TrackedProperty<T> {

    private final List<PropertyTracker<T>> trackers = new ArrayList<>();
    private T value = null;

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        T old = this.value;
        this.value = value;
        for (PropertyTracker<T> tracker : trackers) {
            tracker.onValueChanged(value, old);
        }
    }

    public void subscribe(PropertyTracker<T> tracker, boolean trigger) {
        trackers.add(tracker);
        if (trigger) {
            tracker.onValueChanged(value, null);
        }
    }

    public void unsubscribe(PropertyTracker<T> tracker, boolean trigger) {
        if(trackers.remove(tracker) && trigger) {
            tracker.onValueChanged(null, value);
        }
    }
}
