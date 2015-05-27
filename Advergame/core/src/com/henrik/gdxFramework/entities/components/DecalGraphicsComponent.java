package com.henrik.gdxFramework.entities.components;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 21/11/2014.
 */
public class DecalGraphicsComponent {

    protected boolean billboard;

    protected Decal decal;

    protected BoundingBox boundingBox;
    protected BoundingBox trnBoundingBox;
    protected Vector3 min, max;

    protected Vector3 renderOffset;
    protected Vector3 position;
    protected Vector2 dimensions;

    protected float xRotation;

    private TextureRegion textureRegion;
    
    boolean inView; // Whether or not this entity is within view

    public DecalGraphicsComponent() {
        decal = new Decal();
        boundingBox = null;
        renderOffset = new Vector3(0,0,0);
        xRotation = 0;
        billboard = false;
        
        textureRegion = new TextureRegion();

        boundingBox = new BoundingBox();
        trnBoundingBox = new BoundingBox();
        min = new Vector3();
        max = new Vector3();
        position = new Vector3();
        dimensions = new Vector2();

        decal.setColor(1, 1, 1, 1);
        decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    public DecalGraphicsComponent(float width, float height, TextureRegion textureRegion) {
        this();
        decal = Decal.newDecal(width, height, textureRegion, true);

        calculateBoundingBox();

        this.billboard = true;
    }

    public DecalGraphicsComponent(float width, float height, TextureRegion texture, boolean billboard) {
        this(width, height, texture);
        this.billboard = billboard;
    }

    public DecalGraphicsComponent(float width, float height, TextureRegion texture, boolean billboard, float xRotation) {
        this(width, height, texture, billboard);
        this.xRotation = xRotation;
    }

    private void calculateBoundingBox() {
        position.set(decal.getPosition());
        dimensions.set(decal.getWidth(), decal.getHeight());

        max.set(position.x-(dimensions.x/2), position.y-(dimensions.y/2), position.z-1);
        min.set(position.x+(dimensions.x/2), position.y+(dimensions.y/2), position.z+1);

        boundingBox.set(min, max);
    }

    public void setTint(Color color) { decal.setColor(color); }

    public void render(Camera camera, Renderer renderer, GameObject object) {

        // Calculate the current bound box from original and the object position
        boundingBox.getMin(min);
        boundingBox.getMax(max);
        trnBoundingBox.set(min.add(object.getPosition()), max.add(object.getPosition()));

        // Only render if in view
        if(camera.frustum.boundsInFrustum(trnBoundingBox)) {

            if(billboard) {
                decal.lookAt(camera.position, camera.up);
            } else {
                decal.setRotationX(xRotation);
            }

            object.getPosition(position);
            position.add(renderOffset);
            decal.setPosition(position);

            renderer.addDecal(decal);
            inView = true;
        } else {
            inView = false;
        }
    }

    public void setXRotation(float rotation) { xRotation = rotation; }

    public void setTexture(TextureRegion texture) { decal.setTextureRegion(texture); }

    public void setSize(float width, float height) { decal.setDimensions(width, height); }

    public void setBillboard(boolean billboard) { this.billboard = billboard; }

    public boolean isInView() { return inView; }
}
