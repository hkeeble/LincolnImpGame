package com.henrik.advergame.components;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.AnimationTypePlayer;
import com.henrik.advergame.Game;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.Direction;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.gdxFramework.core.HUD;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A component that controls an object through the use of a touchpad.
 */
public class PlayerController extends ControllerComponent {
    private Touchpad touchpad;
    private Touchpad.TouchpadStyle touchpadStyle;
    private Skin touchpadSkin;
    private Drawable padBackground, padKnob, hitButton;

    private Button buttonHit;

    private Vector3 velocity;

    private final float MAX_MOVE_SPEED = 0.1f;
    private final float MIN_MOVE_SPEED = 0.05f;
    private final float SPEED_INCREMENT = 0.05f;
    private final long MILLIS_BETWEEN_SPEED_UP = 200;
    private float currentMoveSpeed;
    private long millisAtLastSpeedUp;

    private GameWorld world; // Holds reference to world, so that it can manage the 'hit' object.

    private boolean hitActive;
    private float timeHitActive;
    private PhysicalEntity hitEntity; // The physical entity for the hit object

    private HUD hud; // Reference to the game's hud.

    private InputEvent fakeInputEvent; // Used for popup touchpad
    private boolean analogActive;
    private InputHandler input; // Reference to the input handler

    private Direction direction;

    private class ButtonListener extends ClickListener {
        protected Vector3 objectPosition = new Vector3();
        protected Direction direction;
        
