package com.henrik.advergame.pathfinding;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.henrik.advergame.level.*;
import com.henrik.advergame.level.shared.TileState;

import java.util.*;
import java.util.Map;

/**
 * A mesh that represents the space navigable by an entity in the level. Also maintains a scent map for the navigation mesh.
 */
public class NavigationMesh {

    private KDTree meshTree; // The KD Tree containing all nodes in the navigation mesh.
    private ArrayList<Vector2> points; // List of points stored by the nav mesh
    private boolean debug;
    private int cellSize;
    private boolean needsUpdate; // Navigation only needs to swapBuffers once every other frame (boosts performance!)

    private int playerScent; // Current player scent value

    public NavigationMesh(Level level, int cellSize) {
        debug = false;
        playerScent = 1;
        this.cellSize = cellSize;
        generateMesh(level);
    }

    public void generateMesh(Level level) {

        // Temporary map used to build the mesh node objects (sorted into a KDTree for faster lookups later)
        HashMap<Vector2, NavigationNode> meshNodes = new  HashMap<Vector2, NavigationNode>();

        points = new ArrayList<Vector2>();

        // Generate all accessible nodes
        for(int x = 0; x < level.getWidth(); x++) {
            for(int y = 0; y < level.getHeight(); y++) {
                if(level.getTileState(x,y) == TileState.FLOOR) {
                    Vector2 pos = new Vector2(x*cellSize, y*cellSize);
                    meshNodes.put(pos, new NavigationNode(pos));
                    points.add(pos);
                }
            }
        }

        // Generate links between all accessible nodes
        Iterator iter = meshNodes.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry)iter.next();
            NavigationNode n = (NavigationNode)entry.getValue();

            Vector2 pos = meshNodes.get(n.getPosition()).getPosition();

            // Horizontal/Vertical directions
            Vector2 up = new Vector2(), down = new Vector2(), left = new Vector2(), right = new Vector2();
            up.set(pos).add(0, 1 * cellSize);
            down.set(pos).add(0, -1 * cellSize);
            left.set(pos).add(-1 * cellSize, 0);
            right.set(pos).add(1 * cellSize, 0);

            if(meshNodes.containsKey(up)) {
                n.addLink(meshNodes.get(up), 1);
            }
            if(meshNodes.containsKey(down)) {
                n.addLink(meshNodes.get(down), 1);
            }
            if(meshNodes.containsKey(left)) {
                n.addLink(meshNodes.get(left), 1);
            }
            if(meshNodes.containsKey(right)) {
                n.addLink(meshNodes.get(right), 1);
            }

            // Diagonal link tests
            Vector2 leftUp = new Vector2(), rightUp = new Vector2(), leftDown = new Vector2(), rightDown = new Vector2();
            leftUp.set(pos).add(-1 * cellSize, 1 * cellSize);
            rightUp.set(pos).add(1 * cellSize, 1 * cellSize);
            leftDown.set(pos).add(-1 * cellSize, -1 * cellSize);
            rightDown.set(pos).add(1 * cellSize, -1 * cellSize);

            if(meshNodes.containsKey(leftUp) && meshNodes.containsKey(left) && meshNodes.containsKey(up)) {
                n.addLink(meshNodes.get(leftUp), 0);
            }
            if(meshNodes.containsKey(rightUp) && meshNodes.containsKey(right) && meshNodes.containsKey(up)) {
                n.addLink(meshNodes.get(rightUp), 0);
            }
            if(meshNodes.containsKey(leftDown) && meshNodes.containsKey(left) && meshNodes.containsKey(down)) {
                n.addLink(meshNodes.get(leftDown), 0);
            }
            if(meshNodes.containsKey(rightDown) && meshNodes.containsKey(right) && meshNodes.containsKey(down)) {
                n.addLink(meshNodes.get(rightDown), 0);
            }
        }

        // Create array list of nodes ready to be sorted into KD tree
        ArrayList<NavigationNode> nodeList = new ArrayList<NavigationNode>();
        iter = meshNodes.entrySet().iterator();
        while(iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            NavigationNode node = (NavigationNode) entry.getValue();
            nodeList.add(node);
        }

        // Initialize KD Tree
        meshTree = new KDTree(nodeList);

        needsUpdate = true; // Initially, needs to swapBuffers
    }

    public void update(Camera camera, ShapeRenderer renderer, Vector3 playerPosition) {
        if (debug) {
            meshTree.renderAllDebug(camera, renderer);
        }
    }

    /**
     * Clears all scent data on the nav mesh.
     */
    public void clearScentData() {
        playerScent = 1;

        for(Vector2 point : points) {
            NavigationNode node = meshTree.get(point);
            node.setScent(0);
        }
    }

    public double distanceBetween(Vector3 a, Vector3 b) {
        return Math.pow(b.x-a.x,2)+Math.pow(b.y-a.y,2)+Math.pow(b.z-a.z,2);
    }

    public void toggleDebug() {
        debug = !debug;
    }

    public boolean isDebug() { return debug; }

    public NavigationNode getNode(Vector2 position) { return meshTree.get(position); }

    public NavigationNode getNearest(Vector2 position) { return meshTree.getNearest(position); }

    public KDTree getTree() { return meshTree; }
}
