package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * A message sequence that is triggered when the player collides with it.
 */
public class TriggeredMessageSequence extends MessageSequence {

    private PhysicsComponent physicsComponent;
    private Vector3 focusLocation;

    public TriggeredMessageSequence(Vector3 location, int cellSize, String[] pagesText, BitmapFont font, Color textColor, TextureRegion background, Vector3 focusLocation) {
        super(pagesText, font, textColor, background);

        physicsComponent = new PhysicsComponent(new btBoxShape(new Vector3((float)cellSize / 2f, (float)cellSize / 2f, (float)cellSize / 2f)), CollisionTags.MESSAGE_TRIGGER);

        setPosition(location);
        physicsComponent.setTransform(getTransform());

        this.focusLocation = focusLocation;
    }

    public Vector3 getFocusLocation() {
        return focusLocation;
    }

    @Override
    public void show() {
        super.show();

        // When showing a triggered sequence, we must move it's location to it's focus location
        setPosition(focusLocation);
    }

    @Override
    public void render(GameWorld world) {
        super.render(world);
    }

    @Override
    public void update(InputHandler input, World world) {
        super.update(input, world);
        physicsComponent.update(this, world);
    }

    public PhysicsComponent getPhysicsComponent() {
        return physicsComponent;
    }

    @Override
    public void dispose() {
        physicsComponent.dispose();
        super.dispose();
    }
}
