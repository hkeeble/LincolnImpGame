package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.AnimatedDecalObject;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.AnimationUtils;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.AnimatedDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 17/03/2015.
 */
public class DestructableBlock extends LevelEntity {

    private ModelGraphicsComponent graphicsComponent;

    public DestructableBlock(Vector3 position, Model model, AssetManager assetManager) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(5f, 5f, 5f)), CollisionTags.DESTRUCTABLE_BLOCK));

        setPosition(position);
        graphicsComponent = new ModelGraphicsComponent(new ModelInstance(model));

        // Re-create physics component based on bounding box
        BoundingBox bb = new BoundingBox();
        model.calculateBoundingBox(bb);
        physicsComponent.dispose();
        physicsComponent = new PhysicsComponent(new btBoxShape(new Vector3(bb.getWidth()/2, bb.getHeight()/2, bb.getDepth()/2)), CollisionTags.DESTRUCTABLE_BLOCK);
        setPosition(position);
    }


    @Override
    public void update(GameWorld world) {
        super.update(world);
    }

    @Override
    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
