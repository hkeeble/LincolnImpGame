package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.henrik.advergame.Game;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 20/03/2015.
 */
public class PauseTable extends Table {

    public PauseTable(BitmapFont titleFont, BitmapFont buttonFont, EventListener resumeListener, EventListener resetListener, EventListener mainMenuListener) {

        Label.LabelStyle style = new Label.LabelStyle(titleFont, Color.BLACK);

        Label title = HUD.makeLabel(0, 0, "PAUSED", style);
        Button resumeButton = HUD.makeButton(0, 0, resumeListener, Game.getUISkin().getDrawable("playButton"), Game.getUISkin().getDrawable("playButton"), Game.getUISkin().getDrawable("playButton"));
        Button resetButton = HUD.makeButton(0, 0, resetListener, Game.getUISkin().getDrawable("replayButton"), Game.getUISkin().getDrawable("replayButton"), Game.getUISkin().getDrawable("replayButton"));
        Button mainMenuButton = HUD.makeButton(0, 0, mainMenuListener, Game.getUISkin().getDrawable("exitButton"), Game.getUISkin().getDrawable("exitButton"), Game.getUISkin().getDrawable("exitButton"));

        add(title).pad(30, 30, 30, 30).row();

        Table buttonTable = new Table();
        buttonTable.add(resumeButton).pad(10, 10, 10, 10).size(96, 96);
        buttonTable.add(resetButton).pad(10, 10, 10, 10).size(96, 96);
        buttonTable.add(mainMenuButton).pad(10, 10, 10, 10).size(96, 96);
        buttonTable.padBottom(30);
        add(buttonTable);

        setBackground(Game.getUISkin().getDrawable("uiBackground"));
    }

}
