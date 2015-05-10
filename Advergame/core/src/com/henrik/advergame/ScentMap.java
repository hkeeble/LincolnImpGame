package com.henrik.advergame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.henrik.advergame.entities.ModelObject;
import com.henrik.advergame.utils.Point;
import com.henrik.gdxFramework.core.World;
import com.henrik.gdxFramework.entities.components.ModelGraphicsComponent;

import java.util.ArrayList;

/**
 * Represents a scent map.
 */
public class ScentMap {

    /**
     * Represents an individual cell within the scent map.
     */
    public class Cell {
        int scentValue;
        Point position;

        public Cell(Point position, int scentValue) {
            this.scentValue = scentValue;
            this.position = position;
        }

        public void setScentValue(int value) { this.scentValue = scentValue; }

        public int getScentValue() { return scentValue; }
        public Point getPosition() { return position; }
    }

    int playerScent;
    Cell[][] grid;

    // Using this for debugging visualization purposes
    Model model;
    ModelObject[][] debugOverlay;

    public ScentMap(int width, int height) {
        grid = new Cell[width][height];

        for(int x = 0; x < width; x++) {
            for(int y = 0; y < height; y++) {
                grid[x][y] = new Cell(new Point(x,y), 0);
            }
        }

        playerScent = 1;

        clear();
    }

    /**
     * Update the scent map values.
     * @param playerLocation The location of the player.
     */
    public void update(Point playerLocation, World world) {

        // Update scent value
        playerScent = playerScent + 1;
        grid[playerLocation.x][playerLocation.y].setScentValue(playerScent);

        // Update each individual grid cell
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[x].length; y++) {
                ArrayList<Cell> neighbourValues = getNeighbourValues(x, y);
                for(Cell cell : neighbourValues) {
                    if(cell.getScentValue() > grid[x][y].getScentValue())
                        grid[x][y].setScentValue(cell.getScentValue() - 1);
                }
            }
        }

        // SET UP DEBUG OVERLAY
        debugOverlay = new ModelObject[grid.length][grid[0].length];
        ModelBuilder builder = new ModelBuilder();
        Model model = builder.createBox(3, 0.1f, 3,  new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        Color red = Color.RED;
        Color blue = Color.BLUE;
        int highest = findHighest();

        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[x].length; y++) {
                Color color = blue;
                color.lerp(red, grid[x][y].getScentValue()/highest);
                ModelInstance instance = new ModelInstance(model);
                instance.materials.get(0).set(ColorAttribute.createDiffuse(color));
                debugOverlay[x][y] = new ModelObject(new ModelGraphicsComponent(instance));
                debugOverlay[x][y].setPosition(x * 3, -0.5f, y * 3);
            }
        }

        // DRAW DEBUG OVERLAY
        for(int x = 0; x < debugOverlay.length; x++) {
            for(int y = 0; y < debugOverlay[x].length; y++) {
                debugOverlay[x][y].update(world);
            }
        }

        for(int x = 0; x < debugOverlay.length; x++) {
            for(int y = 0; y < debugOverlay[x].length; y++) {
                System.out.print(String.valueOf(grid[x][y]) + " ");
            }
            System.out.println();
        }
    }

    /**
     * Use to check if the given location is a valid position within the grid.
     * @param x The x location to check.
     * @param y The y location to check.
     * @return True if the position is valid, false if not.
     */
    private boolean isValidPosition(int x, int y) {
        return x >= 0 && y >= 0 && x < grid.length && y < grid[0].length;
    }

    /**
     * Gets the values of all neighbouring locations to the given location.
     * @param x The x location to check for neighbours.
     * @param y The y lcoation to check for neighbours.
     * @return An array of values surrounding the given location.
     */
    public ArrayList<Cell> getNeighbourValues(int x, int y) {
        ArrayList<Cell> vals = new ArrayList<Cell>();

        if(isValidPosition(x+1, y)) // Right
            vals.add(grid[x+1][y]);
        if(isValidPosition(x-1, y)) // Left
            vals.add(grid[x-1][y]);
        if(isValidPosition(x, y+1)) // Down
            vals.add(grid[x][y+1]);
        if(isValidPosition(x, y-1)) // Up
            vals.add(grid[x][y-1]);
        if(isValidPosition(x+1, y+1))
            vals.add(grid[x+1][y+1]);
        if(isValidPosition(x-1, y-1))
            vals.add(grid[x-1][y-1]);
        if(isValidPosition(x-1, y+1))
            vals.add(grid[x-1][y+1]);
        if(isValidPosition(x+1, y-1))
            vals.add(grid[x+1][y-1]);

        return vals;
    }

    /**
     * Clears all values, setting each position to 0.
     */
    public void clear() {
        playerScent = 1;
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[0].length; y++) {
                grid[x][y].setScentValue(0);
            }
        }
    }

    private int findHighest() {
        int highest = 0;
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[x].length; y++) {
                if(grid[x][y].getScentValue() > highest)
                    highest = grid[x][y].getScentValue();
            }
        }
        return highest;
    }

    private int findLowest() {
        int lowest = findHighest();
        for(int x = 0; x < grid.length; x++) {
            for(int y = 0; y < grid[x].length; y++) {
                if(grid[x][y].getScentValue() < lowest)
                    lowest = grid[x][y].getScentValue();
            }
        }
        return lowest;
    }

    public Cell getCell(int x, int y) { return grid[x][y]; }
}
