package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A {@link com.henrik.advergame.entities.PhysicalEntity} that is represented by a 3D model.
 */
public class ModelEntity extends PhysicalEntity {
    ModelGraphicsComponent graphicsComponent;

    public ModelEntity(ModelGraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
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

    public void dispose() {
        physicsComponent.dispose();
    }
}
