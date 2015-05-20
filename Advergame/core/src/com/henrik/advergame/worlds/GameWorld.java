package com.henrik.advergame.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.btIDebugDraw;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.henrik.advergame.*;
import com.henrik.advergame.cameras.GameCameraController;
import com.henrik.advergame.hud.HUDManager;
import com.henrik.advergame.level.Level;
import com.henrik.advergame.entities.*;
import com.henrik.advergame.pathfinding.KDTree;
import com.henrik.advergame.states.GameOver;
import com.henrik.advergame.states.LevelInterlude;
import com.henrik.advergame.systems.*;
import com.henrik.advergame.systems.MessageSequence;
import com.henrik.advergame.utils.RandomUtils;
import com.henrik.gdxFramework.core.*;
import com.henrik.gdxFramework.entities.GameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The main game world, handles the level, entities and game flow.
 */
public class GameWorld extends World {

    // First level message sequence pages....
    String[] firstLevelMsgSequence = {  "\"Looks like no one is home!\" (Tap to continue)",
                                        "\"Tap and drag on the bottom-left of the screen to make me move!\"",
                                        "\"And hit the button on the bottom right to hit stuff!\"",
                                        "*KeKeKeKeKe*" };

    private MessageSequence firstLevelMessageSequence;

    private AssetManager assetManager;  // Asset manager reference
    private HUD hud;                    // HUD reference
    private GameWorldCollisionListener listener; // The collision listener for the world

    // The current level
    private Level currentLevel;
    private int currentLevelNumber;

    // The player entity
    private Player player;

    // Timed messages
    private ArrayList<TimedMessage> timedMessages;

    // The session manager manages all details about the current game. It is able to inform the world of changes in game data.
    private SessionManager sessionManager;

    // The camera controller
    private GameCameraController cameraController;

    private final Vector3 CAMERA_FOLLOW_OFFSET = new Vector3(0, 7f, 3f);

    private MessageSequence currentMessageSequence;
    private boolean messageSequenceActive;

    private HUDManager hudManager;

    private Sound hitSound1, hitSound2, hitSound3;
    private Sound playerHurt;
    private Sound teleportSound;

    public GameWorld(GameBase game, AssetManager assetManager) {
        super(game);
        
        // Get HUD and assetManager references
        this.assetManager = assetManager;
        this.hud = game.hud;

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);

        // Retrieve game level
        currentLevelNumber = ((Game)game).getPlayerLevel();

