package com.henrik.advergame.components;

import com.badlogic.gdx.math.Vector3;
import com.henrik.advergame.AngelState;
import com.henrik.advergame.AnimationTypeMove;
import com.henrik.advergame.utils.Direction;
import com.henrik.advergame.utils.Point;
import com.henrik.advergame.utils.VectorMath;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 25/02/2015.
 */
public class AngelDogController extends StateControllerComponent {

    private final float moveSpeed = 7f;
    private float distanceToTarget;
    private Vector3 currentDirection;
    private Vector3 startPosition;
    private Vector3 currentTarget;
    private int wayPointID;
    private ArrayList<Point> wayPoints;

    private Vector3 velocity;
    private Direction direction;

    private int waypointDirection;

    private GameObject object;

    public AngelDogController(Vector3 initialPos, ArrayList<Point> wayPoints) {
        super();

        this.wayPoints = wayPoints;

        currentDirection = new Vector3();
        currentTarget = new Vector3();
        startPosition = new Vector3();

        direction = new Direction();

        setState(AngelState.ROAM);
        wayPointID = 0;
        updateTarget(initialPos);

        waypointDirection = 1;

        velocity = new Vector3();
    }

    @Override
    public void update(GameObject object, GameWorld world, SpriteDecalGraphicsComponent graphicsComponent) {
        super.update(object, world, graphicsComponent);

        this.object = object;

        switch ((AngelState)currentState) {

            case ATTACK:
                break;

            case ROAM:
                velocity.set(currentDirection.x * moveSpeed, currentDirection.y * moveSpeed, currentDirection.z * moveSpeed);
                object.setVelocity(velocity);

                // Change our target if we reach the current one
                if(object.getPosition().dst(startPosition) >= distanceToTarget) {
                    updateWaypoint();
                }
                break;
        }

        direction.update(object.getVelocity());
        updateAnimation(object, graphicsComponent);
    }

    public void reverseDirection() {
        waypointDirection *= -1;
        wayPointID += waypointDirection;
    }

    private void updateWaypoint() {
        wayPointID += waypointDirection;
        if(wayPointID > wayPoints.size()-1)
            wayPointID = 0;
        else if(wayPointID < 0)
            wayPointID = wayPoints.size()-1;

        updateTarget(object.getPosition());
    }

    /**
     * Updates the animation based on direction.
     * @param object The angel object.
     * @param graphicsComponent The animated graphics component.
     */
    private void updateAnimation(GameObject object, SpriteDecalGraphicsComponent graphicsComponent) {
        if(direction.getCurrent() == Direction.UP) {
            graphicsComponent.setAnimation(AnimationTypeMove.WalkUp);
        }
        if(direction.getCurrent() == Direction.DOWN) {
            graphicsComponent.setAnimation(AnimationTypeMove.WalkDown);
        }
        if(direction.getCurrent() == Direction.LEFT) {
            graphicsComponent.setAnimation(AnimationTypeMove.WalkLeft);
        }
        if(direction.getCurrent() == Direction.RIGHT) {
            graphicsComponent.setAnimation(AnimationTypeMove.WalkRight);
        }

        if(!VectorMath.isMoving(object.getVelocity())) {
            graphicsComponent.pause();
            graphicsComponent.reset();
        } else if(!graphicsComponent.isPlaying()) {
            graphicsComponent.start();
        }
    }

    private void updateTarget(Vector3 initialPos) {
        startPosition.set(initialPos);
        currentTarget.set(wayPoints.get(wayPointID).x, 0, wayPoints.get(wayPointID).y);
        VectorMath.directionTo(initialPos, currentTarget, currentDirection);
        distanceToTarget = initialPos.dst(currentTarget);
    }

    @Override
    public void dispose() {

    }
}
