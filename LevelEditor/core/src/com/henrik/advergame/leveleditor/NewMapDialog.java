package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Created by Henri on 24/02/2015.
 */
public class NewMapDialog extends Dialog {

    private TextField nameField;
    private TextField widthField;
    private TextField heightField;

    public int getMapWidth() {
        return Integer.parseInt(widthField.getText());
    }

    public int getMapHeight() {
        return Integer.parseInt(heightField.getText());
    }

    public String getMapName() {
        return nameField.getText();
    }

    public NewMapDialog(String title, Skin skin, EventListener acceptedListener) {
        super(title, skin);

        nameField = new TextField("", skin);
        widthField = new TextField("5", skin);
        heightField = new TextField("5", skin);

        widthField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        heightField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());

        text("Name:");
        getContentTable().add(nameField);
        getContentTable().row();
        text("Width:");
        getContentTable().add(widthField);
        getContentTable().row();
        text("Height:");
        getContentTable().add(heightField);

        getButtonTable().add(HUD.makeTextButton(0, 0, "Create", acceptedListener, skin));

        getButtonTable().add(HUD.makeTextButton(0, 0, "Cancel", new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                hide();
                setVisible(false);

                return super.touchDown(event, x, y, pointer, button);
            }
        }, skin));
    }

    public NewMapDialog(String title, Skin skin, String windowStyleName) {
        super(title, skin, windowStyleName);
    }

    public NewMapDialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);

    }
}
