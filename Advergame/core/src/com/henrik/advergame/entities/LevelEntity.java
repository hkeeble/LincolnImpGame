package com.henrik.advergame.entities;

import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A dynamic level entity.
 */
public class LevelEntity extends PhysicalEntity {

    private boolean markedForRemoval;

    public LevelEntity(PhysicsComponent physicsComponent) {
        super(physicsComponent);

        markedForRemoval = false;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    public void setMarkedForRemoval(boolean markedForRemoval) {
        this.markedForRemoval = markedForRemoval;
    }

    @Override
    public void update(GameWorld world) {
        super.update(world);
    }

    @Override
    public void render(World world) {
        super.render(world);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
