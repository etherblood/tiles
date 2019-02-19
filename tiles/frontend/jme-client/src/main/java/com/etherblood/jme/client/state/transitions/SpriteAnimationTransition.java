package com.etherblood.jme.client.state.transitions;

import com.etherblood.jme.client.sprites.Sprite;
import com.etherblood.jme.client.sprites.data.FrameType;
import com.etherblood.jme.client.state.AbstractStateTransition;
import com.etherblood.jme.client.state.TransitionTime;
import com.etherblood.jme.client.state.locks.CoordinatesLock;
import com.etherblood.rules.util.Coordinates;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import java.util.Arrays;

public class SpriteAnimationTransition extends AbstractStateTransition {

    private final Vector2f from, to;
    private final Sprite sprite;
    private final FrameType frameType;
    private final String animationKey;

    public SpriteAnimationTransition(int from, int to, Sprite sprite, String animationKey) {
        super(sprite.getSpritesheet().createAnimation(animationKey).getDurationSeconds(), Arrays.asList(new CoordinatesLock(from), new CoordinatesLock(to)));
        int toX = Coordinates.x(to);
        int fromX = Coordinates.x(from);
        int toY = Coordinates.y(to);
        int fromY = Coordinates.y(from);
        int dx = toX - fromX;
        int dy = toY - fromY;
        if (dx > 0) {
            this.frameType = FrameType.NORTH_WEST;
        } else if (dx < 0) {
            this.frameType = FrameType.SOUTH_EAST;
        } else if (dy > 0) {
            this.frameType = FrameType.NORTH_EAST;
        } else if (dy < 0) {
            this.frameType = FrameType.SOUTH_WEST;
        } else {
            this.frameType = sprite.getFrameType();
        }
        this.from = new Vector2f(fromX, fromY);
        this.to = new Vector2f(toX, toY);
        this.sprite = sprite;
        this.animationKey = animationKey;
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
