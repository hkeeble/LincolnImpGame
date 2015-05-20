package com.henrik.advergame.systems.mapentities;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.AnimationFactory;
import com.henrik.advergame.Game;
import com.henrik.advergame.components.AngelDogController;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.systems.TimedMessage;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.Point;
import com.henrik.advergame.utils.RandomUtils;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 25/02/2015.
 */
public class AngelDog extends LevelEntity {

    private AngelDogController controllerComponent;
    private SpriteDecalGraphicsComponent graphicsComponent;

    private TimedMessage currentMessage;
    private final static String[] MESSAGE_STRINGS = { "*woof*", "*arf*", "*woof! woof!*", "*arf! arf!*", "*woof?*", "*ARF!*", "*WOOF!*", "*arf?*"};
    
    // Load static texture and font
    private static BitmapFont messageFont = Game.getFont("main", 3);
    private static TextureRegion messageBackground = Game.getUISkin().getRegion("speechBubble");
    
    // Timed Messages are static, as they must render to texture to create them
    private TimedMessage[] timedMessages;
    
    private long millisToNextMessage;
    private long millisAtLastMessage;

    private final long MIN_MILLIS_TO_NEXT_MESSAGE = 2500;
    private final long MAX_MILLITS_TO_NEXT_MESSAGE = 5500;

    public AngelDog(AssetManager assetManager, Vector3 initialPos, ArrayList<Point> wayPoints, Renderer renderer) {
        super(new PhysicsComponent(new btBoxShape(new Vector3(0.6f, 1.5f, 0.4f)), CollisionTags.ANGEL_DOG));

        timedMessages = new TimedMessage[] {
                    new TimedMessage(MESSAGE_STRINGS[0], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[1], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[2], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[3], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[4], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[5], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[6], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this),
                    new TimedMessage(MESSAGE_STRINGS[7], messageFont, Color.BLACK, messageBackground, 1500, new Vector3(0, 0, 0), renderer, this)
        };

        controllerComponent = new AngelDogController(initialPos, wayPoints);
        graphicsComponent = new SpriteDecalGraphicsComponent(2, 2, AnimationFactory.createMoveSet(assetManager.get("sprites/dog.png", Texture.class), 128, 160, 0.6f));

        setPosition(initialPos);

        currentMessage = null;
        millisAtLastMessage = System.currentTimeMillis();

        millisToNextMessage = (long)RandomUtils.randInt((int)MIN_MILLIS_TO_NEXT_MESSAGE, (int)MAX_MILLITS_TO_NEXT_MESSAGE);
    }

    @Override
    public void update(GameWorld world) {
        controllerComponent.update(this, world, graphicsComponent);

        if(TimeUtils.timeSinceMillis(millisAtLastMessage) > millisToNextMessage) {
            if(currentMessage != null)
                currentMessage.dispose();
            world.addMessage(timedMessages[RandomUtils.randInt(0, timedMessages.length-1)], this);
            millisAtLastMessage = System.currentTimeMillis();
            millisToNextMessage = (long)RandomUtils.randInt((int)MIN_MILLIS_TO_NEXT_MESSAGE, (int)MAX_MILLITS_TO_NEXT_MESSAGE);
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
        if(currentMessage != null)
            currentMessage.dispose();

        for(int i = 0; i < timedMessages.length; i++) {
        	timedMessages[i].dispose();
        }
        
        super.dispose();
    }

    public void reverseDirection() {
        controllerComponent.reverseDirection();
    }
}
