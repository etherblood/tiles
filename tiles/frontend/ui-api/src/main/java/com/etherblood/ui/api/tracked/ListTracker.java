package com.etherblood.ui.api.tracked;

/**
 *
 * @author Philipp
 */
public interface ListTracker<T> {
    void onItemAdded(T item);
    void onItemRemoved(T item);
}
