package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * A timed message attached to a target game object, that will move relative to the target.
 */
public class AttachedTimedMessage extends TimedMessage {

    private GameObject target;
    private Vector3 currentPosition;
    private Vector3 relativePosition;

    public AttachedTimedMessage(String text, BitmapFont font, Color textColor, TextureRegion background, long millisecondsToShow, GameObject target, Vector3 relativePosition, Renderer renderer) {
        super(text, font, textColor, background, millisecondsToShow, target.getPosition(), renderer);

        this.currentPosition = new Vector3(target.getPosition()).add(relativePosition);
        this.relativePosition = relativePosition;
        this.target = target;
    }

    @Override
    public void update(InputHandler input, World world) {
        super.update(input, world);

        if(active) {
            currentPosition.set(target.getPosition()).add(relativePosition);
            setPosition(currentPosition);
        }
    }
}
