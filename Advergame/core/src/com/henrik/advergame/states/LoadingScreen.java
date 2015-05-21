package com.henrik.advergame.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.henrik.gdxFramework.core.AnimationUtils;
import com.henrik.gdxFramework.core.HUD;
import com.henrik.gdxFramework.core.LoadState;

/**
 * Created by Henri on 12/03/2015.
 */
public class LoadingScreen extends LoadState {

    private Texture loadingAnimTexture;
    private Animation loadAnimation;

    public LoadingScreen(AssetManager assetManager, HUD hud) {
        super(assetManager, hud);

        loadingAnimTexture = new Texture(Gdx.files.internal("sprites/loadingAnimation.png"));
        loadAnimation = AnimationUtils.createAnimation(loadingAnimTexture, 64, 64, 0.1f, Animation.PlayMode.LOOP);
    }

    @Override
    public void initialize() {
        super.initialize();
        hud.getMainTable().add(HUD.makeAnimatedImage(loadAnimation));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void dispose() {
        loadingAnimTexture.dispose();
        super.dispose();
    }

    @Override
    public void clear() {
        super.clear();
    }
}
