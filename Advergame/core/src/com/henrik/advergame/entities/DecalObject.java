package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;

/**
 * Created by Henri on 05/02/2015.
 */
public class DecalObject extends GameObject {

    DecalGraphicsComponent graphicsComponent;

    public DecalObject(DecalGraphicsComponent graphicsComponent) {
        this.graphicsComponent = graphicsComponent;
    }

    @Override
    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    @Override
    public void update(World world) {
        super.update(world);
    }

    @Override
    public void dispose() {

    }
}
