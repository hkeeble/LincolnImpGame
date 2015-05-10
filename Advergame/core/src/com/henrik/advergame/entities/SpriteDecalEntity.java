package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;

/**
 * A simple {@link com.henrik.gdxFramework.entities.GameObject} that plays an animation.
 */
public class SpriteDecalEntity extends GameObject {

    SpriteDecalGraphicsComponent graphicsComponent;

    public SpriteDecalEntity(SpriteDecalGraphicsComponent graphicsComponent) {
        this.graphicsComponent = graphicsComponent;
    }

    public void update(World world) {
        super.update(world);
    }

    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    @Override
    public void dispose() {
    }
}
