package com.etherblood.jme.client.sprites;

import com.etherblood.jme.client.sprites.data.SpritesheetAnimation;
import com.etherblood.jme.client.sprites.data.SpritesheetAnimationFrame;
import java.util.Objects;

public class AnimationInstance {

    private final String key;
    private final SpritesheetAnimation data;
    private float progress = 0;

    public AnimationInstance(String key, SpritesheetAnimation data) {
        this.data = Objects.requireNonNull(data);
        this.key = Objects.requireNonNull(key);
    }

    public String getCurrentFrameKey() {
        return getCurrentFrame().frameKey;
    }

    private SpritesheetAnimationFrame getCurrentFrame() {
        assert !hasEnded();
        int totalRepeatCount = data.frames.stream().mapToInt(x -> x.repeat).sum();
        int elapsedRepeats = (int) (progress * totalRepeatCount);
        int current = 0;
        for (SpritesheetAnimationFrame frame : data.frames) {
            current += frame.repeat;
            if (current > elapsedRepeats) {
                return frame;
            }
        }
        throw new IllegalStateException();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        assert 0 <= progress && progress <= 1;
        this.progress = progress;
    }

    public float getDurationSeconds() {
        return data.cycleSeconds;
    }

    public boolean hasEnded() {
        return progress == 1;
    }

    public String getKey() {
        return key;
    }

}
