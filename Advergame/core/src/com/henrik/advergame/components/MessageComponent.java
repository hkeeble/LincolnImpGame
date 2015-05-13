package com.henrik.advergame.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.math.Matrix4;
import com.henrik.gdxFramework.core.Renderer;
import com.henrik.gdxFramework.entities.GameObject;
import com.henrik.gdxFramework.entities.components.DecalGraphicsComponent;

import java.util.ArrayList;

/**
 * Created by Henri on 05/02/2015.
 */
public class MessageComponent extends DecalGraphicsComponent {

    private static SpriteBatch batch;
    
    private FrameBuffer fbo;

    private final float DECAL_WIDTH_PER_CHAR = 0.2f;
    private final float DECAL_HEIGHT_PER_LINE = 0.8f;

    private final int CHARS_PER_LINE = 25;

    private final int VERTICAL_PADDING = 10;
    private final int HORIZONTAL_PADDING = 10;

    private int[] lineHeights;

    public MessageComponent(String text, BitmapFont font, Color textColor, TextureRegion background) {

        Color origColor = font.getColor();
        font.setColor(textColor);

        ArrayList<String> lines = new ArrayList<String>();

        // Split the text into lines
        int currentIndex = 0;
        do {

            // Build initial line
            ArrayList<Character> chars = new ArrayList<Character>();
            while(chars.size() < CHARS_PER_LINE && currentIndex < text.length()) {
                chars.add(text.charAt(currentIndex));
                currentIndex++;
            }

            // If not at the end, keep going until we find a space, or end of text (ensures we don't split words)
            if(currentIndex < text.length()) {
                if(!chars.get(chars.size()-1).equals(' ')) {
                    while(currentIndex < text.length() && !chars.get(chars.size()-1).equals(' ')) {
                        chars.add(text.charAt(currentIndex));
                        currentIndex++;
                    }
                }
            }

            // Build the string for this line
            StringBuilder builder = new StringBuilder();
            for(Character ch : chars) {
                builder.append(ch);
            }

            // Add this line
            lines.add(builder.toString());

        } while(currentIndex < text.length());

        GlyphLayout layout = new GlyphLayout();
        
        // Find the longest line
        int width = 0;
        int widestIndex = 0;
        for(int i = 0; i < lines.size(); i++) {
            layout.setText(font, lines.get(i));
        	int current = (int)layout.width;
            if(current > width) {
                width = current;
                widestIndex = i;
            }
        }

        // Find line heights
        lineHeights = new int[lines.size()];
        for(int i = 0; i < lines.size(); i++) {
        	layout.setText(font, lines.get(i));
            lineHeights[i] = (int)layout.height;
        }

        fbo = new FrameBuffer(Pixmap.Format.RGBA8888, width + (HORIZONTAL_PADDING*2), sum(lineHeights) + (VERTICAL_PADDING*2), false);
        
        batch = new SpriteBatch();
        TextureRegion tex = renderToTexture(font, lines, background);
        batch.dispose();
        
        decal = Decal.newDecal(DECAL_WIDTH_PER_CHAR * lines.get(widestIndex).length(), DECAL_HEIGHT_PER_LINE*lines.size(), tex, true);
        this.billboard = true;

        font.setColor(origColor);
    }

    private TextureRegion renderToTexture(BitmapFont font, ArrayList<String> lines, TextureRegion background) {
        // Set up an ortho projection matrix
        Matrix4 projMat = new Matrix4();
        projMat.setToOrtho2D(0, 0, fbo.getWidth(), fbo.getHeight());
        batch.setProjectionMatrix(projMat);

        // Render the text onto an FBO
        Gdx.gl.glDisable(GL20.GL_CULL_FACE);
        fbo.begin();
        batch.begin();
        batch.draw(background, 0, 0, fbo.getWidth(), fbo.getHeight());
        int height = 0;
        for(int i = 0; i < lines.size(); i++) {
            font.draw(batch, lines.get(i), HORIZONTAL_PADDING, fbo.getHeight() - height);
            height += lineHeights[i];
        }
        batch.end();
        fbo.end();
        Gdx.gl.glEnable(GL20.GL_CULL_FACE);

        // Flip the texture, and return it
        TextureRegion tex = new TextureRegion(fbo.getColorBufferTexture());
        tex.flip(false, true);
        return tex;
    }

    @Override
    public void render(Camera camera, Renderer renderer, GameObject object) {
        super.render(camera, renderer, object);
    }

    public void dispose() {
        fbo.dispose();
    }

    private int sum(int[] array) {
        int total = 0;
        for(int i = 0; i < array.length; i++)
            total += array[i];
        return total;
    }
}
