package com.henrik.advergame.leveleditor.shared.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.henrik.advergame.leveleditor.Point;
import com.henrik.advergame.leveleditor.shared.MapEntityState;

/**
 * Created by Henri on 05/03/2015.
 */
public class PushBlockEntity extends MapEntity {

    private int width, height;

    public PushBlockEntity(Point location) {
        super(location, MapEntityState.PUSH_BLOCK.getID());

        width = 1;
        height = 1;
    }

    public void setNewCorner(Point location) {
        width = location.x - this.location.x + 1;
        height = location.y - this.location.y + 1;

        if(this.location.x > location.x || this.location.y > location.y) {
            this.location = location;
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    @Override
    public String build() {
        String str = "";
        str += String.valueOf(MapEntityState.PUSH_BLOCK.getID()) + "#";

        Vector2 worldPos = EditorToWorld(location.x, location.y);

        str += String.valueOf(worldPos.x) + "," + String.valueOf(worldPos.y);

        str += ":";

        str += String.valueOf(width) + POINT_DELIM + String.valueOf(height);

        return str;
    }

    protected PushBlockEntity(String data) {
        String[] exclID = data.split(ID_DELIM);
        String[] splitProperties = exclID[1].split(POS_DELIM);
        id = Integer.parseInt(exclID[0]);

        String[] split = data.split(POS_DELIM);
        String[] widthHeight = split[1].split(POINT_DELIM);
        this.width = Integer.parseInt(widthHeight[0]);
        this.height = Integer.parseInt(widthHeight[1]);

        // Calculate bottom-left position
        String[] position = splitProperties[0].split(POINT_DELIM);
        float xParse = Float.parseFloat(position[0]);
        float yParse = Float.parseFloat(position[1]);

        location = WorldToEditor(xParse, yParse);
    }

    /**
     * Convert the coordinates of a push block entity from game world world to editor.
     */
    public Point WorldToEditor(float x, float y) {
        x += 0.5f;
        y += 0.5f;

        x -= (float)width/2f;
        y -= (float)height/2f;

        return new Point((int)x, (int)y);
    }

    /**
     * Convert the coordinates of a push block entity from editor to game world.
     */
    public Vector2 EditorToWorld(int x, int y) {
        // Calculate true center location
        float trueX = (float)x + ((float)width/2f);
        float trueY = (float)y + ((float)height/2f);

        trueX -= 0.5f;
        trueY -= 0.5f;

        return new Vector2(trueX, trueY);
    }

    public static PushBlockEntity Parse(String data) {
        return new PushBlockEntity(data);
    }
}
