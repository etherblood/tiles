package com.etherblood.jme.client.sprites;

import com.etherblood.jme.client.sprites.data.FrameType;
import com.etherblood.jme.client.sprites.data.SpritesheetFrame;
import com.jme3.math.FastMath;
import com.jme3.math.Vector2f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import java.util.concurrent.ThreadLocalRandom;

public class Sprite {

    private AnimationInstance currentAnimation = null;
    private final AnimationInstance defaultAnimation;
    private final SpritesheetInstance spritesheet;
    private final SpriteMesh mesh;
    private final Geometry geometry;
    private final Node node;
    private FrameType frameType;

    public Sprite(SpritesheetInstance spritesheet, FrameType frameType) {
        this.spritesheet = spritesheet;
        this.frameType = frameType;
        mesh = new SpriteMesh();
        node = new Node("node(sprite - " + spritesheet.getSourceImagePath() + ")");
        defaultAnimation = spritesheet.createAnimation(spritesheet.getDefaultAnimationKey());
        defaultAnimation.setProgress(ThreadLocalRandom.current().nextFloat());
        geometry = new Geometry("geometry(sprite - " + spritesheet.getSourceImagePath() + ")", mesh);
        geometry.rotate(-FastMath.HALF_PI / 3, 5 * FastMath.QUARTER_PI, 0);
        geometry.scale((float) Math.sqrt(2));
        geometry.scale(spritesheet.getScale());
        geometry.setQueueBucket(Bucket.Transparent);
        geometry.setMaterial(spritesheet.getMaterial());
        node.attachChild(geometry);
        updateMesh();
    }

    public void update(float seconds) {
        if (!hasCurrentAnimation()) {
            float next = defaultAnimation.getProgress() + seconds / defaultAnimation.getDurationSeconds();
            defaultAnimation.setProgress(next % 1f);
            updateMesh();
        }
    }

    private boolean hasCurrentAnimation() {
        return currentAnimation != null && !currentAnimation.hasEnded();
    }

    public boolean isAnimationSupported(String key) {
        return spritesheet.getAnimations().contains(key);
    }

    public void setAnimation(String key) {
        currentAnimation = spritesheet.createAnimation(key);
        updateMesh();
    }

    public void setAnimationProgress(float progress) {
        currentAnimation.setProgress(progress);
        updateMesh();
    }

    private void updateMesh() {
        String frameKey = hasCurrentAnimation() ? currentAnimation.getCurrentFrameKey() : defaultAnimation.getCurrentFrameKey();
        SpritesheetFrame frame = spritesheet.getFrame(frameKey, frameType);
        Vector2f uvScale = spritesheet.getUvScale();
        Vector2f upperLeftUv = frame.upperLeftUv;
        Vector2f lowerRightUv = frame.lowerRightUv;
        mesh.updateTexCoords(upperLeftUv.x * uvScale.x, lowerRightUv.y * uvScale.y, lowerRightUv.x * uvScale.x, upperLeftUv.y * uvScale.y);
        Vector2f pivot = frame.pivot;
        Vector2f size = frame.size;
        mesh.updatePivot(pivot.x, pivot.y, size.x, size.y);
    }

    public Node getNode() {
        return node;
    }

    public FrameType getFrameType() {
        return frameType;
    }

    public void setFrameType(FrameType frameType) {
        if (this.frameType != frameType) {
            this.frameType = frameType;
            updateMesh();
        }
    }

    public SpritesheetInstance getSpritesheet() {
        return spritesheet;
    }

}
