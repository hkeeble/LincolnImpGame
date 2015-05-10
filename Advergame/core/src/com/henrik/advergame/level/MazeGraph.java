package com.henrik.advergame.level;

import com.henrik.advergame.utils.Point;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

/**
 * A class used to represent a graph, used to construct mazes.
 */
public class MazeGraph {

    /**
     * A graph edge.
     */
    private class Edge {
        Node node; // Node this edge links to
        boolean blocked;

        public Edge(Node node) {
            blocked = true;
            this.node = node;
        }

        public void setBlocked(boolean blocked) {
            this.blocked = blocked;
        }

        public boolean isBlocked() {
            return blocked;
        }

        public Node getNode() {
            return node;
        }

        public void setNode(Node node) {
            this.node = node;
        }
    }

    /**
     * A node in the graph.
     */
    private class Node {
        Point position;
        ArrayList<Edge> edges;

        public Node(Point position) {
            this.position = position;
            edges = new ArrayList<Edge>();
        }

        public Node(Point position, ArrayList<Edge> edges) {
            this(position);
            this.edges = edges;
        }

        public void addEdge(Edge edge) {
            edges.add(edge);
        }

        public ArrayList<Edge> getEdges() {
            return edges;
        }

        public Point getPosition() {
            return position;
        }

        public int getX() {
            return position.x;
        }

        public int getY() {
            return position.y;
        }
    }

    // Nodes in this graph
    Node[][] nodes;
    int width, height;

    /**
     * Will create a new maze graph, as close to the given grid dimensions as possible (graphs have different structures, so not all grid sizes translate exactly)
     * @param width Preferred width of the grid.
     * @param height Preferred height of the grid.
     */
    public MazeGraph(int width, int height) {
        this.width = ((width-1)/2)+1;
        this.height = ((height-1)/2)+1;

        // Create node grid
        nodes = new Node[this.width][this.height];

        for(int x = 0; x < this.width; x++) {
            for(int y = 0; y < this.height; y++) {
                nodes[x][y] = new Node(new Point(x, y));
            }
        }

        // Add neighbour edges
        for(int x = 0; x < this.width; x++) {
            for(int y = 0; y < this.height; y++) {
                if(x != this.width-1)
                    nodes[x][y].addEdge(new Edge(nodes[x+1][y]));
                if(x != 0)
                    nodes[x][y].addEdge(new Edge(nodes[x-1][y]));
                if(y != this.height-1)
                    nodes[x][y].addEdge(new Edge(nodes[x][y+1]));
                if(y != 0)
                    nodes[x][y].addEdge(new Edge(nodes[x][y-1]));
            }
        }

        // Generate maze
        generateMaze();
    }

/* Recursive maze function, defunct for now due to small stack size on android devices
    private void carvePassageFrom(Node node) {
        visitedNodes.add(node);
        ArrayList<Edge> edges = node.getEdges();
        Collections.shuffle(edges, new Random());

        for(Edge edge : edges) {
            if(!visitedNodes.contains(edge.getNode())) {
                edge.setBlocked(false);
                carvePassageFrom(edge.getNode());
            }
        }
    }
*/

    /**
     * An individual stack item, used for iterative version of maze generator
     */
    private class StackItem {
        public StackItem(Node node) {
            this.node = node;
            ArrayList<Edge> unshuffledEdges = node.getEdges();
            Collections.shuffle(unshuffledEdges, new Random());
            this.edges = unshuffledEdges;
            this.currentEdge = 0;
        }

        public Node node;
        public ArrayList<Edge> edges;
        public int currentEdge;
    }

    // A non-recursive replacement for the maze generation algorithm for smaller devices
    private void generateMaze() {
        ArrayList<Node> visitedNodes = new ArrayList<Node>();
        Stack<StackItem> nodeStack = new Stack<StackItem>();

        // Initialize the first stack item
        nodeStack.push(new StackItem(nodes[0][0]));

        while(nodeStack.isEmpty() == false) {
            StackItem currentItem = nodeStack.pop();
            visitedNodes.add(currentItem.node);

            for(int i = currentItem.currentEdge; i < currentItem.edges.size(); i++) {
                Edge edge = currentItem.edges.get(i);
                if(!visitedNodes.contains(edge.getNode())) {
                    edge.setBlocked(false);
                    currentItem.currentEdge = i;
                    nodeStack.push(currentItem); // Re-add the current node below the new node, with its current edge (to simulate returning to this point in recursion)

                    // Add new node to the stack here...
                    nodeStack.push(new StackItem(edge.getNode()));

                    break;
                }
            }
        }
    }

    /**
     * Converts the maze graph into a cell grid structure. False cells are impassable.
     */
    public boolean[][] convertToCellGrid() {
        int gridWidth = width+(width-1);
        int gridHeight = height+(height-1);

        boolean grid[][] = new boolean[gridWidth][gridHeight];

        for(int x = 0; x < gridWidth; x++) {
            for(int y = 0; y < gridHeight; y++) {
                grid[x][y] = false;
            }
        }

        for(int x = 0; x < width; x++) {
            for(int y = 0; y  < height; y++) {
                Point nodeGridPos = new Point(nodes[x][y].getX()*2, nodes[x][y].getY()*2); // Convert node position to grid position
                grid[nodeGridPos.x][nodeGridPos.y] = true; // Open the cell where the node is located

                // Open all cells where there is an unblocked edge
                ArrayList<Edge> neighbours = nodes[x][y].getEdges();
                for(Edge edge : neighbours) {
                    if(!edge.isBlocked()) {
                        // Determine the physical direction of the edge in cartesian coordinates
                        Node linkNode = edge.getNode();
                        if(linkNode.getX() < nodes[x][y].getX()) { // Left
                            grid[nodeGridPos.x-1][nodeGridPos.y] = true;
                        }
                        else if(linkNode.getX() > nodes[x][y].getX()) { // Right
                            grid[nodeGridPos.x+1][nodeGridPos.y] = true;
                        }
                        else if(linkNode.getY() < nodes[x][y].getY()) { // Up
                            grid[nodeGridPos.x][nodeGridPos.y-1] = true;
                        }
                        else if(linkNode.getY() > nodes[x][y].getY()) { // Down
                            grid[nodeGridPos.x][nodeGridPos.y+1] = true;
                        }
                    }
                }
            }
        }

        return grid;
    }
}
