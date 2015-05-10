package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.henrik.advergame.Game;
import com.henrik.advergame.SessionManager;
import com.henrik.gdxFramework.core.HUD;

/**
 * The HUD used in-game to display scores etc.
 */
public class InGameDataTable extends Table {

    private int currentScore;
    private int targetScore;
    private int scoreIncrement = 1;
    private Label score;

    private int currentKeyCount;
    private Label keys;

    public InGameDataTable(BitmapFont font) {
        super();

        score = HUD.makeLabel(0, 0, Color.BLACK, "Score: ", font);

        currentKeyCount = 0;
        keys = HUD.makeLabel(0, 0, Color.BLACK, "Keys: ", font);

        top().left();
        add(score).align(Align.left).width(170).padRight(10f).padLeft(20f);
        add(keys).align(Align.left).padRight(50f);

        pack();
        setBackground(Game.getUISkin().getDrawable("uiBackground"));
    }

    public void update(SessionManager sessionManager) {

        // Update score
        targetScore = sessionManager.getScore();
        if(currentScore < targetScore) {
            currentScore += scoreIncrement;
        }
        if(currentScore > targetScore) { // If we are above the current score, go down to it
            currentScore = targetScore;
        }

        score.setText("Score: " + String.valueOf(currentScore));

        // Update keys
        if(currentKeyCount != sessionManager.getKeyCount()) {
            currentKeyCount = sessionManager.getKeyCount();
        }

        keys.setText("Keys: " + String.valueOf(currentKeyCount));
    }
}
