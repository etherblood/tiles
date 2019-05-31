package com.etherblood.jme3.gui.sprites;

public class ConsumableSeconds {

    private float remainingSeconds;

    public ConsumableSeconds(float remainingSeconds) {
        this.remainingSeconds = remainingSeconds;
    }

    public void add(float seconds) {
        assert seconds > 0;
        remainingSeconds += seconds;
    }

    public void consume(float seconds) {
        assert seconds > 0;
        remainingSeconds -= seconds;
        assert remainingSeconds >= 0;
    }
    
    public float update(float current, float max) {
        current += remainingSeconds;
        if(current > max) {
            remainingSeconds = current - max;
            return max;
        }
        remainingSeconds = 0;
        return current;
    }

    public boolean hasRemainingSeconds() {
        return remainingSeconds > 0;
    }

    public float getAvailableSeconds() {
        return remainingSeconds;
    }
}
