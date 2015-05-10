package com.henrik.advergame.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.World;

/**
 * Created by Henri on 11/02/2015.
 */
public class TimedMessage extends MessageSequence {

    private long millisecondsToShow;
    private long timeAtStart;

    public TimedMessage(String text, BitmapFont font, Color textColor, TextureRegion background, long millisecondsToShow, Vector3 position) {
        super(new String[] { text }, font ,textColor, background);

        this.millisecondsToShow = millisecondsToShow;
        setPosition(position);
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
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
