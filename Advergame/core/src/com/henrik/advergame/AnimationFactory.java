package com.henrik.advergame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.henrik.gdxFramework.core.AnimationType;
import com.henrik.gdxFramework.core.AnimationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Henri on 04/02/2015.
 */
public class AnimationFactory {

    /**
     * Creates the player's animation set from the given texture.
     * @return
     */
    public static HashMap<AnimationType,Animation> createPlayerSet(Texture spriteSheet) {

        HashMap<AnimationType,Animation> set = new HashMap<AnimationType, Animation>();

        ArrayList<TextureRegion[]> anims = AnimationUtils.createSprite(spriteSheet, 128, 128);

        // Walking
        set.put(AnimationTypePlayer.WalkDown, new Animation(0.3f, anims.get(0)));
        set.put(AnimationTypePlayer.WalkLeft, new Animation(0.3f, anims.get(1)));
        set.put(AnimationTypePlayer.WalkRight, new Animation(0.3f, anims.get(2)));
        set.put(AnimationTypePlayer.WalkUp, new Animation(0.3f, anims.get(3)));

        // Running
        set.put(AnimationTypePlayer.RunDown, new Animation(0.6f, anims.get(0)));
        set.put(AnimationTypePlayer.RunLeft, new Animation(0.6f, anims.get(1)));
        set.put(AnimationTypePlayer.RunRight, new Animation(0.6f, anims.get(2)));
        set.put(AnimationTypePlayer.RunUp, new Animation(0.6f, anims.get(3)));

        // Attacking
        set.put(AnimationTypePlayer.AttackDown, new Animation(0.3f, anims.get(4)));
        set.put(AnimationTypePlayer.AttackLeft, new Animation(0.3f, anims.get(5)));
        set.put(AnimationTypePlayer.AttackRight, new Animation(0.3f, anims.get(6)));
        set.put(AnimationTypePlayer.AttackUp, new Animation(0.3f, anims.get(7)));

        // Set all to loop
        Iterator iter = set.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            Animation anim = (Animation)entry.getValue();
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }

        return set;

    }

    /**
     * Creates a generic movement animation set.
     * @return
     */
    public static HashMap<AnimationType,Animation> createMoveSet(Texture spriteSheet, int frameWidth, int frameHeight, float frameDuration) {
        HashMap<AnimationType,Animation> set = new HashMap<AnimationType, Animation>();

        ArrayList<TextureRegion[]> anims = AnimationUtils.createSprite(spriteSheet, frameWidth, frameHeight);

        // Walking
        set.put(AnimationTypeMove.WalkDown, new Animation(frameDuration, anims.get(0)));
        set.put(AnimationTypeMove.WalkLeft, new Animation(frameDuration, anims.get(1)));
        set.put(AnimationTypeMove.WalkRight, new Animation(frameDuration, anims.get(2)));
        set.put(AnimationTypeMove.WalkUp, new Animation(frameDuration, anims.get(3)));

        // Set all to loop
        Iterator iter = set.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            Animation anim = (Animation)entry.getValue();
            anim.setPlayMode(Animation.PlayMode.LOOP);
        }

        return set;
    }
}

// 128x160