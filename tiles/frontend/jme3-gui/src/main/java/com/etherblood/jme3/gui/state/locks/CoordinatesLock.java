package com.etherblood.jme3.gui.state.locks;

public class CoordinatesLock {

    private final int coordinates;

    public CoordinatesLock(int coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int hashCode() {
        return coordinates;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof CoordinatesLock)) {
            return false;
        }
        CoordinatesLock other = (CoordinatesLock) obj;
        return coordinates == other.coordinates;
    }
}
