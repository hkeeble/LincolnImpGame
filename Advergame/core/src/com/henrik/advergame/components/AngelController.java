package com.henrik.advergame.components;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.advergame.AnimationTypeMove;
import com.henrik.advergame.State;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.level.shared.entities.AngelEntity;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.Direction;
import com.henrik.advergame.utils.VectorMath;
import com.henrik.advergame.worlds.GameWorld;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;
import com.henrik.gdxFramework.entities.components.SpriteDecalGraphicsComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 12/03/2015.
 */
public class AngelController extends StateControllerComponent {

    // Used to control state
    public enum AngelPatrolState implements State {
        MOVE(0), WAIT(1);

        private int id;

        AngelPatrolState(int id) {
            this.id = id;
        }

        @Override
        public int getID() {
            return id;
        }
    }

    // The angel's waypoints, the angel follows these
    private ArrayList<AngelEntity.Waypoint> waypoints;

    // Used for movement
    private Vector3 moveDirection;
    private Vector3 locationAtStartOfMove;
    private Vector3 currentTarget;
    private float distanceToTarget;
    private final float MOVE_SPEED = 6f;

    // Used to control waypoint index
    private int currentWaypointIndex;
    private int waypointIndexDirection;
    private AngelEntity.Waypoint currentWaypoint;

    // used to control instructionsin WAIT state
    private AngelEntity.Instruction currentInstruction;
    private int currentInstructionIndex;
    private Vector3 faceDirection;
    private long timeAtStartOfInstruction;

    // Used to track directions
    private Direction direction;
    private Direction previousDirection;

    // The angel's sight boxes, used for different direction
    private PhysicalEntity currentSightBox;
    private HashMap<Integer,PhysicalEntity> sightBoxes;

    private final float VISION_AREA_LENGTH = 3.2f;
    private final float VISION_AREA_HALF_WIDTH = 2f;

    private Quaternion rotation;
    
    private Vector3 temp;
    
    // Default sight box vertices
    private final Vector3[] VISION_AREA_VERTICES = new Vector3[] {
            new Vector3(0f, 0f, VISION_AREA_LENGTH),
            new Vector3(VISION_AREA_HALF_WIDTH, 0f, 0f),
            new Vector3(-VISION_AREA_HALF_WIDTH, 0f, 0f)
    };

    // Vision model
    private Model visionModel;
    private Mesh upVisionMesh, downVisionMesh, rightVisionMesh, leftVisionMesh;

    public AngelController(ArrayList<AngelEntity.Waypoint> waypoints, AssetManager assetManager) {
        super();
        this.waypoints = waypoints;

        rotation = new Quaternion();
        
        temp = new Vector3();
        currentTarget = new Vector3();
        faceDirection = new Vector3();
        moveDirection = new Vector3();
        locationAtStartOfMove = new Vector3();
        currentWaypointIndex = 0;
        currentWaypointIndex = 0;
        waypointIndexDirection = 1;

        direction = new Direction();
        previousDirection = new Direction();

        createVisionMeshes(); // Create vision models

        // Use vision mesh data to construct physics entities
        sightBoxes = new HashMap<Integer, PhysicalEntity>();
        sightBoxes.put(Direction.DOWN, new PhysicalEntity(new PhysicsComponent(new btConvexHullShape(downVisionMesh.getVerticesBuffer(), downVisionMesh.getNumVertices(), downVisionMesh.getVertexSize()), CollisionTags.ANGEL_SIGHT_BOX)));
        sightBoxes.put(Direction.UP, new PhysicalEntity(new PhysicsComponent(new btConvexHullShape(upVisionMesh.getVerticesBuffer(), upVisionMesh.getNumVertices(), upVisionMesh.getVertexSize()), CollisionTags.ANGEL_SIGHT_BOX)));
        sightBoxes.put(Direction.LEFT, new PhysicalEntity(new PhysicsComponent(new btConvexHullShape(leftVisionMesh.getVerticesBuffer(), leftVisionMesh.getNumVertices(), leftVisionMesh.getVertexSize()), CollisionTags.ANGEL_SIGHT_BOX)));
        sightBoxes.put(Direction.RIGHT, new PhysicalEntity(new PhysicsComponent(new btConvexHullShape(rightVisionMesh.getVerticesBuffer(), rightVisionMesh.getNumVertices(), rightVisionMesh.getVertexSize()), CollisionTags.ANGEL_SIGHT_BOX)));
    }

