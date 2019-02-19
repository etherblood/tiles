package com.etherblood.events;

/**
 *
 * @author Philipp
 */
public abstract class Event {

    protected Event parent;
    protected boolean cancelled = false;
    protected final int id;

    public Event(int id) {
        this.id = id;
    }

    public Event getParent() {
        return parent;
    }

    void setParent(Event parent) {
        this.parent = parent;
    }

    boolean isCancelled() {
        return cancelled;
    }

    public void cancel() {
        this.cancelled = true;
    }

    public int getId() {
        return id;
    }
    
    @Override
    public abstract boolean equals(Object obj);
    
    @Override
    public abstract int hashCode();

}
