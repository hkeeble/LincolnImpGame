package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.AnimatedDecalGraphicsComponent;

/**
 * Represents an object with an animated decal, that is not physical.
 */
public class AnimatedDecalObject extends GameObject {

    AnimatedDecalGraphicsComponent graphicsComponent;

    public AnimatedDecalObject(AnimatedDecalGraphicsComponent graphicsComponent) {
        super();
        this.graphicsComponent = graphicsComponent;
    }

    @Override
    public void update(World world) {
        super.update(world);
    }

    @Override
    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    public AnimatedDecalGraphicsComponent getGraphicsComponent() {
        return graphicsComponent;
    }

    @Override
    public void dispose() {

    }
}
