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
        // divide by 2.0 as for us, a division proportion of 0.1 means that we won't touch 10% of the image, aka 5% on each side 
        this.divisionProportion = divisionProportion/2.0;
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

    void insertWeightInAVL(Kd_node node) {
        this.weights.root = this.weights.insert(this.weights.root, node.getWeigth(), node);
    }

    // tmp for now, delete node parameter and at the beginning of the function if
    // the conditions are met for the nb of leaves, call the chooseLeaf function
    boolean makeDivision(Kd_node node) {
        if (node != null){
            if (node.isALeaf() == true) {

                // calculating the division axis and division coordinate
                boolean div_ok = this.chooseDivision(node);
                if (!div_ok) {
                    System.out.println("div is not ok here");
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
                    node.addLeftChild(node.getWidth(), node.getDivisionCoordinate() - node.startY, node.startX, node.startY,
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
                // checking if the left and right child are dividable in advance before inserting them in the AVL (which only contains the weights of zones that are dividable)
                if (node.leftChild().getHeight() > this.minSizeToDivide && node.leftChild().getWidth() > this.minSizeToDivide){
                    insertWeightInAVL(node.leftChild());
                }
                if (node.rightChild().getHeight() > this.minSizeToDivide && node.rightChild().getWidth() > this.minSizeToDivide){
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

    // tmp for now too
    Image makeImage(String filename) {
        Image tmp = new Image(this.widthOfImage, this.heightOfImage);
        this.colorNode(this.root, tmp);

        try {
            tmp.save(filename + ".png");
        } catch (Exception e) {
            System.out.println("error");
        }
        return tmp;
    }

    // function that returns the leaf that we will later divide
    Kd_node chooseLeaf() {
        AVL_Node avl_leaf = this.weights.getMaxWeight(this.weights.root);

        if (avl_leaf != null) {
            // checking if at least one dimension is dividable
            // add nbLeaves <= maxLeaves condition
            // dont need this condition as checked this to add in avl
            if (avl_leaf.kd_node.getHeight() >= this.minSizeToDivide
                    && avl_leaf.kd_node.getWidth() >= this.minSizeToDivide) {
                // then in this case the leaf is valid and we can divide it
                this.weights.root = this.weights.remove(this.weights.root, avl_leaf.weight);
                return avl_leaf.kd_node;
                // remove from tree here not in the getmax in avl
            }
        }
        return null;
    }

    // function that will change the divisionAxis and divisionCoordinate attributes
    // of the given leaf
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
            double first_bound,second_bound;
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
                // means we can't divide here

                System.out.println("\n Problem with bounds \n");
                // return something and treat it when calling functions, that way we know when
                // theres a problem
                // bool error = makeDivision(chooseLeaf(kd_tree))
                // while error == true and nbLeaves not maxed out
                // error = makeDiv
                return false;
            }
            // getting the random coordinate where we will perform the division
            // System.out.print("Lower Bound: ");
            // System.out.print(lower_bound);
            // System.out.print(" - Upper Bound: ");
            // System.out.print(upper_bound);
            // getting a random int in [lower_bound, upper_bound]
            int randomCoordinate = random.nextInt((upper_bound - lower_bound) + 1) + lower_bound;
            // System.out.print(" - Chosen coordinate: ");
            // System.out.println(randomCoordinate);
            // set the divisionCoordinate to the randomly obtained one
            leaf.setDivisionCoordinate(randomCoordinate);
            return true;
        }
        // means the node is already a division
        return false;
    }

    // done but only with normal distrib for now
    void chooseColor(Kd_node leaf, Color parentColor) {

        Color[] tabOfColors = { Color.RED, Color.BLUE, Color.YELLOW, Color.BLACK, Color.WHITE };
        // add the seed
        
        double rnd = random.nextDouble();
        if (rnd <= this.sameColorProbability){
            leaf.setColor(parentColor);
        }
        else {
            // gets a random integer in [0, 4]
            int randomIndex = random.nextInt((4 - 0) + 1) + 0;
            leaf.setColor(tabOfColors[randomIndex]);
        }
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
        // System.out.println("Parcours sym apres premiere division");
        // this.weights.parcours_sym(this.weights.root);
        // System.out.println();
        while (this.nbOfLeaves < this.maxLeaves && div_ok){
            i+=1;
            chosenleaf = chooseLeaf();
            div_ok = makeDivision(chosenleaf);
            // System.out.print("Parcours sym apres ");
            // System.out.print(Integer.toString(i));
            // System.out.println("-eme division");
            // this.weights.parcours_sym(this.weights.root);
            // System.out.print("div_ok: ");
            // System.out.print(div_ok);
            // System.out.print(" - number of divisions: ");
            // System.out.println(i);
            // System.out.println();
        }
        // tmp for now
        System.out.println(i);
        return this;
    }

    Kd_tree generateBetterRandomTree() {
        // function that returns a random tree when using our own strategy

        // in this case, strategy = true

        // tmp for now
        return null;
    }


    Image toImage(String filename) {
        // method that, given the name of a file in the parameters, creates the canvas
        // from this Kd_tree
        // idea:
        // use a function colorNode() that will:
        // if the given node is a division, then draw the line wrt its width etc, then
        // call colorNode() on each of its child and passing as parameters the new
        // updated coordinates of this child
        // if the given node is a leaf, then use the setRectangle() function to color
        // the zone associated with the coordinates given in parameter
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