    /**
     * Creates vision models, these models are used to create appropriate convex hull collision shapes for the angel's sight.
     */
    private void createVisionMeshes() {
        ModelBuilder builder = new ModelBuilder();
        MeshPartBuilder mpb;

        builder.begin();
        mpb = builder.part("main", GL20.GL_TRIANGLES, VertexAttributes.Usage.Position, new Material(ColorAttribute.createDiffuse(0, 0, 0, 0)));
        for(int i = 0; i < VISION_AREA_VERTICES.length; i++) {
            mpb.vertex(VISION_AREA_VERTICES[i], new Vector3(), Color.GREEN, new Vector2());
        }
        visionModel = builder.end();

        // Get the vertices from the model
        Mesh baseMesh = visionModel.meshes.get(0);
        float[] verts = new float[baseMesh.getNumVertices()*3];
        baseMesh.getVertices(verts);

        // Create mesh for each new direction
        upVisionMesh = new Mesh(true, baseMesh.getNumVertices(), 0, VertexAttribute.Position()).setVertices(verts);
        downVisionMesh = new Mesh(true, baseMesh.getNumVertices(), 0, VertexAttribute.Position()).setVertices(verts);
        leftVisionMesh = new Mesh(true, baseMesh.getNumVertices(), 0, VertexAttribute.Position()).setVertices(verts);
        rightVisionMesh = new Mesh(true, baseMesh.getNumVertices(), 0, VertexAttribute.Position()).setVertices(verts);

        // Rotate meshes accordingly
        leftVisionMesh.transform(new Matrix4().setToRotation(0, 1, 0, 90));
        downVisionMesh.transform(new Matrix4().setToRotation(0, 1, 0, 180));
        rightVisionMesh.transform(new Matrix4().setToRotation(0, 1, 0, -90));
    }

    private void nextWaypoint() {
        if(waypoints.size() != 1) {
            if (currentWaypointIndex + waypointIndexDirection > waypoints.size() - 1 || currentWaypointIndex + waypointIndexDirection < 0) {
                waypointIndexDirection = (waypointIndexDirection == 1) ? -1 : 1;
            }

            currentWaypointIndex += waypointIndexDirection;
            currentWaypoint = waypoints.get(currentWaypointIndex);
        }
    }

    private void enableMove(GameObject object, GameWorld world) {
        currentWaypoint = waypoints.get(currentWaypointIndex);

        setState(AngelPatrolState.MOVE);

        locationAtStartOfMove.set(object.getPosition());
        currentTarget.set(currentWaypoint.getLocation().x * world.getLevel().getCellSize(), 0, currentWaypoint.getLocation().y * world.getLevel().getCellSize());
        moveDirection.set(VectorMath.directionTo(locationAtStartOfMove, currentTarget));
        distanceToTarget = locationAtStartOfMove.dst(currentTarget);
    }

    private void enableWait(GameObject object, GameWorld world) {
        setState(AngelPatrolState.WAIT);

        currentInstructionIndex = 0;
        updateCurrentInstruction(object, world);
    }

    private void updateCurrentInstruction(GameObject object, GameWorld world) {
        currentInstruction = currentWaypoint.getInstructions().get(currentInstructionIndex);
        timeAtStartOfInstruction = System.currentTimeMillis();

        faceDirection.set(currentInstruction.getDirection().x, 0, currentInstruction.getDirection().y);
        direction.update(faceDirection);
    }

