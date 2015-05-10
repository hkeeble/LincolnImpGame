package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 10/03/2015.
 */
public class CannonBall extends PhysicalEntity {

    private DecalGraphicsComponent graphicsComponent;

    private boolean markedForRemoval;
    private final float MOVE_SPEED = 0.2f;
    private Vector3 velocityPerFrame;

    public CannonBall(Texture texture, Vector3 position, Vector3 direction) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.1f, 0.1f, 0.1f)), CollisionTags.CANNON_BALL));
        setPosition(position);
        velocityPerFrame = new Vector3(direction).scl(MOVE_SPEED);
        markedForRemoval = false;

        graphicsComponent = new DecalGraphicsComponent(2f, 2f, new TextureRegion(texture));
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
}
