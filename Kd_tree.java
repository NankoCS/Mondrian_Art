import java.awt.Color;
import java.util.Random;

public class Kd_tree {
    Kd_node root;

    // AVL in which we store weights of the leaves

    // perso: change AVL structure to add pointers to the leaf etc...
    AVL weights;

    // largeur, hauteur
    int widthOfImage, heightOfImage;
    // nbFeuilles
    int maxLeaves;
    // minDimensionCoupe
    int minSizeToDivide;
    // proportionCoupe
    double divisionProportion;
    // memeCouleurProb
    double sameColorProbability;
    // largeurLigne
    int widthOfLine;
    // seed
    long seed;
    // number of leaves
    int nbOfLeaves;
    Random random;
    // 0 for given strategy, 1 for own strategy
    boolean strategy;

    // start of functions

    // constructor
    Kd_tree(int widthOfImage, int heightOfImage, int maxLeaves, double divisionProportion, int minSizeToDivide,
            double sameColorProbability, int widthOfLine, long seed) {
        this.widthOfImage = widthOfImage;
        this.heightOfImage = heightOfImage;
        this.maxLeaves = maxLeaves;
        this.minSizeToDivide = minSizeToDivide;
        // divide by 2.0 as for us, a division proportion of 0.1 means that we won't
        // touch 10% of the image, aka 5% on each side
        this.divisionProportion = divisionProportion / 2.0;
        this.sameColorProbability = sameColorProbability;
        this.widthOfLine = widthOfLine;
        this.seed = seed;
        this.root = new Kd_node(widthOfImage, heightOfImage, 0, 0, widthOfImage, heightOfImage);
        this.random = new Random(this.seed);

        Color[] tabOfColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE };
        // gets a random integer in [0, 4]
        int randomIndex = random.nextInt((4 - 0) + 1) + 0;
        // getting a random color for the first leaf
        this.root.setColor(tabOfColors[randomIndex]);

