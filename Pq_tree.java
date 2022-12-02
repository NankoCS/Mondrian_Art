import java.util.ArrayList;
import java.util.Random;
import java.awt.*;

public class Pq_tree {
    Pq_node root;
    // check later how to treat different point generations
    String pointGeneration;

    int widthOfImage, heightOfImage;
    int widthOfLine;
    int nbOfPoints;
    Random random;
    long seed;
    double sameColorProbability;
    boolean approach;

    // boolean approach corresponds to how you want to choose the points to divide,
    // by time of arrival in the array (FIFO) or by choosing the median points
    // false for median point approach, true for other approach
    Pq_tree(ArrayList<Point> pointsInZone, int widthOfImage, int heightOfImage, int nbOfPoints, int widthOfLine,
            double sameColorProbability, long seed, boolean approach) {

        this.approach = approach;
        this.widthOfImage = widthOfImage;
        this.heightOfImage = heightOfImage;
        this.nbOfPoints = nbOfPoints;
        this.widthOfLine = widthOfLine;
        this.seed = seed;
        this.sameColorProbability = sameColorProbability;
        this.random = new Random(this.seed);

        this.root = new Pq_node(pointsInZone, widthOfImage, heightOfImage, 0, 0, widthOfImage, heightOfImage, approach);

        this.generateSubtrees(this.root);

    }

    static public Pq_tree generateBetterRandomTree(ArrayList<Point> pointsInZone, int widthOfImage, int heightOfImage,
            int nbOfPoints, int widthOfLine, double sameColorProbability,
            long seed, boolean approach) {
        return new Pq_tree(pointsInZone, widthOfImage, heightOfImage, nbOfPoints, widthOfLine, sameColorProbability,
                seed, approach);
    }

    // O(1)
    // same idea as first strategy
    void chooseColor(Pq_node leaf, Color parentColor) {
        if (leaf != null) {
            Color[] tabOfColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE };
            // add the seed

            double rnd = random.nextDouble();
            if (rnd <= this.sameColorProbability) {
                leaf.colorOfZone = parentColor;
            } else {
                // gets a random integer in [0, 4]
                int randomIndex = random.nextInt((4 - 0) + 1) + 0;
                leaf.colorOfZone = tabOfColors[randomIndex];
            }
        }
    }

    // O(nbOfPoints * n) as in the worst case scenario, all of the points are always
    // going in the same zone
    // this worst case can only happen when using a FIFO approach when picking the
    // central point of a zone, otherwise by picking the median point we escape this
    // worst case scenario
    // general idea:
    // a node contains the list of ALL of the points present in its zone, we then
    // want to split these points into 4 different lists, each one corresponding to
    // a specific zone
    // when we have these 4 lists, we then create the 4 child nodes of the current
    // node using the adequate parameters and then generate recursively all of the
    // subtrees needed
    void generateSubtrees(Pq_node node) {
        if (node != null) {
            if (node.pointsInZone.size() >= 0) {
                // this will sort the points in the list present in the node into 4 different
                // arrays
                fourZones arraysOfPoint = node.sortingPoints();
                if (arraysOfPoint == null) {
                    return;
                }

                ArrayList<Point> topLeftPoints = arraysOfPoint.top_left;
                ArrayList<Point> topRightPoints = arraysOfPoint.top_right;
                ArrayList<Point> bottomLeftPoints = arraysOfPoint.bottom_left;
                ArrayList<Point> bottomRightPoints = arraysOfPoint.bottom_right;

                node.addTopLeftChild(topLeftPoints, node.center.getX() - node.startX, node.center.getY() - node.startY,
                        node.startX, node.startY, node.center.getX(), node.center.getY(), this.approach);
                // changing the color of the children according to the parent node
                this.chooseColor(node.top_left, node.colorOfZone);

                node.addTopRightChild(topRightPoints, node.endX - (node.center.getX() + this.widthOfLine),
                        node.center.getY() - node.startY, node.center.getX() + this.widthOfLine, node.startY, node.endX,
                        node.center.getY(), this.approach);
                this.chooseColor(node.top_right, node.colorOfZone);

                node.addBottomLeftChild(bottomLeftPoints, node.center.getX() - node.startX,
                        node.endY - (node.center.getY() + this.widthOfLine), node.startX, node.center.getY(),
                        node.center.getX(), node.endY, this.approach);
                this.chooseColor(node.bottom_left, node.colorOfZone);

                node.addBottomRightChild(bottomRightPoints, node.endX - (node.center.getX() + this.widthOfLine),
                        node.endY - (node.center.getY() + this.widthOfLine), node.center.getX() + this.widthOfLine,
                        node.center.getY() + this.widthOfLine, node.endX, node.endY, this.approach);
                this.chooseColor(node.bottom_right, node.colorOfZone);

                this.generateSubtrees(node.top_left);
                this.generateSubtrees(node.top_right);
                this.generateSubtrees(node.bottom_left);
                this.generateSubtrees(node.bottom_right);

            }
        } else {
            // in the case where the zone contains no point
            return;
        }
    }

    // O(n) with n being the number of nodes in the tree
    // Coloring if it's a leaf (no central point in the zone), or coloring its 4
    // children if it contains a central point
    // this will recursively go through all of the leaves of the tree
    void colorNode(Pq_node node, Image image) {
        if (node != null) {
            // if it's only a leaf
            if (node.containsAPoint == false) {
                image.setRectangle(node.startX, node.endX, node.startY, node.endY, node.colorOfZone);
            }
            // if it is a division
            else {
                this.colorNode(node.top_left, image);
                this.colorNode(node.top_right, image);
                this.colorNode(node.bottom_left, image);
                this.colorNode(node.bottom_right, image);

                image.setRectangle(node.center.getX(), node.center.getX() + this.widthOfLine, node.startY, node.endY,
                        Color.GRAY);
                image.setRectangle(node.startX, node.endX, node.center.getY(), node.center.getY() + this.widthOfLine,
                        Color.GRAY);
            }
        }
    }

    // check if wan tto return an image or just void
    Image toImage(String filename) {

        Image tmp = new Image(this.widthOfImage, this.heightOfImage);
        this.colorNode(this.root, tmp);

        try {
            tmp.save(filename + ".png");
        } catch (Exception e) {
            System.out.println("error");
        }
        return tmp;
    }
}
