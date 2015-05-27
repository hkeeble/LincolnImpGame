package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.henrik.advergame.SaveGame;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 09/04/2015.
 */
public class TitleDataTable extends Table {

    private Label totalScore, totalObjects;

    public TitleDataTable(BitmapFont font, SaveGame saveGame, Skin uiSkin) {

        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);

        totalScore = HUD.makeLabel(0, 0, "Total Score: " + String.valueOf(saveGame.getTotalScore()), style);
        totalObjects = HUD.makeLabel(0, 0, "Total Objects Destroyed: " + String.valueOf(saveGame.getTotalObjectsDestroyed()), style);

        setBackground(uiSkin.getDrawable("uiBackground"));

        add(totalScore).row();
        add(totalObjects).pad(10);
    }

}
