package com.etherblood.ui.api.tracked;

/**
 *
 * @author Philipp
 */
public interface PropertyTracker<T> {
    void onValueChanged(T newValue, T oldValue);
}
