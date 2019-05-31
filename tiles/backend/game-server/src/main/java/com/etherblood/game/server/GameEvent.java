package com.etherblood.game.server;

import java.time.Instant;

public class GameEvent<T> {

    private final Instant time;
    private final T value;

    public GameEvent(T value) {
        this(Instant.now(), value);
    }

    public GameEvent(Instant time, T value) {
        this.time = time;
        this.value = value;
    }

    public Instant getTime() {
        return time;
    }

    public T getValue() {
        return value;
    }
}
