import java.awt.*;
import java.lang.Math;

public class Kd_node {

    // boolean that will be used later on to check if a node is a division node or a
    // leaf node (meaning it should be a colored zone on the canvas)
    private boolean isALeaf;

    // false for X, true for Y
    private boolean divisionAxis;

    private int divisionCoordinate;

    private Color colorOfNode;
    private int width, height;
    private double weigth;
    // startX: where the zone starts in terms of X
    // startY: where the zone starts in terms of Y
    int startX, startY, endX, endY;

    private Kd_node left, right;


    // constructor
    Kd_node(int width, int height, int startX, int startY, int endX, int endY) {
        this.width = width;
        this.height = height;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        // will be changed when a kd_node is initialized
        this.colorOfNode = Color.WHITE;
        this.isALeaf = true;
        this.weigth = (width * height) / (Math.pow((width + height), 1.5));
    }

    static boolean getAxis(float width, float height) {
        return true;
    }

    boolean getDivisionAxis() {
        return this.divisionAxis;
        // returns false if X, true if Y
    }

    int getDivisionCoordinate() {
        return this.divisionCoordinate;
    }

    Color getColorOfNode() {
        return this.colorOfNode;
    }

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    Kd_node leftChild() {
        return this.left;
    }

    Kd_node rightChild() {
        return this.right;
    }

    boolean isALeaf() {
        return this.isALeaf;
    }

    double getWeigth() {
        return this.weigth;
    }

    void setLeafToDivision() {
        // setting a leaf to a division by changing its boolean
        this.isALeaf = false;
        // will determine its divisionAxis and division coordinate in the
        // chooseDivision() function in the kd_tree class
    }

    // functions to add children
    void addLeftChild(int width, int height, int startX, int startY, int endX, int endY) {
        // System.out.println("addLeftchild");
        // System.out.println(width + " - " + height + " " + startX + " " + startY + " "
        // + endX + " " + endY);
        this.left = new Kd_node(width, height, startX, startY, endX, endY);
    }

    void addRightChild(int width, int height, int startX, int startY, int endX, int endY) {
        // System.out.println("addRIGHTchild");
        // System.out.println(width + " - " + height + " " + startX + " " + startY + " "
        // + endX + " " + endY);
        this.right = new Kd_node(width, height, startX, startY, endX, endY);
    }

    void setDivisionAxis(boolean axis) {
        // false for X, true for Y
        this.divisionAxis = axis;
    }

    // to change
    void setDivisionCoordinate(int coordinate) {
        this.divisionCoordinate = coordinate;
    }

    void setColor(Color color) {
        this.colorOfNode = color;
    }
}
