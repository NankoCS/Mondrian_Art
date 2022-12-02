import java.util.ArrayList;
import java.awt.*;
import java.util.Random;

public class Pq_node {
    // coordinates of the center of this node/zone
    Point center;

    boolean containsAPoint;

    Color colorOfZone;

    // list of coordinates corresponding to all of the points in this zone
    // all of the points included in this zone will be in this list
    ArrayList<Point> pointsInZone;
    // the 4 different zones of this current large zone
    Pq_node top_left, top_right, bottom_left, bottom_right;

    int startX, startY, endX, endY;
    int widthOfZone, heightOfZone;

    Pq_node(ArrayList<Point> pointsInZone, int widthOfZone, int heightOfZone, int startX, int startY, int endX,
            int endY, boolean approach) {
        this.widthOfZone = widthOfZone;
        this.heightOfZone = heightOfZone;
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.pointsInZone = pointsInZone;
        if (this.pointsInZone != null) {
            if (this.pointsInZone.size() > 0) {
                this.containsAPoint = true;
                // this will set the center to the point that is the closest to the center of
                // the current zone
                if (approach == false){
                    this.determineCenter();
                }
                else {
                    // getting the first point in the array
                    this.center = this.pointsInZone.get(0);
                    this.pointsInZone.remove(0);
                }

            }
        } else {
            this.containsAPoint = false;
        }

        // random color for now
        Color[] tabOfColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE };
        // gets a random integer in [0, 4]
        Random random = new Random();
        int randomIndex = random.nextInt((4 - 0) + 1) + 0;
        this.colorOfZone = tabOfColors[randomIndex];

        // for now, later in constructor call function that sorts the array of points
        // into 4 zones and creates the 4 children zones recursively
        this.top_left = null;
        this.top_right = null;
        this.bottom_left = null;
        this.bottom_right = null;


    }

    // determining the center of the current zone
    void determineCenter() {
        if (pointsInZone.size() > 0) {
            int centerOnXAxis = (int) (this.endX + this.startX) / 2;

            int centerOnYAxis = (int) (this.endY + this.startY) / 2;

            // System.out.println("Fictional center: (" + centerOnXAxis + ", " + centerOnYAxis + ")");
            int bestIndex = 0;
            double inf = Double.POSITIVE_INFINITY;
            double lowestDistance = inf;
            // to keep the coordinates of the point with the smallest distance value to the
            // center to check for potential duplicates
            int x_tmp = 0;
            int y_tmp = 0;
            for (int i = 0; i < this.pointsInZone.size(); i++) {
                // distance between two points a and b = sqrt((xb-xa)^2 + (yb-ya)^2)
                double xdist = Math.pow(pointsInZone.get(i).getX() - centerOnXAxis, 2);
                double ydist = Math.pow(pointsInZone.get(i).getY() - centerOnYAxis, 2);
                double tmp = Math.sqrt(xdist + ydist);
                if (tmp < lowestDistance) {
                    lowestDistance = tmp;
                    bestIndex = i;
                    x_tmp = pointsInZone.get(bestIndex).getX();
                    y_tmp = pointsInZone.get(bestIndex).getY();
                } else if (tmp == lowestDistance) {
                    // meaning 2 points are at the same place
                    if (pointsInZone.get(i).getX() == x_tmp && pointsInZone.get(i).getY() == y_tmp) {
                        pointsInZone.remove(i);
                    }
                }
            }
            this.center = pointsInZone.get(bestIndex);
            // as when we set the center, we do not want it to be in the list of points that
            // we will later sort into 4 zones
            pointsInZone.remove(bestIndex);
        }
    }

    // sorting pointsInZone into 4 arrays of points, each one corresponding to one
    // zone with:
    // array[0]:top left
    // array[1]:top right
    // array[2]:bottom left
    // array[3]:bottom right
    fourZones sortingPoints() {
        if (this.containsAPoint) {
            ArrayList<Point> top_left = new ArrayList<>();
            ArrayList<Point> top_right = new ArrayList<>();
            ArrayList<Point> bottom_left = new ArrayList<>();
            ArrayList<Point> bottom_right = new ArrayList<>();

            // for loop that will go throught every point present in the current zone and
            // will sort them into four arrays of points
            // each of them corresponding to a specific zone
            for (int i = 0; i < this.pointsInZone.size(); i++) {
                int x = center.getX();
                int y = center.getY();
                int currX = this.pointsInZone.get(i).getX();
                int currY = this.pointsInZone.get(i).getY();
                if (currX < x && currY < y) {
                    // add this current point to the top_left zone array
                    top_left.add(new Point(currX, currY));
                } else if (currX >= x && currY < y) {
                    // add this current point to the top_right zone array
                    top_right.add(new Point(currX, currY));
                } else if (currX >= x && currY >= y) {
                    // add this current point to the bottom_right zone array
                    bottom_right.add(new Point(currX, currY));
                } else {
                    // add this current point to the bottom_left zone array
                    bottom_left.add(new Point(currX, currY));
                }
            }

            fourZones tmp = new fourZones(top_left, top_right, bottom_left, bottom_right);
            return tmp;
        }
        return null;
    }

    void addTopLeftChild(ArrayList<Point> pointsInZone, int width, int height, int startX, int startY, int endX,
            int endY, boolean approach) {
        this.top_left = new Pq_node(pointsInZone, width, height, startX, startY, endX, endY, approach);
    }

    void addTopRightChild(ArrayList<Point> pointsInZone, int width, int height, int startX, int startY, int endX,
            int endY, boolean approach) {
        this.top_right = new Pq_node(pointsInZone, width, height, startX, startY, endX, endY, approach);
    }

    void addBottomLeftChild(ArrayList<Point> pointsInZone, int width, int height, int startX, int startY, int endX,
            int endY, boolean approach) {
        this.bottom_left = new Pq_node(pointsInZone, width, height, startX, startY, endX, endY, approach);
    }

    void addBottomRightChild(ArrayList<Point> pointsInZone, int width, int height, int startX, int startY, int endX,
            int endY, boolean approach) {
        this.bottom_right = new Pq_node(pointsInZone, width, height, startX, startY, endX, endY, approach);
    }
}
