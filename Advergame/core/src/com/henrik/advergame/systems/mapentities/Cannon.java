package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.Game;
import com.henrik.advergame.entities.AnimatedDecalObject;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.entities.SpriteDecalEntity;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.systems.CannonBall;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.Direction;
import com.henrik.advergame.utils.Point;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.AnimationUtils;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.AnimatedDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 10/03/2015.
 */
public class Cannon extends LevelEntity {

    private AnimatedDecalGraphicsComponent graphicsComponent;
    private Point fireDirection;

    private ArrayList<CannonBall> cannonBalls;
    private final Pool<CannonBall> bulletPool = new Pool<CannonBall>() {
        @Override
        protected CannonBall newObject() {
            return new CannonBall();
        }
    };

    private Animation explosionAnimation;

    private Texture cannonballTexture;
    private Vector3 direction;

    public Cannon(Vector3 position, Point fireDirection, AssetManager assetManager) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.4f)), CollisionTags.CANNON));

        Texture texture = assetManager.get("sprites/cannonAnimation.png", Texture.class);

        graphicsComponent = new AnimatedDecalGraphicsComponent(2f, 2f, AnimationUtils.createAnimation(texture, 128, 96, 0.3f, Animation.PlayMode.NORMAL));
        graphicsComponent.start();

        this.fireDirection = fireDirection;

        Direction dir = new Direction();
        dir.update(new Vector3(fireDirection.x, 0, fireDirection.y));
        if(dir.getCurrent() == Direction.DOWN) {
            cannonballTexture = assetManager.get("sprites/fire_ball_down.png", Texture.class);
        } else if(dir.getCurrent() == Direction.UP) {
            cannonballTexture = assetManager.get("sprites/fire_ball_up.png", Texture.class);
        } else if(dir.getCurrent() == Direction.LEFT) {
            cannonballTexture = assetManager.get("sprites/fire_ball_left.png", Texture.class);
        } else if(dir.getCurrent() == Direction.RIGHT) {
            cannonballTexture = assetManager.get("sprites/fire_ball_right.png", Texture.class);
        }


        setPosition(position);

        // Create explosion animation
        explosionAnimation = AnimationUtils.createAnimation(assetManager.get("sprites/explosion.png", Texture.class), 64, 64, 0.05f, Animation.PlayMode.NORMAL);

        cannonBalls = new ArrayList<CannonBall>();
        direction = new Vector3();
    }

    @Override
    public void update(GameWorld world) {
        super.update(world);

        // Check for countdown to add new cannonball
        if(graphicsComponent.isAnimationFinished()) {
            CannonBall ball = bulletPool.obtain();
            direction.set(fireDirection.x, 0, fireDirection.y);
            ball.init(cannonballTexture, this.getPosition(), direction);
            cannonBalls.add(ball);

            world.registerDynamicEntity(ball.getPhysicsComponent(), ball);
            graphicsComponent.reset();
            graphicsComponent.start();
        }

        for(CannonBall cannonBall : cannonBalls) {
            cannonBall.update(world);
        }

        // Check for removals
        for(int i = 0; i < cannonBalls.size(); i++) {
            if(cannonBalls.get(i).isMarkedForRemoval()) {

                CannonBall ball = cannonBalls.get(i);

                // Remove cannonball
                world.unregisterCollisionObject(ball.getPhysicsComponent());
                cannonBalls.remove(ball);
                bulletPool.free(ball);
            }
        }
    }

    @Override
    public void render(World world) {
        super.render(world);

        graphicsComponent.render(world.getCamera(), world.getRenderer(), this);

        for(CannonBall cannonBall : cannonBalls) {
            cannonBall.render(world);
        }
    }

    @Override
    public void dispose() {
        for(CannonBall ball : cannonBalls) {
            ball.dispose();
        }

        int count = bulletPool.getFree();
        for(int i = 0; i < count; i++) {
            CannonBall ball = bulletPool.obtain();
            ball.dispose();
        }
    }
}
