package com.henrik.advergame.pathfinding;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * An implementation of a 2-dimensional KD tree to facilitate faster nearest neighbour searches and navmesh node lookups.
 */
public class KDTree {

    private class VectorCompareX implements Comparator<NavigationNode> {
        public int compare(NavigationNode a, NavigationNode b) {
            if(a.getPosition().x < b.getPosition().x)
                return -1;
            else if(a.getPosition().x == b.getPosition().x)
                return 0;
            else
                return 1;
        }

    }

    private class VectorCompareY implements Comparator<NavigationNode> {
        public int compare(NavigationNode a, NavigationNode b) {
            if(a.getPosition().y < b.getPosition().y)
                return -1;
            else if(a.getPosition().y == b.getPosition().y)
                return 0;
            else
                return 1;
        }
    }

    // The root node
    NavigationNode root;

    // Used internally for searching functions
    NavigationNode currentBest;
    double bestDistance;

    int currentHighest; // Value used when recursively searching for highest scent

    int playerScent;

    private static final int X_AXIS = 0;
    private static final int Y_AXIS = 1;

    public KDTree(ArrayList<NavigationNode> meshNodes) {

        // Initialize values
        playerScent = 0;
        currentHighest = Integer.MIN_VALUE;
        bestDistance = Double.MIN_VALUE;

        // Build the tree
        root = buildTree(0, meshNodes);
    }

    /**
     * Internal recursive tree building function.
     * @param depth The current depth in the tree.
     * @param meshNodes The list of meshnodes that still need to be added.
     * @return Returns the root of the constructed tree.
     */
    private NavigationNode buildTree(int depth, ArrayList<NavigationNode> meshNodes) {

        if(meshNodes.size() == 1) {
            return meshNodes.get(0);
        }
        else if(meshNodes.size() == 0)
            return null;

        // Determine the splitting axis
        int axis = 0;
        if(valueEven(depth))
            axis = X_AXIS;
        else
            axis = Y_AXIS;

        // Sort points and find the median
        meshNodes = sortByAxis(meshNodes, axis);
        int median = meshNodes.size()/2;

        // Ensure that there are points either side of the median (points are sorted, so we can use index 0)
        if(median  == 0) {
            median++;
        }
        if(median == meshNodes.size()-1) {
            median--;
        }

        ArrayList<NavigationNode> beforeList = new ArrayList<NavigationNode>();
        ArrayList<NavigationNode> afterList = new ArrayList<NavigationNode>();

        for(int i = 0; i < median; i++) {
            beforeList.add(meshNodes.get(i));
        }
        for(int i = median+1; i < meshNodes.size(); i++) {
            afterList.add(meshNodes.get(i));
        }

        NavigationNode node = meshNodes.get(median);

        node.setLeft(buildTree(depth + 1, beforeList));
        node.setRight(buildTree(depth + 1, afterList));

        return node;
    }

    /**
     * Renders all nodes for a debug view. Uses an internal recursive helper function.
     * @param camera The camera to render with.
     * @param renderer The renderer to render with.
     */
    public void renderAllDebug(Camera camera, ShapeRenderer renderer) {
        renderer.setProjectionMatrix(camera.combined);
        renderer.setColor(com.badlogic.gdx.graphics.Color.RED);

        int highestScent = findHighestScentValue();
        renderAllRecursive(camera, renderer, root);
    }

    /**
     * Internal recursive helper function, renders all nodes in a debug view.
     */
    private void renderAllRecursive(Camera camera, ShapeRenderer renderer, NavigationNode root) {
        if(root == null) return;
        Color scentColor = com.badlogic.gdx.graphics.Color.WHITE;

        renderer.begin(ShapeRenderer.ShapeType.Line);
        if (root.getLinks().size() > 0) {
            for (Link link : root.getLinks()) {

                com.badlogic.gdx.graphics.Color color = new com.badlogic.gdx.graphics.Color(scentColor);
                float lerp = (float)link.getNode().getScent()/(float)currentHighest;
                color = color.lerp(com.badlogic.gdx.graphics.Color.RED, lerp);
                renderer.setColor(color);

                renderer.line(link.getNode().getPosition().x, 0, link.getNode().getPosition().y,
                        root.getPosition().x, 0, root.getPosition().y);
            }
        }
        renderer.end();

        renderAllRecursive(camera, renderer, root.getLeft());
        renderAllRecursive(camera, renderer, root.getRight());
    }

    /**
     * Internal function for recursively swapping buffers on all navigation nodes.
     */
    private void swapBuffers(NavigationNode node) {
        if(node == null) return;
        node.swapBuffers();
        swapBuffers(node.getLeft());
        swapBuffers(node.getRight());

    }

    /**
     * Updates the tree, updating player scent, all nodes, and finally swapping node buffers.
     * @param playerPosition The current world position of the player.
     */
    public void update(Vector3 playerPosition) {

        // Update scent at closest node to player location
        playerScent = playerScent + 1;
        NavigationNode nearest = getNearest(new Vector2(playerPosition.x, playerPosition.z));
        nearest.setScent(playerScent);
        nearest.setBufferScent(playerScent);

        // Update all nodes
        updateNode(root);

        // Swap the buffers
        swapBuffers(root);
    }

