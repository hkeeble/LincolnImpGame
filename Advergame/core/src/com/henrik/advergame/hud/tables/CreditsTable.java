package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.henrik.gdxFramework.core.HUD;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.utils.Align;

/**
 * Created by Henri on 09/04/2015.
 */
public class CreditsTable extends Table {

    private Label programmerTitle, artTitle, soundTitle, programmer, art, sound;
    private Button exitButton;

    public CreditsTable(BitmapFont largeFont, BitmapFont smallFont, Skin uiSkin, EventListener exitListener) {

        exitButton = HUD.makeButton(0, 0, exitListener, uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"));

        programmerTitle = HUD.makeLabel(0, 0, Color.GRAY, "Programming", smallFont);
        artTitle = HUD.makeLabel(0, 0, Color.GRAY, "Art", smallFont);
        soundTitle = HUD.makeLabel(0, 0, Color.GRAY, "Audio", smallFont);

        programmer = HUD.makeLabel(0, 0, Color.BLACK, "Henri Keeble", largeFont);
        art = HUD.makeLabel(0, 0, Color.BLACK, "Brooke Hayes", largeFont);
        sound = HUD.makeLabel(0, 0, Color.BLACK, "PlayOnLoop.com", largeFont);

        setBackground(uiSkin.getDrawable("uiBackground"));

        add(programmerTitle).align(Align.bottomRight).padLeft(10).padBottom(10);
        add(programmer).align(Align.left).padRight(10).row();
        add(artTitle).align(Align.bottomRight).padLeft(10).padBottom(10);
        add(art).align(Align.left).padRight(10).row();
        add(soundTitle).align(Align.bottomRight).padLeft(10).padBottom(10);
        add(sound).align(Align.left).padRight(10).row();
        add(exitButton).colspan(2).pad(20);

        center();
    }

}
