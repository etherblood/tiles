package com.etherblood.jme.client.state;

public class TransitionTime implements Comparable<TransitionTime> {

    private final float elapsedSeconds;

    public TransitionTime(float elapsedSeconds) {
        if (elapsedSeconds < 0 || !Float.isFinite(elapsedSeconds)) {
            throw new IllegalArgumentException();
        }
        this.elapsedSeconds = elapsedSeconds;
    }

    public float getElapsedSeconds() {
        return elapsedSeconds;
    }

    public TransitionTime plusSeconds(float seconds) {
        return new TransitionTime(elapsedSeconds + seconds);
    }

    @Override
    public int compareTo(TransitionTime time) {
        return Float.compare(elapsedSeconds, time.elapsedSeconds);
    }

    public boolean isBefore(TransitionTime time) {
        return elapsedSeconds < time.elapsedSeconds;
    }

    public boolean isAfter(TransitionTime time) {
        return elapsedSeconds > time.elapsedSeconds;
    }

    @Override
    public int hashCode() {
        return 71 + Float.floatToIntBits(this.elapsedSeconds);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof TransitionTime)) {
            return false;
        }
        TransitionTime other = (TransitionTime) obj;
        return elapsedSeconds == other.elapsedSeconds;
    }

    public float progress(TransitionTime start, TransitionTime end) {
        return durationSeconds(start, this) / durationSeconds(start, end);
    }

    public static float durationSeconds(TransitionTime start, TransitionTime end) {
        return end.elapsedSeconds - start.elapsedSeconds;
    }
}
