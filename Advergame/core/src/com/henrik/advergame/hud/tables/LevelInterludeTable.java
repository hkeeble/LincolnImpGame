package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.henrik.advergame.LevelSaveData;
import com.henrik.advergame.SaveGame;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 21/03/2015.
 */
public class LevelInterludeTable extends Table {

    public LevelInterludeTable(BitmapFont titleFont, BitmapFont mainFont, Skin uiSkin, SaveGame saveGame, EventListener continueListener, EventListener replayListener, EventListener exitListener) {

        Table headingTable = new Table();
        Table buttonTable = new Table();

        headingTable.setBackground(uiSkin.getDrawable("uiBackground"));
        buttonTable.setBackground(uiSkin.getDrawable("uiBackground"));

        LevelSaveData data = saveGame.getLastPlayedLevelData();

        Label titleLabel = HUD.makeLabel(0, 0, Color.BLACK, "FLOOR COMPLETE!", titleFont);
        Label scoreLabel = HUD.makeLabel(0, 0, Color.BLACK, "Score: " + data.getScore(), mainFont);
        Label destroyedLabel = HUD.makeLabel(0, 0, Color.BLACK, "Stuff Destroyed: " + String.valueOf(data.getObjectsDestroyed()) + "/" + String.valueOf(data.getPotentialObjects()), mainFont);

        Button replayButton = HUD.makeButton(0, 0, replayListener, uiSkin.getDrawable("replayButton"), uiSkin.getDrawable("replayButton"), uiSkin.getDrawable("replayButton"));
        Button playButton = HUD.makeButton(0, 0, continueListener, uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"));
        Button exitButton = HUD.makeButton(0, 0, exitListener, uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"));

        titleLabel.setAlignment(Align.center);
        scoreLabel.setAlignment(Align.center);
        destroyedLabel.setAlignment(Align.center);

        headingTable.add(titleLabel).padBottom(10f).center().row();
        headingTable.add(scoreLabel).center().row();
        headingTable.add(destroyedLabel).padBottom(20f).center().row();

        buttonTable.add(replayButton).size(96).pad(10);
        buttonTable.add(playButton).size(96).pad(10);
        buttonTable.add(exitButton).size(96).pad(10);

        add(headingTable).padBottom(150f).row();
        add(buttonTable).padBottom(40f);
    }
}
