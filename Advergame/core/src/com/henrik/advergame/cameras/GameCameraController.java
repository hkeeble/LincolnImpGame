package com.henrik.advergame.cameras;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import com.henrik.advergame.systems.MessageSequence;
import com.henrik.gdxFramework.core.CameraController;
import com.henrik.gdxFramework.entities.GameObject;

/**
 * A camera controller for the game.
 */
public class GameCameraController extends CameraController {

    private enum State {
        NONE,
        FOLLOW,
        ORBIT,
        MESSAGE_SEQUENCE
    }

    // The camera state
    private State currentState;

    // Orbit members
    private Vector3 orbitPoint;
    private float orbitRadius;
    private float orbitSpeed;
    private float orbitHeight;

    // Follow members
    private float followSpeed;
    private Vector3 currentTargetPoint;
    private GameObject followTarget;
    private Vector3 followOffset;

    // Message Sequence...
    private MessageSequence sequenceTarget;

    public GameCameraController() {
        currentState = State.NONE;
        currentTargetPoint = new Vector3(0,0,0);
    }

    /**
     * Update the camera controller.
     */
    public void update() {
        if(currentState.equals(State.FOLLOW)) { // FOLLOW State

            camera.up.set(0, 1, 0);

            currentTargetPoint.set(followTarget.getPosition()).add(followOffset);

            camera.position.x = followTarget.getPosition().x;
            camera.position.z += (currentTargetPoint.z - camera.position.z) * 0.1f;
            camera.position.y += (currentTargetPoint.y - camera.position.y) * 0.1f;

            camera.lookAt(followTarget.getPosition());
        } else if(currentState.equals(State.ORBIT)) { // ORBIT state
            updateOrbit();
        } else if(currentState.equals(State.MESSAGE_SEQUENCE)) { // MESSAGE_SEQUENCE state
            this.orbitPoint = sequenceTarget.getPosition();
            updateOrbit();
        }
    }

    private void updateOrbit() {
        // Rotate
        camera.position.set(orbitPoint);
        camera.lookAt(orbitPoint);
        camera.rotate(orbitSpeed, 0, 1, 0);

        // Move back to correct radius
        Vector3 camDir = new Vector3(camera.direction);
        camDir.scl(-orbitRadius);
        camera.translate(new Vector3(camDir.x, orbitHeight, camDir.z));
        camera.lookAt(orbitPoint); // Re-look at after we translate*/
    }

    /**
     * Set this camera controller to follow this given target.
     * @param target The GameObject to follow.
     * @param followOffset The offset to follow at.
     * @param followSpeed The speed to follow at (0-1, lower speeds are smoother)
     */
    public void follow(GameObject target, Vector3 followOffset, float followSpeed) {
        currentState = State.FOLLOW;
        followTarget = target;
        this.followOffset = followOffset;
        this.followSpeed = Math.abs(followSpeed);

        if(this.followSpeed > 1)
            this.followSpeed = 1;
    }

    /**
     * Orbit the given point.
     * @param orbitPoint The point to orbit.
     * @param orbitRadius The distance to orbit at.
     * @param orbitSpeed The speed to orbit at.
     */
    public void orbit(Vector3 orbitPoint, float orbitRadius, float orbitSpeed, float orbitHeight) {
        currentState = State.ORBIT;
        this.orbitPoint = orbitPoint;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.orbitHeight = orbitHeight;
    }

    /**
     * Gets this camera to follow the given message sequence.
     * @param messageSequence The message sequence to follow.
     */
    public void followMessageSequence(MessageSequence messageSequence, float orbitRadius, float orbitSpeed, float orbitHeight) {
        this.sequenceTarget = messageSequence;
        this.currentState = State.MESSAGE_SEQUENCE;
        this.orbitRadius = orbitRadius;
        this.orbitSpeed = orbitSpeed;
        this.orbitHeight = orbitHeight;
    }

    public PerspectiveCamera getCamera() { return (PerspectiveCamera)camera; }
}
