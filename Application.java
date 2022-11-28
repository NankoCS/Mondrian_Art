import java.awt.*;
import java.util.Random;
import java.util.ArrayList;

public class Application {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        
        // For the first strategy:
        // int widthOfImage = 10000;
        // int heightOfImage = 10000;
        // int maxLeaves = 20000000;
        // double divisionProportion = 0.1;
        // int minSizeToDivide = 5;
        // double sameColorProbability = 0.3;
        // int widthOfLine = 5;
        // long seed = 1;

        // Kd_tree first_tree = new Kd_tree(widthOfImage, heightOfImage, maxLeaves, divisionProportion, minSizeToDivide, sameColorProbability, widthOfLine, seed);
        // first_tree.generateRandomTree();

        // Image curr_img = first_tree.toImage("20kmaxLeaves");




        // For the second strategy:
        
        // int widthOfImage = 100;
        // int heightOfImage = 100;
        // int nbOfPoints = 4;
        // double sameColorProbability = 0.3;
        // int widthOfLine = 1;
        // long seed = 1;
        // Random random = new Random(seed);

        // // To create randomly distributed points across the entire zone:
        // ArrayList<Point> first_array = Point.generateArrayOfRandomPoints(nbOfPoints, widthOfImage, heightOfImage, widthOfLine, random);
        // // To create randomly distributed points on a diagonal:
        // ArrayList<Point> second_array = Point.generateDiagonalOfPoints(nbOfPoints, widthOfImage, heightOfImage, widthOfLine, random);

        // Pq_tree second_tree = Pq_tree.generateBetterRandomTree(second_array, widthOfImage, heightOfImage, nbOfPoints, widthOfLine, sameColorProbability, seed);
        // second_tree.toImage("testinglast_pq");
        

        long endTime = System.currentTimeMillis();

        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
