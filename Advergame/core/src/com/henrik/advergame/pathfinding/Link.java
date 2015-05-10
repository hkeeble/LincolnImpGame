package com.henrik.advergame.pathfinding;

/**
 * Represents a link to a node and the cost of the mode.
 */
public class Link {
    private NavigationNode node;
    private int cost;

    public Link(NavigationNode node, int cost) {
        this.node = node;
        this.cost = cost;
    }

    public NavigationNode getNode() {
        return node;
    }

    public int getCost() {
        return cost;
    }
}