package com.etherblood.jme3.gui.state.transitions;

import com.etherblood.core.util.Coordinates;
import com.etherblood.jme3.gui.sprites.Sprite;
import com.etherblood.jme3.gui.sprites.data.FrameType;
import com.etherblood.jme3.gui.state.AbstractStateTransition;
import com.etherblood.jme3.gui.state.TransitionTime;
import com.etherblood.jme3.gui.state.locks.CoordinatesLock;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.Arrays;

public class SpriteAnimationTransition extends AbstractStateTransition {

    private final Vector2f from, to;
    private final Sprite sprite;
    private final FrameType frameType;
    private final String animationKey;

    public SpriteAnimationTransition(int from, int to, Sprite sprite, String animationKey) {
        this(from, to, sprite, animationKey, frameType(from, to, sprite));
    }

    public SpriteAnimationTransition(int from, int to, Sprite sprite, String animationKey, FrameType frameType) {
        super(sprite.getSpritesheet().createAnimation(animationKey).getDurationSeconds(), Arrays.asList(new CoordinatesLock(from), new CoordinatesLock(to)));
        int toX = Coordinates.x(to);
        int fromX = Coordinates.x(from);
        int toY = Coordinates.y(to);
        int fromY = Coordinates.y(from);
        this.frameType = frameType;
        this.from = new Vector2f(fromX, fromY);
        this.to = new Vector2f(toX, toY);
        this.sprite = sprite;
        this.animationKey = animationKey;
    }

    public static FrameType frameType(int from, int to, Sprite sprite) {
        int toX = Coordinates.x(to);
        int fromX = Coordinates.x(from);
        int toY = Coordinates.y(to);
        int fromY = Coordinates.y(from);
        int dx = toX - fromX;
        int dy = toY - fromY;
        if (dx > 0) {
            return FrameType.NORTH_WEST;
        } else if (dx < 0) {
            return FrameType.SOUTH_EAST;
        } else if (dy > 0) {
            return FrameType.NORTH_EAST;
        } else if (dy < 0) {
            return FrameType.SOUTH_WEST;
        } else {
            return sprite.getFrameType();
        }
    }

    @Override
    public void start(TransitionTime time) {
        super.start(time);
        sprite.getNode().setLocalTranslation(toWorldCoodinates(from));
        sprite.setFrameType(frameType);
        sprite.setAnimation(animationKey);
    }

    @Override
    public void update(TransitionTime current) {
        float progress = current.progress(start, getEnd());
        Vector2f coordinates = new Vector2f().interpolateLocal(from, to, progress);
        sprite.getNode().setLocalTranslation(toWorldCoodinates(coordinates));
        sprite.setAnimationProgress(progress);
    }

    @Override
    public void end() {
        sprite.getNode().setLocalTranslation(toWorldCoodinates(to));
        sprite.setAnimationProgress(1);
    }

    private static Vector3f toWorldCoodinates(Vector2f tileCoordinates) {
        return new Vector3f(tileCoordinates.x + 0.5f, 0, tileCoordinates.y + 0.5f);
    }
}
