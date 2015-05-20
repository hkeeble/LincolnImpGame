package com.henrik.advergame.systems;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.advergame.components.MessageComponent;
import com.henrik.gdxFramework.core.InputHandler;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * Created by Henri on 09/02/2015.
 */
public class MessageSequence extends GameObject implements Pool.Poolable {

    private MessageComponent[] pages;
    protected boolean active;
    public boolean finished;
    private int currentPage;
    private boolean touchLastFrame; // Whether or not there was a touch last frame.

    public MessageSequence(String[] pagesText, BitmapFont font, Color textColor, TextureRegion background, Renderer renderer) {
        super();

        pages = new MessageComponent[pagesText.length];

        for(int i = 0; i < pagesText.length; i++) {
            pages[i] = new MessageComponent(pagesText[i], font, textColor, background, renderer);
        }

        currentPage = 0;
        finished = false;
        active = false;
        touchLastFrame = false;
    }



    public boolean isFinished() {
        return finished;
    }

    public void show() {
        if(active == false && finished == false) {
            active = true;
            finished = false;
        }
    }

    public void stop() {
        active = false;
        finished = true;
        currentPage = 0;
    }

    public void advance() {
        currentPage++;
        if(currentPage > pages.length-1) {
            stop();
        }
    }

    @Override
    public void dispose() {
        for(int i = 0; i < pages.length; i++) {
        	pages[i].dispose();
        }
    }

    public void render(GameWorld world) {
        super.render(world);
        if(active) {
            pages[currentPage].render(world.getCamera(), world.getRenderer(), this);
        }
    }

    public void update(InputHandler input, World world) {
        super.update(world);

        if(active) {
            if (touchLastFrame && !input.isTouched()) {
                advance();
            }

            touchLastFrame = input.isTouched();
        }
    }

    public void reset() {

    }

}
