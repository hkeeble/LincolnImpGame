package com.henrik.advergame.leveleditor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;

/**
 * Created by Henri on 18/11/2014.
 */
public class HUD extends Stage {

    OrthographicCamera camera;
    Viewport viewport;
    private ArrayList<Table> tableList;
    private ArrayList<Label> labelList;
    private ArrayList<TextButton> buttonList;
    private ArrayList<Image> imageList;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;
    private static final float ASPECT_RATIO = (float)WIDTH/(float)HEIGHT;

    public HUD() {
        super();

        float aspectRatio = (float) Gdx.graphics.getWidth()/(float) Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WIDTH, HEIGHT, camera);
        setViewport(viewport);

        tableList = new ArrayList<Table>();
        labelList = new ArrayList<Label>();
        buttonList = new ArrayList<TextButton>();
        imageList = new ArrayList<Image>();
    }

    public void resize(int width, int height) {
        viewport.update(width, height);
        camera.position.set((float)WIDTH/2,(float)HEIGHT/2,0);
    }

    public void render() {
        act();
        draw();
    }

    public TextButton addButton(float x, float y, String text, EventListener event, Drawable buttonUp, Drawable buttonDown, Drawable buttonChecked, BitmapFont font) {
        TextButton button = HUD.makeTextButton(x, y, text, event, buttonUp, buttonDown, buttonChecked, font);
        buttonList.add(button);
        addActor(buttonList.get(buttonList.size() - 1));
        return buttonList.get(buttonList.size() - 1);
    }

    public static TextButton makeTextButton(float x, float y, String text, EventListener event, Drawable buttonUp, Drawable buttonDown, Drawable buttonChecked, BitmapFont font) {
        TextButton button = new TextButton(text, new TextButton.TextButtonStyle(buttonUp, buttonDown, buttonChecked, font));
        button.setPosition(x, y);
        button.addListener(event);
        return button;
    }

    public static TextButton makeTextButton(float x, float y, String text, EventListener event, Skin skin) {
        TextButton button = new TextButton(text, skin);
        button.setPosition(x, y);
        button.addListener(event);
        return button;
    }

    public static Button makeButton(float x, float y, EventListener event, Drawable buttonUp, Drawable buttonDown, Drawable buttonChecked) {
        Button button = new Button(new Button.ButtonStyle(buttonUp, buttonDown, buttonChecked));
        button.setPosition(x, y);
        button.addListener(event);
        return button;
    }

    public void addButton(Button button) {
        addActor(button);
    }

    public Label addLabel(float x, float y, Color color, String text, BitmapFont font) {
        Label label = HUD.makeLabel(x, y, color, text, font);
        addActor(labelList.get(labelList.size() - 1));
        return labelList.get(labelList.size()-1);
    }

    public static Label makeLabel(float x, float y, Color color, String text, BitmapFont font) {
        Label label = new Label(text, new Label.LabelStyle(font, color));
        label.setPosition(x, y);
        return label;
    }

    public void addLabel(Label label) {
        addActor(label);
    }

    public Table addTable(float x, float y) {
        tableList.add(new Table());
        tableList.get(tableList.size()-1).setPosition(x, y);
        addActor(tableList.get(tableList.size() - 1));
        return tableList.get(tableList.size()-1);
    }

    public void addTable(Table table) {
        addActor(table);
    }

    public static Image makeImage(TextureRegion textureRegion) {
        Image image = new Image(textureRegion);
        return image;
    }

    public Image addImage(TextureRegion textureRegion) {
        Image image = makeImage(textureRegion);
        imageList.add(image);
        addActor(imageList.get(imageList.size() - 1));
        return imageList.get(imageList.size() - 1);
    }

    public void addImage(Image image) { addActor(image); }

    public void clear() {
        super.clear();
        labelList.clear();
        buttonList.clear();
        tableList.clear();
        imageList.clear();
    }
}
