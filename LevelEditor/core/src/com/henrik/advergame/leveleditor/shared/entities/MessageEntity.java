package com.henrik.advergame.leveleditor.shared.entities;

import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

import java.util.ArrayList;

/**
 * Created by Henri on 19/03/2015.
 */
public class MessageEntity extends MapEntity {

    private static final String MESSAGE_DELIM = "~";
    private static final String FOCUS_DELIM = "%";

    private ArrayList<String> messages;
    private Point focusPoint;

    private boolean selfFocus;

    public MessageEntity(Point location) {
        super(location, MapEntityState.MESSAGE.getID());

        messages = new ArrayList<String>();
        focusPoint = location;

        selfFocus = true;
    }

    protected MessageEntity(String data) {
        super(data);

        String[] split = data.split(POS_DELIM);
        String[] messageFocusPoint = split[1].split(FOCUS_DELIM);

        // Messages
        String[] messageStrings = messageFocusPoint[0].split(MESSAGE_DELIM);
        this.messages = new ArrayList<String>();
        for(String message : messageStrings) {
            messages.add(message);
        }

        // Focus point
        String[] focusPointStr = messageFocusPoint[1].split(POINT_DELIM);
        focusPoint = new Point(Integer.parseInt(focusPointStr[0]), Integer.parseInt(focusPointStr[1]));

        if(focusPoint.equals(location)) {
            selfFocus = true;
        } else {
            selfFocus = false;
        }
    }

    public void addMessage(String message) {
        messages.add(message);
    }

    public boolean removeMessage(String message) {
        return messages.remove(message);
    }

    public void setMessages(ArrayList<String> messages) {
        this.messages.clear();
        for(String message : messages) {
            this.messages.add(message);
        }
    }

    public ArrayList<String> getMessages() {
        return messages;
    }

    public Point getFocusPoint() {
        return focusPoint;
    }

    public boolean isSelfFocus() {
        return selfFocus;
    }

    public void setFocusPoint(Point focusPoint) {
        this.focusPoint = focusPoint;
        if(this.focusPoint.equals(location))
            selfFocus = true;
        else
            selfFocus = false;
    }

    @Override
    public String build() {
        String str = super.build(MapEntityState.MESSAGE.getID());

        for(String message : messages) {
            str += message + MESSAGE_DELIM;
        }

        str += FOCUS_DELIM + String.valueOf(focusPoint.x) + POINT_DELIM + String.valueOf(focusPoint.y);

        return str;
    }

    public static MessageEntity Parse(String data) {
        return new MessageEntity(data);
    }
}


