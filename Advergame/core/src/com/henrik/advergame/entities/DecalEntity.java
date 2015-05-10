package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A {@link com.henrik.advergame.entities.PhysicalEntity} that is represented by a static decal.
 */
public class DecalEntity extends PhysicalEntity {

    DecalGraphicsComponent graphicsComponent;

    public DecalEntity(DecalGraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
        super(physicsComponent);
        this.graphicsComponent = graphicsComponent;
    }

    public void update(World world) {
        super.update(world);
    }

    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getMainCamera(), world.getRenderer(), this);
    }

    public DecalGraphicsComponent getGraphicsComponent() {
        return graphicsComponent;
    }
}
