package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.ArrayList;

/**
 * Created by Henri on 19/03/2015.
 */
public class EditMessageDialog extends Dialog {

    private TextField message;

    public String getMessage() {
        return message.getText();
    }

    public EditMessageDialog(String title, Skin skin, EventListener acceptListener, ArrayList<String> existingMessage) {
        super(title, skin);

        text("Enter messages, using ';' to delimit message sections:");
        getContentTable().row();

        // Compile the existing message sequence
        String existMsg = "";
        for(String msg : existingMessage) {
            existMsg += msg + ";";
        }

        message = new TextField("", skin);
        message.setText(existMsg);
        getContentTable().add(message).fillX();

        getButtonTable().add(HUD.makeTextButton(0, 0, "Accept", acceptListener, skin));

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