        this.nbOfLeaves = 1;
        this.weights = new AVL();
        insertWeightInAVL(this.root);
    }

    void insertWeightInAVL(Kd_node node) {
        this.weights.root = this.weights.insert(this.weights.root, node.getWeigth(), node);
    }

    // O(1) because only calling constant time operations (random functions are in
    // O(1))
    void chooseColor(Kd_node leaf, Color parentColor) {

        Color[] tabOfColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE };
        // add the seed

        double rnd = random.nextDouble();
        if (rnd <= this.sameColorProbability) {
            leaf.setColor(parentColor);
        } else {
            // gets a random integer in [0, 4]
            int randomIndex = random.nextInt((4 - 0) + 1) + 0;
            leaf.setColor(tabOfColors[randomIndex]);
        }
    }

    // function that returns the leaf that we will later divide
    // O(height(this)) = O(log n) (with n the number of nodes) because getMaxWeight
    // and remove are in O(log n)
    Kd_node chooseLeaf() {
        AVL_Node avl_leaf = this.weights.getMaxWeight(this.weights.root);

        if (avl_leaf != null) {
            // no need to check if the two dimensions are valid as we check these conditions
            // before adding a weight (and its linked kd_node) into the AVL
            this.weights.root = this.weights.remove(this.weights.root, avl_leaf.weight);
            return avl_leaf.kd_node;
        }
        return null;
    }

    // function that will change the divisionAxis and divisionCoordinate attributes
    // of the given leaf
    // O(1) because only doing constant time operations
    boolean chooseDivision(Kd_node leaf) {
        if (leaf != null) {
            // at this point, we know that the leaf is dividable (each dimension >=
            // minSizeToDivide)
            // as we perform chooseDivision on a leaf obtained from chooseLeaf and we verify
            // these conditions there

            // Determining wrt (with respect to) which axis the division will be done

            double width_prob = (double) leaf.getWidth() / (double) (leaf.getWidth() + leaf.getHeight());

            // gets a random double between 0.0 and 1.0
            double rnd_double = random.nextDouble();

            int s;
            double first_bound, second_bound;
            if (rnd_double <= width_prob) {
                // then do the division wrt the X axis
                // this will set the divisionAxis of the given leaf to the X axis
                leaf.setDivisionAxis(false);
                // as the division will be done wrt (with respect to) the X axis, we only care
                // about the width
                s = leaf.getWidth();
                first_bound = s * this.divisionProportion + leaf.startX;
                second_bound = s * (1 - this.divisionProportion) + leaf.startX;
                // System.out.println("Dividing on the X axis");
            } else {
                // do the division wrt the Y axis
                leaf.setDivisionAxis(true);
                // as the division is done by the Y axis in this case, we only care about the
                // height of the leaf
                s = leaf.getHeight();
                first_bound = s * this.divisionProportion + leaf.startY;
                second_bound = s * (1 - this.divisionProportion) + leaf.startY;
                // System.out.println("Dividing on the Y axis");

            }

            // Determining the coodinate where the division will be done

            // calculating the lower and upper bounds of an interval in which we'll randomly
            // pick an integer from, which will correspond to the divisionCoordinate
            // eg. width = 10 and divisionProportion = 0.9 --> upper_bound = ceil(10*0.9) =
            // 9 and lower_bound = floor(10*0.1) = 1
            // System.out.print("s=");
            // System.out.println(s);
            // will use floor and ceil later as we don't know which one is the biggest yet,
            // and using floor() on the lower_bound will make things false

            int upper_bound = (int) Math.floor(Math.max(first_bound, second_bound) - this.widthOfLine);
            int lower_bound = (int) Math.ceil(Math.min(second_bound, first_bound));
            // System.out.println("[" + lower_bound + ", " + upper_bound + "]");
            if (upper_bound - lower_bound <= 0) {
                System.out.println("Couldn't go further because of bounds.\n");
                return false;
            }
            // getting the random coordinate where we will perform the division
            // getting a random int in [lower_bound, upper_bound]
            int randomCoordinate = random.nextInt((upper_bound - lower_bound) + 1) + lower_bound;

            // set the divisionCoordinate to the randomly obtained one
            leaf.setDivisionCoordinate(randomCoordinate);
            return true;
        }
        // means the node is already a division
        return false;
    }

    // tmp for now, delete node parameter and at the beginning of the function if
    // the conditions are met for the nb of leaves, call the chooseLeaf function
    boolean makeDivision(Kd_node node) {
        if (node != null) {
            if (node.isALeaf() == true) {

                // calculating the division axis and division coordinate
                boolean div_ok = this.chooseDivision(node);
                if (!div_ok) {
                    return false;
                }
                // then making this leaf a division
                node.setLeafToDivision();
                // swap the 2

                // verify which division axis with if conditions it is but for now only Y axis
                // division
                // if divisionAxis == true it means the division is done on the Y axis
                if (node.getDivisionAxis() == true) {
                    // System.out.println("IN THERE");
                    node.addLeftChild(node.getWidth(), node.getDivisionCoordinate() - node.startY, node.startX,
                            node.startY,
                            node.endX, node.getDivisionCoordinate());
                    node.addRightChild(node.getWidth(), node.endY - (node.getDivisionCoordinate() + this.widthOfLine),
                            node.startX, node.getDivisionCoordinate() + this.widthOfLine, node.endX, node.endY);
                }
                // in the case of a division on the X axis
                else {
                    // System.out.println("OR WHAT");
                    node.addLeftChild(node.getDivisionCoordinate() - node.startX, node.getHeight(), node.startX,
                            node.startY, node.getDivisionCoordinate(), node.endY);
                    node.addRightChild(node.endX - (node.getDivisionCoordinate() + this.widthOfLine),
                            node.getHeight(), node.getDivisionCoordinate() + this.widthOfLine, node.startY, node.endX,
                            node.endY);
                }
                // fix with the new chooseColor with the parent
                this.chooseColor(node.leftChild(), node.getColorOfNode());
                this.chooseColor(node.rightChild(), node.getColorOfNode());
                // inserting the weight of the recently created children in the weights' AVL
                // checking if the left and right child are dividable in advance before
                // inserting them in the AVL (which only contains the weights of zones that are
                // dividable)
                if (node.leftChild().getHeight() > this.minSizeToDivide
                        && node.leftChild().getWidth() > this.minSizeToDivide) {
                    insertWeightInAVL(node.leftChild());
                }
                if (node.rightChild().getHeight() > this.minSizeToDivide
                        && node.rightChild().getWidth() > this.minSizeToDivide) {
                    insertWeightInAVL(node.rightChild());
                }

                // after performing a division, we got from n leaves to n+1 leaves
                this.nbOfLeaves += 1;
                return true;
            }
            // System.out.println("In this false");
        }
        return false;
    }

    Kd_tree generateRandomTree() {
        // given a max number of leaves (nbFeuilles). Each step we pick a leaf (wrt to
        // the weights) and divide it by 2 (if it won't cross the minSizeToDivide
        // threshold)
        // assign its color wrt to parent's color with a probability and if not this
        // case, uniform distribution between the 5 colors
        int i = 1;
        // this.weights.parcours_sym(this.weights.root);
        Kd_node chosenleaf = chooseLeaf();
        boolean div_ok = makeDivision(chosenleaf);

        while (this.nbOfLeaves < this.maxLeaves && div_ok) {
            i += 1;
            chosenleaf = chooseLeaf();
            div_ok = makeDivision(chosenleaf);
        }
        // tmp for now
        System.out.println(i + 1 + " leaves were created when using these parameters.");
        return this;
    }

    // generateBetterRandomTree function is in our Pq_tree.java file

    // color function, maybe changes later on
    void colorNode(Kd_node node, Image image) {
        if (node != null) {
            if (node.isALeaf()) {
                image.setRectangle(node.startX, node.endX, node.startY, node.endY, node.getColorOfNode());
            }
            // if it is a division
            else {
                // for Y axis division
                if (node.getDivisionAxis() == true) {
                    image.setRectangle(node.startX, node.endX, node.getDivisionCoordinate(),
                            node.getDivisionCoordinate() + this.widthOfLine, Color.GRAY);
                }
                // for X axis division
                else {
                    image.setRectangle(node.getDivisionCoordinate(), node.getDivisionCoordinate() + this.widthOfLine,
                            node.startY, node.endY, Color.GRAY);
                }

                this.colorNode(node.leftChild(), image);
                this.colorNode(node.rightChild(), image);
            }
        }
    }

    Image toImage(String filename) {
        // method that, given the name of a file in the parameters, creates the canvas
        // from the given Kd_tree
        // idea:
        // use a function colorNode() that will:
        // if the given node is a division, then draw the line wrt its width etc,
        // then call colorNode() which will recursively
        // color the lines, then the zones/leaves
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
