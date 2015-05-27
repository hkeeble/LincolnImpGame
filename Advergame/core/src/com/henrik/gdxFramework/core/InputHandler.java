package com.henrik.gdxFramework.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 12/11/2014.
 */
public class InputHandler extends InputAdapter {

    private final int MAX_POINTERS = 2;
    private int mostRecentPointer = 0;

    class TouchData {
        private Vector2 position;
        private Vector2 dragDistance;
        private boolean touched;

        public TouchData() {
            position = new Vector2();
            dragDistance = new Vector2();
            touched = false;
        }

        public Vector2 getPosition() { return position; }
        public Vector2 getDragDistance() { return dragDistance; }
        public boolean isTouched() { return touched; }

        public void setPosition(float x, float y) { this.position.set(x, y); }
        public void setDragDistance(float x, float y) { this.dragDistance.set(x, y); }
        public void setTouched(boolean touched) {this.touched = touched; }
    }

    private Map<Integer,TouchData> touchData = new HashMap<Integer,TouchData>();

    public InputHandler() {
        for(int i = 0; i < MAX_POINTERS; i++ ) {
            touchData.put(i, new TouchData());
        }
    }

    /**
     * Gets the position of the most recently touched pointer.
     */
    public Vector2 getPosition() {
        return touchData.get(mostRecentPointer).getPosition();
    }

    /**
     * Gets whether or not the player is touching/clicking the screen.
     */
    public boolean isTouched() {
        for(int i = 0; i < MAX_POINTERS; i++) {
            if(touchData.get(i).isTouched() == true)
                return true;
        }
        return false;
    }

    public Vector2 getDrag() {
        return touchData.get(0).getDragDistance();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(pointer < MAX_POINTERS) {
            touchData.get(pointer).setPosition(screenX, screenY);
            touchData.get(pointer).setTouched(true);
            mostRecentPointer = pointer;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(pointer < MAX_POINTERS) {
            touchData.get(pointer).setPosition(screenX, screenY);
            touchData.get(pointer).setTouched(false);
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(pointer < MAX_POINTERS) {
            Vector2 initialPos = touchData.get(pointer).getPosition();
            touchData.get(pointer).setDragDistance(screenX - initialPos.x, screenY - initialPos.y);
        }

        return false;
    }

    /**
     * Use this to clear the input buffer.
     */
    public void clear() {
        Iterator iter = touchData.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            TouchData data = (TouchData)entry.getValue();
            data.setTouched(false);
        }
    }
}