    /**
     * Internval recursive helper function to render all node scent values.
     * @param root The node to render, all children will be updated recursively.
     */
    private void updateNode(NavigationNode root) {
        if(root == null) return;

        // Process render for this node
        ArrayList<NavigationNode> neighbours = root.getNeighbours();
        for (NavigationNode neighbour : neighbours) {
            if (neighbour.getBufferScent() > root.getBufferScent()) {
                root.setBufferScent(neighbour.getBufferScent() - 1);
            }
        }

        updateNode(root.getLeft());
        updateNode(root.getRight());
    }

    /**
     * Finds the current highest scent value in the tree. Uses a recursive helper function for pre-order traversal. Sets updates currentHighest to this value.
     */
    private int findHighestScentValue() {
        currentHighest = Integer.MIN_VALUE;
        findHighestScentRecursive(root);
        return currentHighest;
    }

    /**
     * Recursive helper function for findHighestScentValue.
     */
    private void findHighestScentRecursive(NavigationNode root) {
        if(root == null) return;
        if(currentHighest < root.getScent()) {
            currentHighest = root.getScent();
        }
        findHighestScentRecursive(root.getLeft());
        findHighestScentRecursive(root.getRight());
    }

    /**
     * Gets the navigation node at the given position. If not found, returns null.
     * @param point The point to get.
     */
    public NavigationNode get(Vector2 point) {
        // Uses nearest neighbour search, but returns null if an equal node is never found
        bestDistance = Double.MAX_VALUE;
        search(0, point, root);
        if(currentBest.getPosition().x == point.x && currentBest.getPosition().y == point.y) {
            return currentBest;
        } else {
            return null;
        }
    }

    /**
     * Gets the navigation node closest to the given position.
     */
    public NavigationNode getNearest(Vector2 position) {
        bestDistance = Double.MAX_VALUE;
        search(0, position, root);
        return currentBest;
    }

    /**
     * Internal function updates the current closest during a nearest-neighbour search.
     * @param node The node found.
     * @param point The point being searched for.
     */
    private void updateCurrentBest(NavigationNode node, Vector2 point) {
        currentBest = node;
        bestDistance = distanceSq(point, node.getPosition());
    }

    /**
     * Recursive internal helper function used to search for the nearest neighbour for the given point.
     * @param depth The depth of the node being searched.
     * @param point The point being searched for.
     * @param node The current node. (All children searched recursively)
     */
    private void search(int depth, Vector2 point, NavigationNode node) {
        if(node == null)
            return;
        if(node.getLeft() == null && node.getRight() == null) {
            if(distanceSq(point, node.getPosition()) < bestDistance)
                updateCurrentBest(node, point);
        }
        else {
            double splittingPlane = 0;
            double comparisonValue = 0;
            if(valueEven(depth)) {
                splittingPlane = node.getPosition().x;
                comparisonValue = point.x;
            }
            else {
                splittingPlane = node.getPosition().y;
                comparisonValue = point.y;
            }

            if(splittingPlane < comparisonValue) {
                search(depth + 1, point, node.getRight());
                if(distanceSq(node.getPosition(), point) < bestDistance) {
                    updateCurrentBest(node, point);
                }
                if(Math.abs(comparisonValue - splittingPlane) < bestDistance) {
                    search(depth + 1, point, node.getLeft());
                }
            }
            else {
                search(depth + 1, point, node.getLeft());
                if(distanceSq(node.getPosition(), point) < bestDistance) {
                    updateCurrentBest(node, point);
                }
                if(Math.abs(comparisonValue - splittingPlane) < bestDistance) {
                    search(depth + 1, point, node.getRight());
                }
            }
        }
    }

    /**
     * Helper function to calcualte the squared distanceSq between two points.
     */
    private double distanceSq(Vector2 a, Vector2 b) {
        return Math.pow(b.x - a.x, 2) + Math.pow(b.y - a.y, 2);
    }

    /**
     * Internal helper functions that sorts a list of points in order of the given axis (X_AXIS or Y_AXIS constants can be used for this parameter).
     */
    private ArrayList<NavigationNode> sortByAxis(ArrayList<NavigationNode> meshNodes, int axis) {
        Comparator<NavigationNode> comparator;
        if(axis == X_AXIS)
            comparator = new VectorCompareX();
        else
            comparator = new VectorCompareY();

        // Sort points as array (Android API doesn't seem to support ArrayList.sort...)
        NavigationNode[] sortedPoints = meshNodes.toArray(new NavigationNode[meshNodes.size()]);
        Arrays.sort(sortedPoints, comparator);

        // Convert back into ArrayList and return
        ArrayList<NavigationNode> list = new ArrayList<NavigationNode>();
        for(int i = 0; i < sortedPoints.length; i++) {
            list.add(sortedPoints[i]);
        }
        return list;
    }

    /**
     * Returns whether or not the given value is even.
     */
    private boolean valueEven(int value) {
        return valueEven((float) value);
    }

    /**
     * Returns whether or not the given value is even.
     */
    private boolean valueEven(float value) {
        if(value % 2.0f == 0.0f)
            return true;
        else
            return false;
    }
}
