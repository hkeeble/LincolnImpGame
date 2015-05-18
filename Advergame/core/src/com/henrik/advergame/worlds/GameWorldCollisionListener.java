package com.henrik.advergame.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.ContactListener;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btManifoldPoint;
import com.henrik.advergame.entities.LevelEntity;
import com.henrik.advergame.entities.PhysicalEntity;
import com.henrik.advergame.systems.CannonBall;
import com.henrik.advergame.systems.DestructableObject;
import com.henrik.advergame.systems.Player;
import com.henrik.advergame.systems.TriggeredMessageSequence;
import com.henrik.advergame.systems.mapentities.*;
import com.henrik.advergame.utils.CollisionTags;
import com.henrik.advergame.utils.RandomUtils;
import com.henrik.advergame.utils.VectorMath;
import com.henrik.gdxFramework.core.CollisionObject;
import com.henrik.gdxFramework.core.CollisionTag;
import com.henrik.gdxFramework.core.CollisionWorld;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.PhysicsComponent;

/**
 * Created by Henri on 28/01/2015.
 */
public class GameWorldCollisionListener extends ContactListener {

    GameWorld world; // Reference to the game world

    public GameWorldCollisionListener(GameWorld world) {
        super();

        this.world = world;
    }

    @Override
    public boolean onContactAdded(btManifoldPoint cp, btCollisionObject colObj0, int partId0, int index0, btCollisionObject colObj1, int partId1, int index1) {

        // Get collision objects from user data
        CollisionObject obj0 = (CollisionObject)colObj0.userData;
        CollisionObject obj1 = (CollisionObject)colObj1.userData;

        /// Get the collision tags of the objects involved in the collision
        CollisionTag obj0Tag = obj0.getCollisionTag();
        CollisionTag obj1Tag = obj1.getCollisionTag();

        // Check for attack
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER_ACTION_BOX, CollisionTags.OBJECT)) {
            if(obj0Tag == CollisionTags.PLAYER_ACTION_BOX) {
                handleAttack(obj0, obj1);
            }
            else {
                handleAttack(obj1, obj0);
            }
        }

        // Check for hit-switch collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER_ACTION_BOX, CollisionTags.SWITCH)) {
            if(obj0Tag == CollisionTags.SWITCH) {
                handleSwitchActivate(obj0);
            } else {
                handleSwitchActivate(obj1);
            }
            return false;
        }

        // Check for player-teleporter collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER_ACTION_BOX, CollisionTags.TELEPORTER)) {
            if(obj1Tag == CollisionTags.TELEPORTER) {
                handleTeleport(obj1, obj0);
            } else {
                handleTeleport(obj0, obj1);
            }
            return false;
        }

        // If we have an attack and no object, ignore it
        if(obj0Tag == CollisionTags.PLAYER_ACTION_BOX || obj1Tag == CollisionTags.PLAYER_ACTION_BOX) {
            return false;
        }

        // Check for angel-player collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER,  CollisionTags.ANGEL) || isCollisionPair(obj0, obj1, CollisionTags.PLAYER,  CollisionTags.ANGEL_DOG)) {
            if(obj1Tag == CollisionTags.ANGEL || obj1Tag == CollisionTags.ANGEL_DOG) {
                handlePlayerHit(obj1);
            } else {
                handlePlayerHit(obj0);
            }
            return false;
        }

        // Check for player-key collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.KEY)) {
            if(obj0Tag == CollisionTags.KEY) {
                handleKeyCollection(obj0);
            } else {
                handleKeyCollection(obj1);
            }
            return false;
        }

        // Check for player-door collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.DOOR)) {
            if(obj0Tag == CollisionTags.DOOR) {
                handleDoorAttempt(obj0);
            } else {
                handleDoorAttempt(obj1);
            }
        }

        // Check for player-exit collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.EXIT)) {
            if (obj0Tag == CollisionTags.PLAYER) {
                handleExitAttempt(obj0, obj1);
            } else {
                handleExitAttempt(obj1, obj0);
            }
        }

        // Check for angel-object collision (ignore this physically)
        if(isCollisionPair(obj0, obj1, CollisionTags.ANGEL, CollisionTags.OBJECT)) {
            return false;
        }

        // Collisions that destroy cannon balls
        if (isCollisionPair(obj0, obj1, CollisionTags.STATIC_SOLID, CollisionTags.CANNON_BALL) ||
            isCollisionPair(obj0, obj1, CollisionTags.PUSH_BLOCK, CollisionTags.CANNON_BALL) ||
            isCollisionPair(obj0, obj1, CollisionTags.DOOR, CollisionTags.CANNON_BALL) ||
            isCollisionPair(obj0, obj1, CollisionTags.EXIT, CollisionTags.CANNON_BALL) ||
            isCollisionPair(obj0, obj1, CollisionTags.SWITCH_DOOR, CollisionTags.CANNON_BALL) ||
            isCollisionPair(obj0, obj1, CollisionTags.KEY, CollisionTags.CANNON_BALL)) {
            if (obj0Tag == CollisionTags.CANNON_BALL) {
                ((CannonBall) obj0.getObject()).setMarkedForRemoval(true);
            } else {
                ((CannonBall) obj1.getObject()).setMarkedForRemoval(true);
            }
            return false;
        }

        // Check for player-cannonball collision
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.CANNON_BALL)) {
            if (obj0Tag == CollisionTags.CANNON_BALL) {
                ((CannonBall) obj0.getObject()).setMarkedForRemoval(true);
                handlePlayerHitCannon(obj0);
            } else {
                ((CannonBall) obj1.getObject()).setMarkedForRemoval(true);
                handlePlayerHitCannon(obj1);
            }
            return false;
        }

        // Cannon-ball unaffected by cannons
        if(isCollisionPair(obj0, obj1, CollisionTags.CANNON, CollisionTags.CANNON_BALL)) {
            return false;
        }

        // Check for player-angel sight collision
        if(obj0Tag == CollisionTags.ANGEL_SIGHT_BOX || obj1Tag == CollisionTags.ANGEL_SIGHT_BOX) {
            if (isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.ANGEL_SIGHT_BOX)) {
                if(obj0Tag == CollisionTags.ANGEL_SIGHT_BOX) {
                    if(isPlayerInSight(obj0.getObject(), world.getPlayer(), world)) {
                        world.getSessionManager().setPlayerSighted(true);
                        Angel angel = ((Angel)obj0.getObject());
                        if(!angel.isPlayerSighted()) {
                            angel.setPlayerSighted(true);
                            world.getCameraController().orbit(angel.getPosition(), 5f, 0.05f, 3f);
                            world.addMessage(Angel.SIGHTING_MESSAGES[RandomUtils.randInt(0, Angel.SIGHTING_MESSAGES.length - 1)], 5000, angel);
                        }
                    }
                } else {
                    if(isPlayerInSight(obj1.getObject(), world.getPlayer(), world)) {
                        world.getSessionManager().setPlayerSighted(true);
                        Angel angel = ((Angel)obj1.getObject());
                        if(!angel.isPlayerSighted()) {
                            angel.setPlayerSighted(true);
                            world.getCameraController().orbit(angel.getPosition(), 5f, 0.05f, 3f);
                            world.addMessage(Angel.SIGHTING_MESSAGES[RandomUtils.randInt(0, Angel.SIGHTING_MESSAGES.length - 1)], 5000, angel);
                        }
                    }
                }
            }

            return false;
        }

        // Check for cannonball-destructable block collision
        if(isCollisionPair(obj0, obj1, CollisionTags.CANNON_BALL, CollisionTags.DESTRUCTABLE_BLOCK)) {
            if(obj0Tag == CollisionTags.CANNON_BALL) {
                ((CannonBall) obj0.getObject()).setMarkedForRemoval(true);
                ((DestructableBlock) obj1.getObject()).setMarkedForRemoval(true);
            } else {
                ((CannonBall) obj1.getObject()).setMarkedForRemoval(true);
                ((DestructableBlock) obj0.getObject()).setMarkedForRemoval(true);
            }
        }

        // Check for player collision with message sequence
        if(isCollisionPair(obj0, obj1, CollisionTags.PLAYER, CollisionTags.MESSAGE_TRIGGER)) {
            if(obj0Tag == CollisionTags.MESSAGE_TRIGGER) {
                world.showTriggeredMessageSequence(((TriggeredMessageSequence)obj0.getObject()));
            } else {
                world.showTriggeredMessageSequence(((TriggeredMessageSequence)obj1.getObject()));
            }

            return false;
        }

        if(obj0Tag == CollisionTags.MESSAGE_TRIGGER || obj1Tag == CollisionTags.MESSAGE_TRIGGER) {
            return false;
        }

        // Check for angel dog and wall collision
        if(isCollisionPair(obj0, obj1, CollisionTags.ANGEL_DOG, CollisionTags.STATIC_SOLID) || isCollisionPair(obj0, obj1, CollisionTags.ANGEL_DOG, CollisionTags.PUSH_BLOCK)) {
            if(obj0Tag == CollisionTags.ANGEL_DOG) {
                ((AngelDog)obj0.getObject()).reverseDirection();
            } else {
                ((AngelDog)obj1.getObject()).reverseDirection();
            }
        }

        // If we have not returned before this point, objects should react physically to each other
        handleCollisions(obj0, obj1, cp);

        return false;
    }

    /**
     * Checks if the player is in sight of the given object. If they are, records last sighting time and returns true.
     * @param object The object.
     * @param player The player object.
     * @param world The game world.
     */
    private Vector3 temp = new Vector3();
    private Vector3 temp2 = new Vector3();
    private boolean isPlayerInSight(GameObject object, GameObject player, GameWorld world) {
        player.getPosition(temp);
        object.getPosition(temp2);
    	
    	CollisionObject obj = world.rayTestFirst(temp2, temp, CollisionWorld.CollisionFilter.ALL.getValue(),
                (short)(CollisionWorld.CollisionFilter.DYNAMIC.getValue() | CollisionWorld.CollisionFilter.STATIC.getValue()));

        if(obj != null) {
            if (obj.getCollisionTag() == CollisionTags.PLAYER)
                return true;
        }

        return false;
    }

    private void handlePlayerHitCannon(CollisionObject cannonBall) {
        Player player = world.getPlayer();

        PhysicsComponent physicsComponent = ((PhysicalEntity)cannonBall.getObject()).getPhysicsComponent();

        if(!physicsComponent.isCollisionHandled()) {
            physicsComponent.setCollisionHandled(true);
            player.damage(3);

            world.playPlayerHurtSound();

            if(player.isDead()) {
                world.getSessionManager().setGameOver(true);
            }
        }
    }

    private void handlePlayerHit(CollisionObject angel) {

        Player player = world.getPlayer();

        PhysicsComponent physicsComponentAngel = ((PhysicalEntity)angel.getObject()).getPhysicsComponent();

        if(!physicsComponentAngel.isCollisionHandled()) {

            if(player.canBeHit()) {
                physicsComponentAngel.setCollisionHandled(true);

                player.damage(2);

                world.playPlayerHurtSound();

                if(player.isDead()) {
                    world.getSessionManager().setGameOver(true);
                }
            }
        }
    }

    private void handleAttack(CollisionObject attackObject, CollisionObject receiver) {

        DestructableObject object = (DestructableObject)receiver.getObject();
        PhysicsComponent physComponent = object.getPhysicsComponent();

        if(!physComponent.isCollisionHandled()) {
            if(object.canBeHit()) {
                object.damage(1);

                world.playHitSound();

                // Remove object attack object
                world.unregisterCollisionObject(((PhysicalEntity) attackObject.getObject()).getPhysicsComponent());

                if (object.isDestroyed()) {
                    object.setMarkedForRemoval(true);
                    world.getSessionManager().addObjectDestroyed();
                }

            }
            physComponent.setCollisionHandled(true);
        }

    }

    private void handleKeyCollection(CollisionObject key) {
        Key keyEntity = (Key)key.getObject();
        PhysicsComponent physicsComponent = keyEntity.getPhysicsComponent();

        if(!physicsComponent.isCollisionHandled()) {
            keyEntity.setMarkedForRemoval(true);
            world.getSessionManager().addKey();
            physicsComponent.setCollisionHandled(true);
        }
    }

    private void handleDoorAttempt(CollisionObject door) {
        Door doorEntity = (Door)door.getObject();
        PhysicsComponent physicsComponent = doorEntity.getPhysicsComponent();

        if(!physicsComponent.isCollisionHandled()) {
            if(world.getSessionManager().hasKey()) {
                doorEntity.setMarkedForRemoval(true);
                world.getSessionManager().removeKey();
                physicsComponent.setCollisionHandled(true);
            }
        }
    }

    private void handleSwitchActivate(CollisionObject switchObject) {
        Switch switchEntity = (Switch)switchObject.getObject();

        // If not yet activated, remove all doors
        if(!switchEntity.isActivated()) {
            switchEntity.activate();
            for (Door door : switchEntity.getDoors()) {
                door.setMarkedForRemoval(true);
            }
        }

    }

    private void handleTeleport(CollisionObject teleporter, CollisionObject actionObject)
    {
        Teleporter teleporterObject = (Teleporter)teleporter.getObject();

        world.playTeleportSound();

        world.getPlayer().setPosition(new Vector3(teleporterObject.getPartner().getPosition()).add(2, 0, 0));

        teleporterObject.getPhysicsComponent().setCollisionHandled(true);

    }

    /**
     * Handle an object colliding with another static object (player or angel with wall, for example)
     */
    public void handleCollisions(CollisionObject obj0, CollisionObject obj1, btManifoldPoint cp) {
        Vector3 normal = new Vector3(0, 0, 0);
        cp.getNormalWorldOnB(normal);
        Vector3 posDelta = normal.scl(cp.getDistance());

        CollisionTag obj0Tag = obj0.getCollisionTag();
        CollisionTag obj1Tag = obj1.getCollisionTag();

        GameObject object0 = obj0.getObject();
        GameObject object1 = obj1.getObject();

        PhysicsComponent physicsComponent0 = ((PhysicalEntity)object0).getPhysicsComponent();
        PhysicsComponent physicsComponent1 = ((PhysicalEntity)object1).getPhysicsComponent();

        short flags0 = (short)physicsComponent0.getCollisionObject().getCollisionFlags();
        short flags1 = (short)physicsComponent1.getCollisionObject().getCollisionFlags();

        // Object A changes
        if ((short) (flags0 & btCollisionObject.CollisionFlags.CF_STATIC_OBJECT) != btCollisionObject.CollisionFlags.CF_STATIC_OBJECT && !physicsComponent0.isKinematic()) {
            Vector3 temp = new Vector3(), temp2 = new Vector3();
            temp.set(normal).scl(temp2.set(object0.getVelocity()).dot(normal));
            object0.getVelocity().sub(temp);
            physicsComponent0.addCollisionChange(posDelta);
        }

        // Object B changes
        if((short)(flags1 & btCollisionObject.CollisionFlags.CF_STATIC_OBJECT) != btCollisionObject.CollisionFlags.CF_STATIC_OBJECT && !physicsComponent1.isKinematic()) {
            Vector3 temp = new Vector3(), temp2 = new Vector3();
            temp.set(normal).scl(temp2.set(object1.getVelocity()).dot(normal));
            object1.getVelocity().sub(temp);
            physicsComponent1.addCollisionChange(new Vector3(-posDelta.x, -posDelta.y, -posDelta.z));
        }
    }

    private void handleExitAttempt(CollisionObject player, CollisionObject exit) {
        PhysicsComponent physicsComponent = ((PhysicalEntity)exit.getObject()).getPhysicsComponent();

        world.getSessionManager().setPlayerExitAttempt(true);

        physicsComponent.setCollisionHandled(true);
    }

    /**
     * Checks of object A and object B are instances of collision tag A and B colliding.
     * @return
     */
    private boolean isCollisionPair(CollisionObject objA,  CollisionObject objB, CollisionTag a, CollisionTag b) {
        return (objA.getCollisionTag() == a && objB.getCollisionTag() == b) || (objB.getCollisionTag() == a && objA.getCollisionTag() == b);
    }
}
