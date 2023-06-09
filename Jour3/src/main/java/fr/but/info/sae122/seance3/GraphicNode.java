package fr.but.info.sae122.seance3;

import fr.but.info.sae122.seance3.model.Graph;
import fr.but.info.sae122.seance3.model.Path;

/**
 * Represents a graphical node in a graph.
 */
public class GraphicNode {

    private double x;
    private double y;
    private double radius;
    private String color;

    /**
     * Returns the X coordinate of the node.
     *
     * @return the X coordinate of the node
     */
    public double getX() {
        return x;
    }

    public GraphicNode(double x, double y, double radius, String color) {
		this.x = x;
		this.y = y;
		this.radius = radius;
		this.color = color;
	}

	/**
     * Sets the X coordinate of the node.
     *
     * @param x the new X coordinate of the node
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the Y coordinate of the node.
     *
     * @return the Y coordinate of the node
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the Y coordinate of the node.
     *
     * @param y the new Y coordinate of the node
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the radius of the node.
     *
     * @return the radius of the node
     */
    public double getRadius() {
        return radius;
    }

    /**
     * Sets the radius of the node.
     *
     * @param radius the new radius of the node
     */
    public void setRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Returns the color of the node.
     *
     * @return the color of the node
     */
    public String getColor() {
        return color;
    }

    /**
     * Sets the color of the node.
     *
     * @param color the new color of the node
     */
    public void setColor(String color) {
        this.color = color;
    }

}
