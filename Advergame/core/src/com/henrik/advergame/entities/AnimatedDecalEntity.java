package com.henrik.advergame.entities;

import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A {@link com.henrik.advergame.entities.PhysicalEntity} that is represented by an animated decal.
 */
public class AnimatedDecalEntity extends PhysicalEntity {

    SpriteDecalGraphicsComponent graphicsComponent;

    public AnimatedDecalEntity(SpriteDecalGraphicsComponent graphicsComponent, PhysicsComponent physicsComponent) {
        super(physicsComponent);
        this.graphicsComponent = graphicsComponent;
    }

    @Override
    public void update(World world) {
        super.update(world);
    }

    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    public void startAnimation() {
        graphicsComponent.start();
    }

    public void pauseAnimation() {
        graphicsComponent.pause();
    }

    public void resetAnimation() {
        graphicsComponent.reset();
    }

    public boolean isAnimationFinished() { return graphicsComponent.isAnimationFinished(); }

    public boolean isInView() { return graphicsComponent.isInView(); }

    @Override
    public void dispose() {
        super.dispose();
    }
}