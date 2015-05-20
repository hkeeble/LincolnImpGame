package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 11/02/2015.
 */
public class TimedMessage extends MessageSequence {

    private long millisecondsToShow;
    private long timeAtStart;
    private GameObject attachedObject;

    public TimedMessage(String text, BitmapFont font, Color textColor, TextureRegion background, long millisecondsToShow, Vector3 position, Renderer renderer) {
        super(new String[] { text }, font ,textColor, background, renderer);

        this.millisecondsToShow = millisecondsToShow;
        setPosition(position);

        this.attachedObject = null;
    }

    public TimedMessage(String text, BitmapFont font, Color textColor, TextureRegion background, long millisecondsToShow, Vector3 position, Renderer renderer, GameObject attachedObject) {
        this(text, font, textColor, background, millisecondsToShow, position, renderer);

        this.attachedObject = attachedObject;
    }

    @Override
    public void show() {
        super.show();
        timeAtStart = System.currentTimeMillis();
    }

    @Override
    public void render(GameWorld world) {
        super.render(world);
    }

    @Override
    public void update(InputHandler input, World world) {
        if(active) {
            if(TimeUtils.timeSinceMillis(timeAtStart) > millisecondsToShow) {
                stop();
            }
        }

        if(this.attachedObject != null) {
            setPosition(attachedObject.getPosition().add(0, 1f, 0));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
