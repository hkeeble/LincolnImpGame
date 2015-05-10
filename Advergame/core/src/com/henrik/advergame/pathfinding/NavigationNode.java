package com.henrik.advergame.pathfinding;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class NavigationNode {

    private Vector2 position;
    private NavigationNode left, right;

    private ArrayList<Link> links;
    private int scent;
    private int backBufferScent;

    public NavigationNode() {
        this.left = null;
        this.right = null;
    }

    public NavigationNode(Vector2 position)  {
        this();
        links = new ArrayList<Link>();
        scent = 0;
        backBufferScent = 0;
        this.position = position;
    }

    public void swapBuffers() { scent = backBufferScent; }

    public Vector2 getPosition() { return position; }

    public int getScent() { return scent; }
    public int getBufferScent() { return backBufferScent; }
    public void setScent(int scent) { this.scent = scent; }
    public void setBufferScent(int scent) { this.backBufferScent = scent; }

    public NavigationNode getLeft() { return left; }
    public NavigationNode getRight() { return right; }
    public void setLeft(NavigationNode node) { this.left = node; }
    public void setRight(NavigationNode node) { this.right = node; }

    public void addLink(NavigationNode node, int cost) { links.add(new Link(node, cost)); }

    public ArrayList<Link> getLinks() { return links; }

    public ArrayList<NavigationNode> getNeighbours() {
        ArrayList<NavigationNode> neighbours = new ArrayList<NavigationNode>();
        for(Link link : links) {
            neighbours.add(link.getNode());
        }
        return  neighbours;
    }

    @Override
    public boolean equals(Object other) {
        NavigationNode otherNode = (NavigationNode)other;
        return otherNode.getPosition().equals(position);
    }
}