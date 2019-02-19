package com.etherblood.jme.client.sprites;

import com.etherblood.jme.client.sprites.data.FrameType;
import com.etherblood.jme.client.sprites.data.SpritesheetData;
import com.etherblood.jme.client.sprites.data.SpritesheetFrame;
import com.jme3.material.Material;
import com.jme3.math.Vector2f;
import java.util.Set;

public class SpritesheetInstance {

    private final SpritesheetData data;
    private final Material material;

    public SpritesheetInstance(SpritesheetData data, Material material) {
        this.data = data;
        this.material = material;
    }

    public String getSourceImagePath() {
        return data.spritesheetPath;
    }

    public String getDefaultAnimationKey() {
        return data.defaultAnimation;
    }

    public AnimationInstance createAnimation(String key) {
        return new AnimationInstance(key, data.animations.get(key));
    }

    public Material getMaterial() {
        return material;
    }

    public SpritesheetFrame getFrame(String key, FrameType frameType) {
        return data.frames.get(key).get(frameType);
    }

    public Vector2f getUvScale() {
        return data.uvScale;
    }

    public float getScale() {
        return data.scale;
    }

    public Set<String> getAnimations() {
        return data.animations.keySet();
    }
}
