package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
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

    private StringBuilder stringBuilder;

    public InGameDataTable(BitmapFont font) {
        super();

        Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);

        score = HUD.makeLabel(0, 0, "Score: ", style);

        currentKeyCount = 0;
        keys = HUD.makeLabel(0, 0, "Keys: ", style);

        top().left();
        add(score).align(Align.left).width(170).padRight(10f).padLeft(20f);
        add(keys).align(Align.left).padRight(50f);

        pack();
        setBackground(Game.getUISkin().getDrawable("uiBackground"));

        stringBuilder = new StringBuilder();
    }

    public void update(SessionManager sessionManager) {

        // Update score
        targetScore = sessionManager.getScore();
        if(currentScore < targetScore) {
            currentScore += scoreIncrement;
            updateScore();
        }
        if(currentScore > targetScore) { // If we are above the current score, go down to it
            currentScore = targetScore;
            updateScore();
        }

        // Update keys
        if(currentKeyCount != sessionManager.getKeyCount()) {
            currentKeyCount = sessionManager.getKeyCount();
            updateKeys();
        }
    }

    private void updateScore() {
        stringBuilder.setLength(0);
        stringBuilder.append("Score ").append(String.valueOf(currentScore));
        score.setText(stringBuilder.toString());
    }

    private void updateKeys() {
        stringBuilder.setLength(0);
        stringBuilder.append("Keys ").append(String.valueOf(currentKeyCount));
        keys.setText(stringBuilder.toString());
    }
}
