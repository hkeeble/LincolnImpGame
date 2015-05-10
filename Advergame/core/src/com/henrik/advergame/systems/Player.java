package com.henrik.advergame.systems;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.AnimationFactory;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.components.HealthComponent;
import com.henrik.advergame.components.PlayerController;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.HUD;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * The player system. A collection of components derived from a physical entity that run the player.
 */
public class Player extends PhysicalEntity {

    PlayerController controllerComponent;
    SpriteDecalGraphicsComponent graphicsComponent;
    HealthComponent healthComponent;

    public Player(GameWorld world, HUD hud, AssetManager assetManager, InputHandler input) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.8f)), CollisionTags.PLAYER));

        controllerComponent = new PlayerController(hud, world, input);
        graphicsComponent = new SpriteDecalGraphicsComponent(2f, 2f, AnimationFactory.createPlayerSet(assetManager.get("sprites/player.png", Texture.class)));

        healthComponent = new HealthComponent(3, 3000);
    }

    public PlayerController getControllerComponent() {
        return controllerComponent;
    }

    public SpriteDecalGraphicsComponent getGraphicsComponent() { return graphicsComponent; }

    public void update(GameWorld world) {
        super.update(world);
        controllerComponent.update(this, world, graphicsComponent);
        healthComponent.update();

        if(!healthComponent.canBeHit())
            graphicsComponent.setTint(Color.RED);
        else
            graphicsComponent.setTint(Color.WHITE);
    }

    public void render(GameWorld world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
        healthComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    public void damage(int amount) {
        healthComponent.damage(amount);
    }

    public void restore(int amount) {
        healthComponent.restore(amount);
    }

    public boolean canBeHit() {
        return healthComponent.canBeHit();
    }

    public boolean isDead() {
        return healthComponent.isDead();
    }

    public void fillHealth() {
        healthComponent.refill();
    }

    @Override
    public void dispose() {
        physicsComponent.dispose();
        controllerComponent.dispose();
    }

}
