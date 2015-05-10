package com.henrik.advergame.entities;

import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A {@link com.henrik.gdxFramework.entities.GameObject} that has a physical component, and is able to collide with other physical components in the same world.
 */
public class PhysicalEntity extends GameObject {

    protected PhysicsComponent physicsComponent;

    public PhysicalEntity(PhysicsComponent physicsComponent) {
        super();
        this.physicsComponent = physicsComponent;
    }

    public void update(GameWorld world) {
        physicsComponent.update(this, world);
        super.update(world);
    }

    public void render(World world) {
        super.render(world);
    }

    public PhysicsComponent getPhysicsComponent() {
        return physicsComponent;
    }

    public void dispose() {
        physicsComponent.dispose();
    }
}
