package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 27/02/2015.
 */
public class Door extends LevelEntity {

    private ModelGraphicsComponent graphicsComponent;

    public Door(Vector3 position, Model model, boolean keyDoor) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(5f, 5f, 5f)), CollisionTags.DOOR));
        graphicsComponent = new ModelGraphicsComponent(new ModelInstance(model));

        CollisionTags tag = CollisionTags.DOOR;
        if(!keyDoor)
            tag = CollisionTags.SWITCH_DOOR;

        // Re-create physics component based on bounding box
        BoundingBox bb = new BoundingBox();
        model.calculateBoundingBox(bb);
        physicsComponent.dispose();
        physicsComponent = new PhysicsComponent(new btBoxShape(new Vector3(bb.getWidth()/2, bb.getHeight()/2, bb.getDepth()/2)), tag);
        setPosition(position);
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
        super.dispose();
    }
}