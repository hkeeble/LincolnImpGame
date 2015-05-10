package com.henrik.advergame.components;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.utils.TimeUtils;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;

/**
 * A component given to an entity with a health bar.
 */
public class HealthComponent extends DecalGraphicsComponent {

    private int maxHealth;
    private int health;
    private int currentImageHealth;

    private int blockSize = 16;

    private Texture currentTexture;

    private final int BORDER_SIZE = 2;

    private final long TIME_TO_HIDE = 4000; // time, in millis, to show health bar for after a change in health
    private long timeAtLastChange;

    private boolean canBeHit;
    private long cooldownTime; // Cooldown time between being hit, in milliseconds
    private long timeAtLastDamage;

    /**
     *
     * @param maxHealth The maximum health for this health component.
     * @param cooldownTime The cooldown time, in milliseconds, between damage to this component.
     */
    public HealthComponent(int maxHealth, long cooldownTime) {
        super();

        this.maxHealth = maxHealth;
        this.cooldownTime = cooldownTime;

        health = maxHealth;
        currentImageHealth = maxHealth;

        currentTexture = new Texture(buildHealthBar());
        decal = Decal.newDecal(2, 0.5f, new TextureRegion(currentTexture), true);

        canBeHit = true;
    }

    public boolean canBeHit() {
        return canBeHit;
    }

    public void damage(int amount) {

        if(canBeHit) {
            health -= amount;
            if (health < 0)
                health = 0;

            timeAtLastChange = System.currentTimeMillis();
            timeAtLastDamage = System.currentTimeMillis();
            canBeHit = false;
        }

    }

    public void restore(int amount) {
        health += amount;
        if(health > maxHealth)
            health = maxHealth;

        timeAtLastChange = System.currentTimeMillis();
    }

    public void refill() {
        health = maxHealth;
    }

    public int getHealth() { return health; }

    public void setCoolDownActive(boolean active) {
        if(active)
            canBeHit = false;
        else
            canBeHit = true;
    }

    public boolean isDead() {
        return health <= 0;
    }

    private Pixmap buildHealthBar() {
        Pixmap pixmap = new Pixmap((blockSize*maxHealth) + (BORDER_SIZE*2), blockSize + (BORDER_SIZE*2), Pixmap.Format.RGBA8888);

        // Build base bar
        for(int x = 0; x < pixmap.getWidth(); x++) {
            for(int y = 0; y < pixmap.getHeight(); y++) {

                if(x > 2 && x < pixmap.getWidth()-BORDER_SIZE && y > BORDER_SIZE && y < pixmap.getHeight()-BORDER_SIZE)
                    pixmap.setColor(Color.BLACK);
                else
                    pixmap.setColor(Color.GRAY);

                pixmap.drawPixel(x, y);
            }
        }

        // Build health bar
        for(int i = 0; i < health; i++) {
            for(int x = (i*blockSize)+BORDER_SIZE; x < ((i*blockSize) + BORDER_SIZE) + blockSize; x++) {
                for(int y = BORDER_SIZE; y < pixmap.getHeight()-BORDER_SIZE; y++) {
                    pixmap.setColor(Color.PURPLE);
                    pixmap.drawPixel(x, y);
                }
            }
        }

        return pixmap;
    }

    public void update() {
        if(health != currentImageHealth) {
            currentTexture = new Texture(buildHealthBar());
            decal.setTextureRegion(new TextureRegion(currentTexture));
            currentImageHealth = health;
        }

        if(!canBeHit) {
            if(TimeUtils.timeSinceMillis(timeAtLastDamage) >= cooldownTime) {
                canBeHit = true;
            }
        }
    }

    public void render(Camera camera, Renderer renderer, GameObject object) {

        if(TimeUtils.timeSinceMillis(timeAtLastChange) < TIME_TO_HIDE) {
            renderOffset.set(0, 2, 0);
            super.render(camera, renderer, object);
        }

    }
}
