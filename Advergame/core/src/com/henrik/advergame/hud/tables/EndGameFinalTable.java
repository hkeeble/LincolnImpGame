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
 * Created by Henri on 10/04/2015.
 */
public class EndGameFinalTable extends Table {

    public EndGameFinalTable(BitmapFont titleFont, BitmapFont mainFont, Skin uiSkin, SaveGame saveGame, EventListener exitListener) {

        Table headingTable = new Table();
        Table buttonTable = new Table();

        headingTable.setBackground(uiSkin.getDrawable("uiBackground"));
        buttonTable.setBackground(uiSkin.getDrawable("uiBackground"));

        Label titleLabel = HUD.makeLabel(0, 0, Color.BLACK, "GAME COMPLETE!", titleFont);
        Label scoreLabel = HUD.makeLabel(0, 0, Color.BLACK, "Total Score: " + saveGame.getTotalScore(), mainFont);
        Label destroyedLabel = HUD.makeLabel(0, 0, Color.BLACK, "Total\nStuff Destroyed: " + saveGame.getTotalObjectsDestroyed(), mainFont);
        Label congratsLabel = HUD.makeLabel(0, 0, Color.BLACK, "Congratulations!", titleFont);

        Button exitButton = HUD.makeButton(0, 0, exitListener, uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"), uiSkin.getDrawable("exitButton"));

        titleLabel.setAlignment(Align.center);
        scoreLabel.setAlignment(Align.center);
        destroyedLabel.setAlignment(Align.center);

        headingTable.add(titleLabel).padBottom(10f).center().row();
        headingTable.add(scoreLabel).padBottom(20f).center().row();
        headingTable.add(destroyedLabel).padBottom(20f).center().row();
        headingTable.add(congratsLabel).padBottom(20f).center().row();

        buttonTable.add(exitButton).size(96).center().pad(10);

        add(headingTable).padBottom(50f).row();
        add(buttonTable).padBottom(400f);
    }

}
