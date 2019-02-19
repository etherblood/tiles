package com.etherblood.jme.client.sprites.data;

import com.jme3.math.Vector2f;
import java.util.Map;

public class SpritesheetData {

    public String defaultAnimation = "default";
    public String spritesheetPath;
    public Map<String, Map<FrameType, SpritesheetFrame>> frames;
    public Map<String, SpritesheetAnimation> animations;
    public Vector2f uvScale = Vector2f.UNIT_XY;
    public float scale = 1;

}