        public void update(Vector3 position, Direction direction) {
        	objectPosition.set(position);
        	this.direction = direction;
        }
        
        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            return super.touchDown(event, x, y, pointer, button);
        }
    }

    private class HitButtonListener extends ButtonListener {
        
    	private Vector3 position = new Vector3(0,0,0);
    	
    	@Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {

            if(!hitActive) {
                // Place the object based on player position and the direction that they currently face
                position.set(objectPosition);
                if (direction.getCurrent() == Direction.DOWN)
                    position.add(0, 0, 0.5f);
                else if (direction.getCurrent() == Direction.UP)
                    position.add(0, 0, -0.5f);
                else if (direction.getCurrent() == Direction.LEFT)
                    position.add(-0.5f, 0, 0);
                else if (direction.getCurrent() == Direction.RIGHT)
                    position.add(0.5f, 0, 0);

                hitEntity.setPosition(position);

                // Update the physics component once, to reposition before registering with collision world
                hitEntity.getPhysicsComponent().update(hitEntity, world);

                hitActive = true;
                timeHitActive = 0;

                world.registerDynamicEntity(hitEntity.getPhysicsComponent(), hitEntity);
            }

            return true;
        }
    }

    private HitButtonListener hitListener;

    public PlayerController(HUD hud, GameWorld world, InputHandler input) {
        super();

        direction = new Direction();
        
        touchpadStyle = new Touchpad.TouchpadStyle();

        this.padBackground = Game.getUISkin().getDrawable("touchBackground");
        this.padKnob = Game.getUISkin().getDrawable("touchKnob");
        this.hitButton = Game.getUISkin().getDrawable("hitButton");

        // Create touchpad
        touchpadStyle.background = this.padBackground;
        touchpadStyle.knob = this.padKnob;
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(0, 0, 64, 64);

        hitListener = new HitButtonListener();

        // Create buttons
        buttonHit = HUD.makeButton(0, 0, hitListener, hitButton, hitButton, hitButton);
        buttonHit.setPosition(HUD.WIDTH - 135, 0);

        hud.addActor(touchpad);
        hud.addActor(buttonHit);

        velocity = new Vector3(0,0,0);

        hitEntity = new PhysicalEntity(new PhysicsComponent(new btBoxShape(new Vector3(1f, 1.5f, 0.4f)), CollisionTags.PLAYER_ACTION_BOX));

        fakeInputEvent = new InputEvent();
        fakeInputEvent.setType(InputEvent.Type.touchDown);

        this.hud = hud;
        this.world = world;
        this.input = input;
    }

    public void activateHUD() {
        hud.addActor(buttonHit);
        hud.addActor(touchpad);
    }

    public void deactivateHUD() {
        hud.getActors().removeValue(buttonHit, true);
        hud.getActors().removeValue(touchpad, true);
    }

    @Override
    public void update(GameObject object, GameWorld world, SpriteDecalGraphicsComponent graphicsComponent) {

        // Update movement based on input
        if(input.isTouched()) {
            enableAnalog(input.getPosition());
        } else {
            disableAnalog();
        }

        if(!hitActive) {
            // Set velocity based on current control inputs
            velocity.set(-touchpad.getKnobPercentX() * currentMoveSpeed, 0, touchpad.getKnobPercentY() * currentMoveSpeed);

            if (isMoving(velocity)) {
                if (TimeUtils.timeSinceMillis(millisAtLastSpeedUp) > MILLIS_BETWEEN_SPEED_UP) {
                    currentMoveSpeed += SPEED_INCREMENT;
                    if (currentMoveSpeed > MAX_MOVE_SPEED) {
                        currentMoveSpeed = MAX_MOVE_SPEED;
                    }
                    millisAtLastSpeedUp = System.currentTimeMillis();
                }
            } else {
                currentMoveSpeed = MIN_MOVE_SPEED;
                millisAtLastSpeedUp = System.currentTimeMillis();
            }
        } else {
            velocity.set(0, 0, 0);
            currentMoveSpeed = MIN_MOVE_SPEED;
            millisAtLastSpeedUp = System.currentTimeMillis();
        }

        object.setVelocity(velocity);
        direction.update(velocity); // Update current direction for use by button responses

        // Update listeners
        hitListener.update(object.getPosition(), direction);

        // If the hit entity is active, render it
        if(hitActive) {
            if(graphicsComponent.isAnimationFinished()) {
                hitActive = false;
                world.unregisterCollisionObject(hitEntity.getPhysicsComponent());
            }
            hitEntity.update(world);
        }

        // Update the animation component
        updateAnimation(object, graphicsComponent);

        // Update base controller object
        super.update(object, world, graphicsComponent);
    }

    /**
     * Handles animation updates for the player controller
     * @param object
     * @param graphicsComponent
     */
    private void updateAnimation(GameObject object, SpriteDecalGraphicsComponent graphicsComponent) {
        if(!hitActive) {
            if(direction.getCurrent() == Direction.UP) {
                graphicsComponent.setAnimation(AnimationTypePlayer.WalkUp);
            }
            if(direction.getCurrent() == Direction.DOWN) {
                graphicsComponent.setAnimation(AnimationTypePlayer.WalkDown);
            }
            if(direction.getCurrent() == Direction.LEFT) {
                graphicsComponent.setAnimation(AnimationTypePlayer.WalkLeft);
            }
            if(direction.getCurrent() == Direction.RIGHT) {
                graphicsComponent.setAnimation(AnimationTypePlayer.WalkRight);
            }

            if(!isMoving(object.getVelocity())) {
                graphicsComponent.pause();
                graphicsComponent.reset();
            } else if(!graphicsComponent.isPlaying()) {
                graphicsComponent.start();
            }

        } else {
            if(direction.getCurrent() == Direction.UP) {
                graphicsComponent.setAnimation(AnimationTypePlayer.AttackUp);
            }
            if(direction.getCurrent() == Direction.DOWN) {
                graphicsComponent.setAnimation(AnimationTypePlayer.AttackDown);
            }
            if(direction.getCurrent() == Direction.LEFT) {
                graphicsComponent.setAnimation(AnimationTypePlayer.AttackLeft);
            }
            if(direction.getCurrent() == Direction.RIGHT) {
                graphicsComponent.setAnimation(AnimationTypePlayer.AttackRight);
            }

            if(!graphicsComponent.isPlaying()) {
                graphicsComponent.start();
            }
        }
    }

    private void enableAnalog(Vector2 touchPos) {

        if(!analogActive) {
            Vector3 unprojected = hud.getCamera().unproject(new Vector3(touchPos.x, touchPos.y, 0));
            unprojected.set(unprojected.x - (touchpad.getWidth() / 2), unprojected.y - (touchpad.getHeight() / 2), 0);

            if (unprojected.x < (HUD.WIDTH / 3) && unprojected.y < (HUD.HEIGHT / 3)) {
                analogActive = true;
                touchpad.setVisible(true);
                touchpad.setPosition(unprojected.x, unprojected.y);

                fakeInputEvent.setStage(touchpad.getStage());
                fakeInputEvent.setStageX(unprojected.x);
                fakeInputEvent.setStageY(unprojected.y);

                touchpad.fire(fakeInputEvent);
            }
        }
    }

    private void disableAnalog() {
        analogActive = false;
        touchpad.setVisible(false);
    }

    public void reset() {
        hitActive = false;
    }

    private boolean isMoving(Vector3 velocity) {
        return (velocity.x + velocity.y + velocity.z != 0);
    }

    @Override
    public void dispose() {
        hitEntity.dispose();
    }
}