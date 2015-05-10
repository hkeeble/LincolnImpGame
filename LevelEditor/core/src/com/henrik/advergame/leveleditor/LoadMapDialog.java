package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Henri on 24/02/2015.
 */
public class LoadMapDialog extends Dialog {

    private TextField nameField;

    public String getMapName() {
        return nameField.getText();
    }

    public LoadMapDialog(String title, Skin skin, EventListener acceptListener) {
        super(title, skin);

        nameField = new TextField("", skin);

        text("Name: ");
        getContentTable().add(nameField);

        getButtonTable().add(HUD.makeTextButton(0, 0, "Load", acceptListener, skin));

        getButtonTable().add(HUD.makeTextButton(0, 0, "Cancel", new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hide();
                setVisible(false);
                return super.touchDown(event, x, y, pointer, button);
            }
        }, skin));
    }
}
