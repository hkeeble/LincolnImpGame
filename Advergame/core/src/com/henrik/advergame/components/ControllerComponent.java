package com.henrik.advergame.components;

import com.badlogic.gdx.math.Vector3;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;

/**
 * Created by Henri on 18/11/2014.
 */
public abstract class ControllerComponent {
    private Vector3 lastDirection;

    public ControllerComponent() {
        lastDirection = new Vector3(0,0,0);
    }

    public void update(GameObject object, GameWorld world, SpriteDecalGraphicsComponent graphicsComponent) {
        Vector3 vel = object.getVelocity();
        float total = vel.x + vel.y + vel.z;

        if(total != 0) {
            lastDirection.set(object.getVelocity());
            lastDirection.nor();
        }
    }

    /**
     * Retrieve the last move direction of this entity.
     */
    public Vector3 getLastDirection() { return lastDirection; }

    public abstract void dispose();
}
