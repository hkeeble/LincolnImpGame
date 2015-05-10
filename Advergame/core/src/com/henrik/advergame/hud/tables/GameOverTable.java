package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.henrik.advergame.LevelSaveData;
import com.henrik.advergame.SaveGame;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 01/04/2015.
 */
public class GameOverTable extends Table {

    private Table mainTable;

    public GameOverTable(BitmapFont titleFont, BitmapFont mainFont, Skin uiSkin, SaveGame saveGame, EventListener replayListener, EventListener exitListener) {
        LevelSaveData data = saveGame.getLastPlayedLevelData();

        mainTable = new Table();

        Table headingTable = new Table();
        Table buttonTable = new Table();

        Label titleLabel = HUD.makeLabel(0, 0, Color.BLACK, "GAME OVER!", titleFont);
        Label subtitleLabel = HUD.makeLabel(0, 0, Color.BLACK, "You got caught...", mainFont);
        Button exitButton = HUD.makeButton(0, 0, exitListener, uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"));
        Button replayButton = HUD.makeButton(0, 0, replayListener, uiSkin.getDrawable("replayButton"), uiSkin.getDrawable("replayButton"), uiSkin.getDrawable("replayButton"));

        titleLabel.setAlignment(Align.center);
        subtitleLabel.setAlignment(Align.center);

        headingTable.setBackground(uiSkin.getDrawable("uiBackground"));
        buttonTable.setBackground(uiSkin.getDrawable("uiBackground"));

        headingTable.add(titleLabel).pad(15).center().row();
        headingTable.add(subtitleLabel).pad(15).center().row();
        buttonTable.add(exitButton).size(96, 96).pad(10, 10, 10, 10);
        buttonTable.add(replayButton).size(96, 96).pad(10, 10, 10, 10);

        mainTable.add(headingTable).padBottom(250f).row();
        mainTable.add(buttonTable);
        mainTable.setFillParent(true);
        add(mainTable);
    }

}
