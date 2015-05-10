package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 10/03/2015.
 */
public class Switch extends LevelEntity {

    private boolean activated;
    private ArrayList<Door> doors;
    private DecalGraphicsComponent graphicsComponent;

    private Texture onTexture;

    public Switch(Vector3 position, ArrayList<Door> doors, AssetManager assetManager) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.4f)), CollisionTags.SWITCH));
        graphicsComponent = new DecalGraphicsComponent(2f, 2f, new TextureRegion(assetManager.get("sprites/switch_off.png", Texture.class)));

        onTexture = assetManager.get("sprites/switch_on.png", Texture.class);

        setPosition(position);

        this.doors = doors;

        activated = false;
    }

    public void activate() {
        activated = true;
        graphicsComponent.setTexture(onTexture);
    }

    public boolean isActivated() {
        return activated;
    }

    public ArrayList<Door> getDoors() {
        return doors;
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

    @Override
    public void dispose() {
        for(Door door : doors) {
            door.dispose();
        }

        super.dispose();
    }
}
