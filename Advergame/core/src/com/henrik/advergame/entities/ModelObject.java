package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;

/**
 * Created by Henri on 15/01/2015.
 */
public class ModelObject extends GameObject {
    ModelGraphicsComponent graphicsComponent;

    public ModelObject(ModelGraphicsComponent graphicsComponent) {
        this.graphicsComponent = graphicsComponent;
    }

    public void update(World world) {

    }

    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    public ModelGraphicsComponent getGraphicsComponent() {
        return graphicsComponent;
    }

    @Override
    public void dispose() {

    }
}