    private Quaternion calculateRotation(GameObject object, float angle) {
        float current = object.getRotation().getAngleAround(0, 1, 0);
        rotation.set(0, 1, 0, angle - current);
        return rotation;
    }

    private void updateMove(GameObject object, GameWorld world) {
        temp.set(moveDirection).scl(MOVE_SPEED);
    	object.setVelocity(temp);
    	
        if(object.getPosition().dst(locationAtStartOfMove) >= distanceToTarget) {
            object.setPosition(currentTarget);
            nextWaypoint();
            enableWait(object, world);
        } else {
            direction.update(object.getVelocity());
        }
    }

    private void updateWait(GameObject object, GameWorld world) {
        object.setPosition(currentTarget);
        object.setVelocity(0, 0, 0);

        if(TimeUtils.timeSinceMillis(timeAtStartOfInstruction) >= (long)(currentInstruction.getWaitTimeSeconds()*1000)) {
            currentInstructionIndex++;
            if(currentInstructionIndex > currentWaypoint.getInstructions().size()-1) {
                enableMove(object, world);
            } else {
                updateCurrentInstruction(object, world);
            }
        }
    }

    @Override
    public void update(GameObject object, GameWorld world, SpriteDecalGraphicsComponent graphicsComponent) {
        super.update(object, world, graphicsComponent);

        // Initial state...
        if(currentState == null) {
            enableMove(object, world);

            Iterator iter = sightBoxes.entrySet().iterator();
            while(iter.hasNext()) {
                Map.Entry entry = (Map.Entry)iter.next();
                PhysicalEntity box = (PhysicalEntity)entry.getValue();
                box.setPosition(object.getPosition());
            }
        }

        switch ((AngelPatrolState)currentState) {

            case MOVE:
                updateMove(object, world);
                break;

            case WAIT:
                updateWait(object, world);
                break;

            default:
                enableMove(object, world);
        }

        if(!direction.equals(previousDirection)) {
            if (currentSightBox != null) {
                world.unregisterCollisionObject(currentSightBox.getPhysicsComponent());
            }

            if (direction.getCurrent() == Direction.DOWN) {
                currentSightBox = sightBoxes.get(Direction.DOWN);
                currentSightBox.setPosition(new Vector3(object.getPosition()).add(0, 0, VISION_AREA_LENGTH));
            } else if (direction.getCurrent() == Direction.UP) {
                currentSightBox = sightBoxes.get(Direction.UP);
                currentSightBox.setPosition(new Vector3(object.getPosition()).add(0, 0, -VISION_AREA_LENGTH));
            } else if (direction.getCurrent() == Direction.LEFT) {
                currentSightBox = sightBoxes.get(Direction.LEFT);
                currentSightBox.setPosition(new Vector3(object.getPosition()).add(-VISION_AREA_LENGTH, 0, 0));
            } else if (direction.getCurrent() == Direction.RIGHT) {
                currentSightBox = sightBoxes.get(Direction.RIGHT);
                currentSightBox.setPosition(new Vector3(object.getPosition()).add(VISION_AREA_LENGTH, 0, 0));
            }

            world.registerTriggerEntity(currentSightBox.getPhysicsComponent(), object);
        }

        Iterator iter = sightBoxes.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            PhysicalEntity box = (PhysicalEntity)entry.getValue();
            box.setVelocity(object.getVelocity());
            box.update(world);
        }

        updateAnimation(object, graphicsComponent);
        previousDirection.setCurrent(direction.getCurrent());
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

    public void removeSight(GameWorld world) {
        world.unregisterCollisionObject(currentSightBox.getPhysicsComponent());
    }

    @Override
    public void dispose() {

        Iterator iter = sightBoxes.entrySet().iterator();
        while(iter.hasNext()) {
            PhysicalEntity physicalEntity = (PhysicalEntity)((Map.Entry)iter.next()).getValue();
            physicalEntity.dispose();
        }

        super.dispose();

    }
}
