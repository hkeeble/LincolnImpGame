package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 10/03/2015.
 */
public class Teleporter extends LevelEntity {

    private Teleporter partner;
    private DecalGraphicsComponent graphicsComponent;

    public Teleporter(Vector3 position, AssetManager assetManager) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.4f)), CollisionTags.TELEPORTER));

        graphicsComponent = new DecalGraphicsComponent(2f, 2f, new TextureRegion(assetManager.get("sprites/teleporter.png", Texture.class)));
        setPosition(position);
    }

    public void setPartner(Teleporter partner) {
        this.partner = partner;
    }

    public Teleporter getPartner() {
        return partner;
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
}
