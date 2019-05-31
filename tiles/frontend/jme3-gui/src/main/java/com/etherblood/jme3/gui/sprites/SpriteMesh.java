package com.etherblood.jme3.gui.sprites;

import com.jme3.bounding.BoundingSphere;
import com.jme3.math.Vector3f;
import com.jme3.scene.Mesh;
import com.jme3.scene.VertexBuffer;

public class SpriteMesh extends Mesh {

    public SpriteMesh() {
        setBuffer(VertexBuffer.Type.Index, 3, new short[]{
            0, 1, 2,
            0, 2, 3});
        setBuffer(VertexBuffer.Type.Normal, 3, new float[]{
            0, 0, 1,
            0, 0, 1,
            0, 0, 1,
            0, 0, 1});
        setBound(new BoundingSphere((float) Math.sqrt(0.5), Vector3f.ZERO));
    }

    public void updatePivot(float x, float y, float width, float height) {
        x *= width;
        y *= height;
        setBuffer(VertexBuffer.Type.Position, 3, new float[]{
            -x, y - height, 0,
            width - x, y - height, 0,
            width - x, y, 0,
            -x, y, 0
        });
    }

    public void updateTexCoords(float leftX, float lowerY, float rightX, float upperY) {
        setBuffer(VertexBuffer.Type.TexCoord, 2, new float[]{
            leftX, lowerY,
            rightX, lowerY,
            rightX, upperY,
            leftX, upperY});
    }
}
