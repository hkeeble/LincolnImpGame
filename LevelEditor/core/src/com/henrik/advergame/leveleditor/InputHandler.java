package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Henri on 25/02/2015.
 */
public class InputHandler extends InputAdapter {

    private boolean leftClickDown;
    private boolean rightClickDown;
    private boolean scrolled;

    private Vector2 leftClickPos;
    private Vector2 rightClickPos;
    private int scrollAmount;

    public InputHandler() {
        leftClickPos = new Vector2();
        rightClickPos = new Vector2();
    }

    public Vector2 getRightClickPos() {
        return rightClickPos;
    }

    public Vector2 getLeftClickPos() {
        return leftClickPos;
    }

    public boolean isLeftClickDown() {
        return leftClickDown;
    }

    public boolean isRightClickDown() {
        return rightClickDown;
    }

    public int getScrollAmount() {
        return scrollAmount;
    }

    public boolean isScrolled() {
        return scrolled;
    }

    public void clear() {
        leftClickDown = false;
        rightClickDown = false;
    }

    public void scrollHandled() {
        scrolled = false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            leftClickDown = true;
            leftClickPos.set(screenX, screenY);
        } else if(button == Input.Buttons.RIGHT) {
            rightClickDown = true;
            rightClickPos.set(screenX, screenY);
        }

        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button == Input.Buttons.LEFT) {
            leftClickDown = false;
        } else if(button == Input.Buttons.RIGHT) {
            rightClickDown = false;
        }
        return super.touchUp(screenX, screenY, pointer, button);
    }

    @Override
    public boolean scrolled(int amount) {
        scrolled = true;
        scrollAmount = amount;

        return super.scrolled(amount);
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(leftClickDown) {
            leftClickPos.set(screenX, screenY);
        } else if(rightClickDown) {
            rightClickPos.set(screenX, screenY);
        }

        return super.touchDragged(screenX, screenY, pointer);
    }

    public Vector3 unprojectedMousePos(Camera camera, int button) {
        Vector3 touchPos = new Vector3();
        if(button == Input.Buttons.LEFT) {
            touchPos.set(leftClickPos.x, leftClickPos.y, 0);
        } else if(button == Input.Buttons.RIGHT) {
            touchPos.set(rightClickPos.x, rightClickPos.y, 0);
        }
        camera.unproject(touchPos);
        return touchPos;
    }

    public Vector3 unprojectedMousePos(Camera camera, Vector3 pos) {
        Vector3 touchPos = new Vector3();
        touchPos.set(pos.x, pos.y, 0);
        camera.unproject(touchPos);
        return touchPos;
    }
}