import java.awt.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

public class Application {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Do you want to enter parameters in an interactive way ? false if no, true if yes");
        Boolean yOrN = scanner.nextBoolean();
        
        if (yOrN == false) {
            System.out.println("Uncomment the code at the bottom and run it using the parameters you prefer");
        } else {
            System.out.println("Which strategy do you want to use ? false for classic, true for ours");

            Boolean strategy = scanner.nextBoolean();
            System.out.println("Strategy is: " + strategy);
            scanner.nextLine();
            if (strategy == false) {
                // For the first strategy:
                System.out.println("Name of the file you want to store the image in (e.g. thisFile): ");
                String filename = scanner.nextLine();

                System.out.println("Width of the image: ");
                int widthOfImage = scanner.nextInt();

                System.out.println("Height of the image: ");
                int heightOfImage = scanner.nextInt();

                System.out.println("Maximum number of leaves: ");
                int maxLeaves = scanner.nextInt();

                System.out.println("Division proportion: ");
                double divisionProportion = scanner.nextDouble();

                System.out.println("Minimum size to divide a zone: ");
                int minSizeToDivide = scanner.nextInt();

                System.out.println("Same color probability: ");
                double sameColorProbability = scanner.nextDouble();

                System.out.println("Width of the division line: ");
                int widthOfLine = scanner.nextInt();

                System.out.println("Seed number: ");
                long seed = scanner.nextLong();

                long startTime = System.currentTimeMillis();

                Kd_tree first_tree = new Kd_tree(widthOfImage, heightOfImage, maxLeaves,
                        divisionProportion, minSizeToDivide, sameColorProbability, widthOfLine,
                        seed);
                first_tree.generateRandomTree();
                Image curr_img = first_tree.toImage(filename);

                long endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime) + " milliseconds");
                System.out.println("You can now find the art you generated in the " + filename + ".png file.");

            } else if (strategy == true) {

                // For the second strategy:
                System.out.println("Name of the file you want to store the image in (e.g. thisFile): ");
                String filename = scanner.nextLine();

                System.out.println("Width of the image: ");
                int widthOfImage = scanner.nextInt();

                System.out.println("Height of the image: ");
                int heightOfImage = scanner.nextInt();

                System.out.println("Number of points you want to generate: ");
                int nbOfPoints = scanner.nextInt();

                System.out.println("Same color probability: ");
                double sameColorProbability = scanner.nextDouble();

                System.out.println("Width of the division line: ");
                int widthOfLine = scanner.nextInt();

                System.out.println("Seed number: ");
                long seed = scanner.nextLong();

                Random random = new Random(seed);

                System.out.println("Which way do you want to generate the points: ");
                System.out.println("- 0 for a random generation across the whole canvas.");
                System.out.println(
                        "- 1 for a random generation across a diagonal going from the top left to the bottom right of the canvas.");
                System.out.println("- 2 to make a cluster of points around the point generated first.");
                System.out.println(
                        "If you choose approach 2 you will have to choose how spread out the points will be from each other (Cluster Probability)");
                System.out.println(
                        "e.g. 0.1 would mean the coordinates of the points can only differ by 10% of the firstly generated point's coordinates, 1.0 would make this approach equivalent to approach 0.");
                int approach = scanner.nextInt();

                ArrayList<Point> generatedPoints = new ArrayList<>();
                if (approach == 0) {
                    // To create randomly distributed points across the entire zone:
                    generatedPoints = Point.generateArrayOfRandomPoints(nbOfPoints, widthOfImage, heightOfImage,
                            widthOfLine, random);
                } else if (approach == 1) {
                    // To create randomly distributed points on a diagonal:
                    generatedPoints = Point.generateDiagonalOfPoints(nbOfPoints, widthOfImage, heightOfImage,
                            widthOfLine, random);
                } else if (approach == 2) {
                    // To create randomly distributed points around a cluster determined at the
                    // beginning of the function;
                    System.out.println("Cluster Probability (in [0.0, 1.0]):");
                    double clusterProbability = scanner.nextDouble();
                    generatedPoints = Point.generateCluster(nbOfPoints, widthOfImage, heightOfImage, widthOfLine,
                            random, clusterProbability);
                } else {
                    System.out.println("You need to pick a valid approach to generate points");
                    scanner.close();
                    return;
                }

                System.out.println("Which way do you want to perform divisions ?");
                System.out.println(
                        "- false : get the median point from all of the points and perform the division on this one.");
                System.out.println("- true : perform divisions in order, first point arrived, first to get divided.");
                boolean divisionApproach = scanner.nextBoolean();

                long startTime = System.currentTimeMillis();
                Pq_tree second_tree = Pq_tree.generateBetterRandomTree(generatedPoints, widthOfImage, heightOfImage,
                        nbOfPoints, widthOfLine, sameColorProbability, seed, divisionApproach);

                second_tree.toImage(filename);
                long endTime = System.currentTimeMillis();
                System.out.println("That took " + (endTime - startTime) + " milliseconds");
                System.out.println("You can now find the art you generated in the " + filename + ".png file.");
            }
        }

        scanner.close();

        // For the first strategy
        // System.out.println("Basic strategy:");
        // int widthOfImage = 10000;
        // int heightOfImage = 10000;
        // int maxLeaves = 20000;
        // double divisionProportion = 0.1;
        // int minSizeToDivide = 5;
        // double sameColorProbability = 0.3;
        // int widthOfLine = 5;
        // long seed = 1;
        // String filename = "nameOfFile";

        // long startTime = System.currentTimeMillis();
        // Kd_tree first_tree = new Kd_tree(widthOfImage, heightOfImage, maxLeaves, divisionProportion, minSizeToDivide, sameColorProbability, widthOfLine, seed);
        // first_tree.generateRandomTree();
        
        // Image curr_img = first_tree.toImage(filename);

        // long endTime = System.currentTimeMillis();
        // System.out.println("That took " + (endTime - startTime) + " milliseconds");
        // System.out.println("You can now find the art you generated in the " + filename + ".png file.");


        // For the second strategy:
        // System.out.println("Our strategy:");
        // int widthOfImage = 100;
        // int heightOfImage = 100;
        // int nbOfPoints = 40;
        // double sameColorProbability = 0.3;
        // int widthOfLine = 1;
        // long seed = 1;
        // Random random = new Random(seed);
        // // false for median point, true for FIFO division
        // boolean divisionApproach = false;
        // // probability used in the third approach to generate points
        // double clusterProbability = 0.1;
        // String filename = "nameOfFile";


        // // To create randomly distributed points across the entire zone:
        // ArrayList<Point> first_array = Point.generateArrayOfRandomPoints(nbOfPoints, widthOfImage, heightOfImage, widthOfLine, random);
        // // To create randomly distributed points on a diagonal:
        // ArrayList<Point> second_array = Point.generateDiagonalOfPoints(nbOfPoints, widthOfImage, heightOfImage, widthOfLine, random);

        // ArrayList<Point> clusterPoints = Point.generateCluster(nbOfPoints, widthOfImage, heightOfImage, widthOfLine, random, clusterProbability);

        // long startTime = System.currentTimeMillis();

        // // changed the first attribute depending on which generation you want to use
        // Pq_tree second_tree = Pq_tree.generateBetterRandomTree(second_array, widthOfImage, heightOfImage, nbOfPoints, widthOfLine, sameColorProbability, seed, divisionApproach);
        // second_tree.toImage(filename);

        // long endTime = System.currentTimeMillis();
        // System.out.println("That took " + (endTime - startTime) + " milliseconds");
        // System.out.println("You can now find the art you generated in the " + filename + ".png file.");

    }
}
