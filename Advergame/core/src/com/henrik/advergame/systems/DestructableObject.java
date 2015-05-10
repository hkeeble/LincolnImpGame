package com.henrik.advergame.systems;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.utils.RandomUtils;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.components.HealthComponent;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 06/02/2015.
 */
public class DestructableObject extends LevelEntity {

    DecalGraphicsComponent graphicsComponent;
    HealthComponent healthComponent;

    public DestructableObject(AssetManager assetManager, Vector3 initialPos) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.8f, 1.5f, 0.4f)), CollisionTags.OBJECT));

        setPosition(initialPos);

        // Randomly determine texture to use
        int i = RandomUtils.randInt(0, 1);
        if(i == 0)
            graphicsComponent = new DecalGraphicsComponent(2, 2, new TextureRegion(assetManager.get("sprites/table.png", Texture.class)), false);
        else
            graphicsComponent = new DecalGraphicsComponent(2, 2, new TextureRegion(assetManager.get("sprites/chest.png", Texture.class)), false);

        graphicsComponent.setXRotation(-45);

        healthComponent = new HealthComponent(2, 500);
    }

    @Override
    public void update(GameWorld world) {
        super.update(world);
        healthComponent.update();

        if(healthComponent.canBeHit()) {
            graphicsComponent.setTint(Color.WHITE);
        } else {
            graphicsComponent.setTint(Color.RED);
        }
    }

    @Override
    public void render(World world) {
        super.render(world);
        healthComponent.render(world.getCamera(), world.getRenderer(), this);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    public void damage(int amount) {
        healthComponent.damage(amount);
    }

    public boolean canBeHit() {
        return healthComponent.canBeHit();
    }

    public boolean isDestroyed() {
        return healthComponent.isDead();
    }

    public DecalGraphicsComponent getGraphicsComponent() { return graphicsComponent; }

}
