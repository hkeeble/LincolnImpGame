package com.henrik.advergame.cameras;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.henrik.advergame.utils.VectorMath;
import com.henrik.gdxFramework.core.CameraController;

/**
 * A camera controller used for cinematic purposes.
 */
public class CinematicCamera extends CameraController {

    public enum State {
        TARGET,
        ZOOM_TO_TARGET,
        NONE
    }

    // The current state
    private State currentState;

    // Target Variables
    private Vector3 currentTarget;
    private BoundingBox targetBox;
    private float moveSpeed;

    // Zoom Variables
    private float currentFOV;
    private float minFOV;

    public CinematicCamera() {
        currentTarget = new Vector3();
        currentState = State.NONE;
    }

    public void update() {
        if(currentState.equals(State.TARGET)) {
            Vector3 velocity = VectorMath.directionTo(camera.position, currentTarget).scl(moveSpeed);
            this.camera.translate(velocity);

            Vector3 currentPosition = camera.position;
            if(targetBox.contains(currentPosition)) {
                currentState = State.NONE;
            }

        } if(currentState.equals(State.ZOOM_TO_TARGET)) {
            currentFOV = ((PerspectiveCamera)camera).fieldOfView;
            float newFOV = currentFOV-moveSpeed;
            if(newFOV <= minFOV) {
                newFOV = minFOV;
                currentState = State.NONE;
            }

            ((PerspectiveCamera)camera).fieldOfView = newFOV;
            camera.projection.setToProjection(camera.near, camera.far, newFOV, Gdx.graphics.getWidth()/Gdx.graphics.getHeight());
        }
    }

    public void setPosition(Vector3 position) {
        this.camera.position.set(position);
    }

    public void lookAt(Vector3 point) { this.camera.lookAt(point); }

    public void moveToTarget(Vector3 target, float speed) {
        currentState = State.TARGET;
        currentTarget.set(target);
        moveSpeed = speed;

        targetBox = new BoundingBox(new Vector3(target).sub(speed), new Vector3(target).add(speed));
    }

    public void zoomToTarget(Vector3 target, float speed, float minFOV) {
        currentState = State.ZOOM_TO_TARGET;
        currentTarget.set(target);
        moveSpeed = speed;
        this.minFOV = minFOV;
    }

    public State getCurrentState() { return currentState; }
}
