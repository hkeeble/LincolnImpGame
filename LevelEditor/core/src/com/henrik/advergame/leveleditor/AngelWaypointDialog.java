package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.henrik.advergame.leveleditor.shared.entities.AngelEntity;

import java.util.ArrayList;


/**
 * Created by Henri on 05/03/2015.
 */
public class AngelWaypointDialog extends Dialog {

    private final String DELIM = ";";

    private final String UP = "UP", DOWN = "DOWN", LEFT = "LEFT", RIGHT = "RIGHT";

    private ArrayList<AngelEntity.Instruction> instructions;

    private TextField timeTextField;
    private TextField directionsTextField;

    private AngelEntity.Waypoint waypoint;

    public AngelWaypointDialog(String title, Skin skin, EventListener acceptListener, EventListener deleteListener, EventListener cancelListener, AngelEntity.Waypoint waypoint) {
        super(title, skin);

        this.waypoint = waypoint;

        timeTextField = new TextField("", skin);
        directionsTextField = new TextField("", skin);

        text("Directions: ");
        getContentTable().add(directionsTextField);
        row();
        text("Times: ");
        getContentTable().add(timeTextField);

        getButtonTable().add(HUD.makeTextButton(0, 0, "Accept", acceptListener, skin));
        getButtonTable().add(HUD.makeTextButton(0, 0, "Delete", deleteListener, skin));
        getButtonTable().add(HUD.makeTextButton(0, 0, "Cancel", cancelListener, skin));



        parse(waypoint);
    }

    public void parse(AngelEntity.Waypoint waypoint) {
        instructions = waypoint.getInstructions();

        for(int i = 0; i < instructions.size(); i++) {
            timeTextField.setText(timeTextField.getText() + String.valueOf(instructions.get(i).getWaitTimeSeconds()));

            Point dir = instructions.get(i).getDirection();
            String dirStr = "";
            if(dir.x < 0) {
                dirStr = LEFT;
            } else if (dir.x > 0) {
                dirStr = RIGHT;
            } else if(dir.y < 0) {
                dirStr = DOWN;
            } else if(dir.y > 0) {
                dirStr = UP;
            }

            directionsTextField.setText(directionsTextField.getText() + dirStr);

            if(!(i == instructions.size()-1)) {
                directionsTextField.setText(directionsTextField.getText() + DELIM);
                timeTextField.setText(timeTextField.getText() + DELIM);
            }
        }
    }

    public void build() {
        String[] times = timeTextField.getText().split(DELIM);
        String[] directions = directionsTextField.getText().split(DELIM);

        instructions = new ArrayList<AngelEntity.Instruction>();

        if(times.length != directions.length) {
            Gdx.app.log("Error!", "Number of times and directions are not equal.");
            return;
        }

        if(times[0].equals(""))
            return;

        for(int i = 0; i < times.length; i++) {
            Point dir = new Point(0,0);
            if(directions[i].equals(UP)) {
                dir.y = 1;
            } else if(directions[i].equals(DOWN)) {
                dir.y = -1;
            } else if(directions[i].equals(LEFT)) {
                dir.x = -1;
            } else if(directions[i].equals(RIGHT)) {
                dir.x = 1;
            }

            int time = Integer.parseInt(times[i]);

            AngelEntity.Instruction inst = new AngelEntity.Instruction(dir, time);
            instructions.add(inst);
        }
    }

    public AngelEntity.Waypoint getWaypoint() {
        return waypoint;
    }

    public ArrayList<AngelEntity.Instruction> getInstructions() {
        return instructions;
    }
}
