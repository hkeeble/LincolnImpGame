package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * Created by Henri on 24/02/2015.
 */
public class EditorMainHUD extends Table {

    BitmapFont font;

    public EditorMainHUD(Skin skin, EventListener newMapListener, EventListener loadMapListener, EventListener saveMapListener) {

        font = new BitmapFont();

        Button newButton = HUD.makeTextButton(0, 0, "New Map", newMapListener, skin.getDrawable("button"), skin.getDrawable("button"), skin.getDrawable("button"), font);
        Button saveButton = HUD.makeTextButton(0, 0, "Save Map", saveMapListener, skin.getDrawable("button"), skin.getDrawable("button"), skin.getDrawable("button"), font);
        Button loadButton = HUD.makeTextButton(0, 0, "Load Map", loadMapListener, skin.getDrawable("button"), skin.getDrawable("button"), skin.getDrawable("button"), font);

        add(newButton).pad(0,0,10,0);
        row();
        add(saveButton).pad(0,0,10,0);
        row();
        add(loadButton).pad(0,0,10,0);

        pack();
    }
}