        // Set up the environment
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(Color.WHITE, new Vector3(0, -5f, 0f)));

        // Construct player entity
        player = new Player(this, hud, assetManager, game.getInput());

        // Initialize the session manager, and attach all observers
        sessionManager = new SessionManager(this, assetManager.get("sounds/keyGet.wav", Sound.class));

        // Get sounds
        playerHurt = assetManager.get("sounds/hurt.wav", Sound.class);
        hitSound1 = assetManager.get("sounds/hit1.wav", Sound.class);
        hitSound2 = assetManager.get("sounds/hit2.wav", Sound.class);
        hitSound3 = assetManager.get("sounds/hit3.wav", Sound.class);
        teleportSound = assetManager.get("sounds/teleport.wav", Sound.class);

        game.enableProfiling(); // ENABLE PROFILING

        // Initialize game camer controller
        cameraController = new GameCameraController();
        setMainCameraController(cameraController);

        // Initialize timed messages
        timedMessages = new ArrayList<TimedMessage>();

        // Initialize some collision world variables, and the collision listener.
        setCollisionWorldDebugDrawMode(btIDebugDraw.DebugDrawModes.DBG_MAX_DEBUG_DRAW_MODE);
        listener = new GameWorldCollisionListener(this);

        currentMessageSequence = null;
        messageSequenceActive = false;

        // Initialize the HUD
        hudManager = new HUDManager(game, this, assetManager.get("sounds/select.wav", Sound.class));
        hudManager.enableMainHUD();

        firstLevelMessageSequence = new MessageSequence(firstLevelMsgSequence, Game.getFont("main", 3), Color.BLACK, Game.getUISkin().getRegion("speechBubble"), renderer);
    }

    /**
     * Generates a new level, and initializes the world with the new level.
     */
    public void loadLevel(int level) {

        // Set current level number
        currentLevelNumber = level;

        // Set the new last played level in the savegame
        ((Game)game).getSaveGame().setLastPlayedLevel(currentLevelNumber);

        reset();

        currentLevel = new Level(String.valueOf(level), 2, assetManager, game.getFont("main", 3), this);

        // Set player position
        player.setPosition(currentLevel.getPlayerStart().x * currentLevel.getCellSize(), 0, currentLevel.getPlayerStart().y * currentLevel.getCellSize());

        // Initialize the new collision world
        initializeCollisionWorld();

        cameraController.follow(player, CAMERA_FOLLOW_OFFSET, 0.1f); // Otherwise, set camera to follow player
    }

    public void resetLevel() {
        reset();

        clearCollisionWorld();

        currentLevel.Load(this, String.valueOf(currentLevelNumber));

        // Set player position
        player.setPosition(currentLevel.getPlayerStart().x * currentLevel.getCellSize(), 0, currentLevel.getPlayerStart().y * currentLevel.getCellSize());

        // Initialize the new collision world
        initializeCollisionWorld();

        cameraController.follow(player, CAMERA_FOLLOW_OFFSET, 0.1f); // Otherwise, set camera to follow player
    }

    private void reset() {
        // Reset all
        for(TimedMessage msg : timedMessages) {
            msg.dispose();
        }

        game.getInput().clear();
        timedMessages.clear();
        player.getControllerComponent().reset();
        sessionManager.reset();
        player.fillHealth();
    }

    /**
     * Used to initialize the collision world after a new level has been generated.
     */
    public void initializeCollisionWorld() {
        // Add the player entity to the collision world
        registerDynamicEntity(player.getPhysicsComponent(), player);

        // Add the level walls to the collision world
/*        ArrayList<ModelObject> walls = currentLevel.getModelEntities();
        for (ModelEntity e : walls) {
            registerStaticGeometry(e.getPhysicsComponent(), e);
        }*/

        // Add all static level entities to the collision world
        ArrayList<LevelEntity> staticEntities = currentLevel.getStaticEntities();
        for(LevelEntity entity : staticEntities) {
            registerStaticGeometry(entity.getPhysicsComponent(), entity);
        }

        // Add all dynamic level entities to the collision world
        ArrayList<LevelEntity> dynamicEntities = currentLevel.getDynamicEntities();
        for(PhysicalEntity entity : dynamicEntities) {
            registerDynamicEntity(entity.getPhysicsComponent(), entity);
        }

        // Add all triggered message sequences
        ArrayList<MessageSequence> messages = currentLevel.getMessageSequences();
        for(MessageSequence message : messages) {
            if(message instanceof TriggeredMessageSequence) {
                registerTriggerEntity(((TriggeredMessageSequence)message).getPhysicsComponent(), message);
            }
        }

        registerStaticGeometry(currentLevel.getExitEntity().getPhysicsComponent(), currentLevel.getExitEntity());
    }

    public void update() {

        if(!paused) {

            // Update all world objects
            currentLevel.update(this, game.getDebugRenderer(), player.getPosition(), game.getInput());

            if(!sessionManager.isPlayerSighted())
                player.update(this);

            // Update timed messages
            for(int i = 0; i < timedMessages.size(); i++) {
                timedMessages.get(i).update(game.getInput(), this);

                // Dispose when finished
                if(timedMessages.get(i).isFinished()) {
                    timedMessages.get(i).dispose();
                    timedMessages.remove(i);
                }
            }

            // Check for session manager changes (these happen here as new level is called, cannot be called mid-callback during collision)
            if (sessionManager.isGameOver()) {
                game.enableState(GameOver.class);
                return;
            }

            // Check if the player has exited the game
            if(sessionManager.isPlayerExitAttempt()) {

                // Save the game with a concrete reference to the game object
                Game gameConcrete = (Game)game;

                gameConcrete.getSaveGame().saveLevelData(currentLevelNumber, new LevelSaveData(currentLevelNumber, sessionManager.getScore(), sessionManager.getObjectsDestroyed(),
                        currentLevel.getObjectCount()));

                // Advance the level stored in savegame
                if(currentLevelNumber == gameConcrete.getPlayerLevel()) {
                    gameConcrete.getSaveGame().advanceLevel();
                }

                gameConcrete.saveGame();

                // Enter level interlude state
                game.enableState(LevelInterlude.class);
                return;
            }

            sessionManager.setPlayerExitAttempt(false);

            // Update the game HUD
            hudManager.update(sessionManager);

            if(currentLevelNumber == 1 && !firstLevelMessageSequence.isFinished()) {
                firstLevelMessageSequence.setPosition(new Vector3(player.getPosition().x , 2f, player.getPosition().z));
                showMessageSequence(firstLevelMessageSequence, new Vector3(player.getPosition().x , 2f, player.getPosition().z));
            }

        } else {
            updateMessageSequence();
        }

        super.update(); // Update camera and collision world
    }

    public void render() {
        renderer.clear();

        // Render timed messages
        for(int i = 0; i < timedMessages.size(); i++) {
            timedMessages.get(i).render(this);
        }

        firstLevelMessageSequence.render(this);

        player.render(this);
        currentLevel.render(this);

        super.render(false);
    }

    /**
     * Will trigger the display of a message sequence.
     * @param sequence The sequence to display.
     */
    public void showTriggeredMessageSequence(TriggeredMessageSequence sequence) {

        if(!sequence.getPhysicsComponent().isCollisionHandled()) {
            pause(false);

            if (sequence.getFocusLocation().equals(sequence.getPosition()))
                player.setPosition(sequence.getPosition().x, 0, sequence.getPosition().z + 0.01f);

            messageSequenceActive = true;

            cameraController.orbit(sequence.getFocusLocation(), 5f, 0.05f, 3f);
            currentMessageSequence = sequence;
            currentMessageSequence.show();

            game.getInput().clear();

            sequence.getPhysicsComponent().setCollisionHandled(true);
            unregisterCollisionObject(sequence.getPhysicsComponent());
        }
    }

    public void showMessageSequence(MessageSequence sequence, Vector3 focusPoint) {
        pause(false);

        messageSequenceActive = true;

        cameraController.orbit(focusPoint, 5f, 0.05f, 3f);
        currentMessageSequence = sequence;
        currentMessageSequence.show();

        game.getInput().clear();
    }

    /**
     * Updates the current message sequence, if it is active.
     */
    private void updateMessageSequence() {
        if(messageSequenceActive) {
            currentMessageSequence.update(game.getInput(), this);
            if (currentMessageSequence.isFinished()) {
                unPause();
                messageSequenceActive = false;
                cameraController.follow(player, CAMERA_FOLLOW_OFFSET, 0.1f);
            }
        }
    }

    /**
     * Pause the game, and optionally show the pause menu.
     * @param showPauseMenu
     */
    public void pause(boolean showPauseMenu) {
        if(!paused) {
            paused = true;
            player.getControllerComponent().deactivateHUD();

            if (showPauseMenu) {
                hudManager.enablePauseHUD();
            }
        }
    }

    /**
     * Unpasue the game
     */
    public void unPause() {
        if(paused) {
            paused = false;
            player.getControllerComponent().activateHUD();
            hudManager.enableMainHUD();
        }
    }

    /**
     * Adds a message to the world.
     */
    public void addMessage(String text, long millisToShow, Vector3 position) {
        timedMessages.add(new TimedMessage(text, Game.getFont("main", 3), Color.BLACK, Game.getUISkin().getRegion("speechBubble"), millisToShow, position, renderer));
        timedMessages.get(timedMessages.size() - 1).show();
    }

    /**
     * Adds a message to the world. The message will follow the given game object.
     */
    public void addMessage(String text, long millisToShow, GameObject source) {
        TimedMessage message = new TimedMessage(text, Game.getFont("main", 3), Color.BLACK, Game.getUISkin().getRegion("speechBubble"), millisToShow, source.getPosition(), renderer, source);
        timedMessages.add(message);
        message.show();
    }

    /**
     * Adds a message to the world that follows the given game object.
     */
    public void addMessage(TimedMessage message, GameObject source) {
    	message.setPosition(source.getPosition());
    	timedMessages.add(message);
    	message.show();
    }
    
    public void toggleNavDebug() {
        currentLevel.toggleNavDebug();
    }

    public Player getPlayer() {
        return player;
    }

    public Level getLevel() { return currentLevel; }

    public void dispose() {

        player.dispose();

        currentLevel.dispose();

        for(int i = 0; i < timedMessages.size(); i++) {
            timedMessages.get(i).dispose();
        }

        super.dispose();
    }

    public void playPlayerHurtSound() {
        playerHurt.play();
    }

    public void playHitSound() {

        int i = RandomUtils.randInt(0, 2);

        switch (i) {
            case 0:
                hitSound1.play();
                break;
            case 1:
                hitSound2.play();
                break;
            case 2:
                hitSound3.play();
                break;
        }

    }

    public void playTeleportSound() {
        teleportSound.play();
    }

    public GameCameraController getCameraController() {
        return cameraController;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public int getCurrentLevelNumber() { return currentLevelNumber; }

    public KDTree getNavMeshTree() { return currentLevel.getNavMesh().getTree(); }
}
