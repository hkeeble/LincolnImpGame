package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.AnimationFactory;
import com.henrik.advergame.components.AngelController;
import com.henrik.advergame.entities.AnimatedDecalObject;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.level.shared.entities.AngelEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.AnimationUtils;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.AnimatedDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 12/03/2015.
 */
public class Angel extends LevelEntity {

    public static final String[] SIGHTING_MESSAGES = { "\"!!!\"", "\"Hm!?\"", "\"Who's there!?\"", "\"Halt!\"", "\"You there!\"", "\"Stop right there!\"", "\"Whoa there!\"",
                                                        "\"What do we have here!?\"", "\"You shouldn't be here!\"", "\"Stop!\"", "\"Not so fast!\"", "\"Freeze!\"", "\"Intruder!\""};

    private final long TIME_UNTIL_PETRIFY = 3000;
    private long millisWhenSighted;

    private SpriteDecalGraphicsComponent graphicsComponent;
    private AngelController controllerComponent;

    private boolean playerSighted;

    public Angel(AssetManager assetManager, Vector3 initialPos, ArrayList<AngelEntity.Waypoint> wayPoints) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.4f)), CollisionTags.ANGEL));

        graphicsComponent = new SpriteDecalGraphicsComponent(2, 2, AnimationFactory.createMoveSet(assetManager.get("sprites/angel.png", Texture.class), 128, 160, 0.3f));
        controllerComponent = new AngelController(wayPoints, assetManager);

        playerSighted = false;

        setPosition(initialPos);
    }

    /**
     * Remove the angel's sight from the collision world.
     */
    public void removeSight(GameWorld world) {
        controllerComponent.removeSight(world);
    }

    public void setPlayerSighted(boolean sighted) {
        playerSighted = sighted;
        if(sighted)
            millisWhenSighted = System.currentTimeMillis();
    }

    public boolean isPlayerSighted() {
        return playerSighted;
    }

    @Override
    public void update(GameWorld world) {
        if(!playerSighted) {
            controllerComponent.update(this, world, graphicsComponent);
        } else {
            setVelocity(Vector3.Zero);
            if(TimeUtils.timeSinceMillis(millisWhenSighted) > TIME_UNTIL_PETRIFY) {
                world.getSessionManager().setGameOver(true);
            }
        }

        super.update(world);
    }

    @Override
    public void render(World world) {
        super.render(world);
        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);
    }

    @Override
    public void dispose() {
        controllerComponent.dispose();
        super.dispose();
    }
}
