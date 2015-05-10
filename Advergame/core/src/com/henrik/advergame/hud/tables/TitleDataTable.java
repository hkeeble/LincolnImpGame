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

        totalScore = HUD.makeLabel(0, 0, Color.BLACK, "Total Score: " + String.valueOf(saveGame.getTotalScore()), font);
        totalObjects = HUD.makeLabel(0, 0, Color.BLACK, "Total Objects Destroyed: " + String.valueOf(saveGame.getTotalObjectsDestroyed()), font);

        setBackground(uiSkin.getDrawable("uiBackground"));

        add(totalScore).row();
        add(totalObjects).pad(10);
    }

}
