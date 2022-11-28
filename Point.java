import java.util.Random;
import java.util.ArrayList;

public class Point {
    private int x, y;
    
    Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    public static Point generateRandomPoint(int width, int height, Random random){

        int x = random.nextInt((width - 0) + 1) + 0;
        int y = random.nextInt((height - 0) + 1) + 0;

        return new Point(x, y);
    }

    public static ArrayList<Point> generateArrayOfRandomPoints(int nbOfPoints, int width, int height, int widthOfLine, Random random){
        if (nbOfPoints > 0){
            ArrayList<Point> tmp = new ArrayList<>();
            for (int i = 0; i < nbOfPoints; i++){
                tmp.add(generateRandomPoint(Math.max(0, width - widthOfLine), Math.max(0, height - widthOfLine), random));
            }
            return tmp;
        }
        return null;
    }

    public static ArrayList<Point> generateDiagonalOfPoints(int nbOfPoints, int width, int height, int widthOfLine, Random random){
        if (nbOfPoints > 0){
            ArrayList<Point> tmp = new ArrayList<>();
            for (int i = 0; i < nbOfPoints; i++){
                int randomCoord = random.nextInt((Math.min(width, height) - 0) + 1) + 0;
                tmp.add(new Point(Math.max(0,randomCoord - widthOfLine), Math.min(Math.max(randomCoord - widthOfLine, 0), width - widthOfLine)));
            }
            return tmp;
        }
        return null;
    }


    void printInfo(){
        System.out.println("(x=" + x + " , y=" + y + ")");
    }
}
