package com.henrik.advergame.hud.tables;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.henrik.gdxFramework.core.HUD;

/**
 * Created by Henri on 10/04/2015.
 */
public class EndGameMessageTable extends Table {

    private String[] pages = {
            "In the end,\nthe Imp caused\nquite the commotion.",
            "But, 'twas inevitable\nhe would be caught...",
            "Turned to stone,\nthe imp was placed\natop a pillar.",
            "And to this day,\nthere he still sits..."
    };

    int currentPage = 0;
    private Label text;
    private Button playButton;

    boolean done;

    public EndGameMessageTable(BitmapFont font, Skin uiSkin, EventListener playListener) {

        text = HUD.makeLabel(0, 0, Color.BLACK, pages[currentPage], font);

        text.setAlignment(Align.center);

        playButton = HUD.makeButton(0, 0, playListener, uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"), uiSkin.getDrawable("playButton"));

        setBackground(uiSkin.getDrawable("uiBackground"));

        add(text).pad(10);
        add(playButton).size(96).center().pad(10);

        done = false;
    }

    public void advancePage() {
        currentPage++;

        if(currentPage < pages.length)
            text.setText(pages[currentPage]);
        else
            done = true;
    }

    public boolean isDone() {
        return done;
    }

    public int getCurrentPage() {
        return currentPage;
    }
}
