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

        Label.LabelStyle grayStyle = new Label.LabelStyle(smallFont, Color.GRAY);
        Label.LabelStyle blackStyle = new Label.LabelStyle(largeFont, Color.BLACK);


        programmerTitle = HUD.makeLabel(0, 0, "Programming", grayStyle);
        artTitle = HUD.makeLabel(0, 0, "Art", grayStyle);
        soundTitle = HUD.makeLabel(0, 0, "Audio", grayStyle);

        programmer = HUD.makeLabel(0, 0, "Henri Keeble", blackStyle);
        art = HUD.makeLabel(0, 0, "Brooke Hayes", blackStyle);
        sound = HUD.makeLabel(0, 0, "PlayOnLoop.com", blackStyle);

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
