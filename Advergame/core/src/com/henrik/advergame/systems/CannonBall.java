package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Pool;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 10/03/2015.
 */
public class CannonBall extends PhysicalEntity implements Pool.Poolable {

    private DecalGraphicsComponent graphicsComponent;

    private boolean markedForRemoval;
    private final float MOVE_SPEED = 12f;
    private Vector3 velocityPerFrame;

    public CannonBall() {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.1f, 0.1f, 0.1f)), CollisionTags.CANNON_BALL));

        velocityPerFrame = new Vector3();
        graphicsComponent = new DecalGraphicsComponent();
    }

    public void init(Texture texture, Vector3 position, Vector3 direction) {
        setPosition(position);
        velocityPerFrame.set(direction).scl(MOVE_SPEED);
        markedForRemoval = false;

        graphicsComponent.setBillboard(true);
        graphicsComponent.setTexture(new TextureRegion(texture));
        graphicsComponent.setSize(2f, 2f);
    }

    public void setDirection(float x, float y, float z) {
    	velocityPerFrame.set(x,y,z).scl(MOVE_SPEED);
    }
    
    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    @Override
    public void update(GameWorld world) {
        super.update(world);
        setVelocity(velocityPerFrame);
    }

    @Override
    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    @Override
    public void reset() {
        markedForRemoval = false;
        velocityPerFrame.set(0,0,0);
    }
}
