package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 09/04/2015.
 */
public class IntroTable extends Table {

    private String[] pages = {
            "'Twas said that in the\n" +
            "14th Century a mischevious\n" +
            "Imp entered Lincoln Cathedral.",
            "He was sent to cause mayhem,\n" +
            "but was turned to stone.",
            "He still sits atop a pillar\n" +
            "there to this day.",
            "But how did this come about?"
    };

    int currentPage = 0;
    private Label text;
    private Button playButton;

    boolean done;

    public IntroTable(BitmapFont font, Skin uiSkin, EventListener playListener) {

        text = HUD.makeLabel(0, 0, Color.BLACK, pages[currentPage], font);

        playButton = HUD.makeButton(0, 0, playListener, uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"));

        setBackground(uiSkin.getDrawable("uiBackground"));

        add(text).pad(10).row();
        add(playButton).size(96).pad(10);

        done = false;
    }

    public void advancePage() {
        currentPage++;

        if(currentPage < pages.length)
            text.setText(pages[currentPage]);
        else
            done = true;
    }

    public boolean isDone() {
        return done;
    }
}
