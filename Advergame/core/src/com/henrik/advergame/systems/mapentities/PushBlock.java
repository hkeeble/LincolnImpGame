package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 10/03/2015.
 */
public class PushBlock extends LevelEntity {

    private ModelGraphicsComponent graphicsComponent;

    public PushBlock(Vector3 position, Model model, Vector3 size) {
        super(new PhysicsComponent(new btBoxShape(size.scl(0.5f)), CollisionTags.PUSH_BLOCK));

        graphicsComponent = new ModelGraphicsComponent(new ModelInstance(model));

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
