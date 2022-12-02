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

    public static ArrayList<Point> generateCluster(int nbOfPoints, int width, int height, int widthOfLine, Random random, double clusterProbability){
        if (nbOfPoints > 0){
            ArrayList<Point> tmp = new ArrayList<>();
            int rndX = random.nextInt((width - 0) + 1) + 0;
            int rndY = random.nextInt((height - 0) + 1) + 0;
            Point centerOfCLuster = new Point(rndX, rndY);
            // generating points close to this point
            //  Math.max(0, rndX +- 5% of a random other X value)
            //  Math.max(0, rndY +- 5% of a random other Y value)
            for (int i = 0; i < nbOfPoints; i++){
                
                int tmpX = random.nextInt((width - 0) + 1) + 0;
                int tmpY = random.nextInt((height - 0) + 1) + 0;

                double plusOrMinus = random.nextDouble();
                int newX, newY;
                // in this case this new point should be on the top left of the center
                if (plusOrMinus <= 0.25){
                    newX = (int) Math.min(width - widthOfLine, Math.max(0, rndX - clusterProbability*tmpX));
                    newY = (int) Math.min(height - widthOfLine, Math.max(0, rndY - clusterProbability*tmpY));
                }
                // in this case this new point should be on the top right of the center
                else if (plusOrMinus > 0.25 && plusOrMinus <= 0.5){
                    newX = (int) Math.min(width - widthOfLine, Math.max(0, rndX + clusterProbability*tmpX));
                    newY = (int) Math.min(height - widthOfLine, Math.max(0, rndY - clusterProbability*tmpY));
                }
                // in this case this new point should be on the bottom left of the center
                else if (plusOrMinus > 0.5 && plusOrMinus <= 0.75){
                    newX = (int) Math.min(width - widthOfLine, Math.max(0, rndX - clusterProbability*tmpX));
                    newY = (int) Math.min(height - widthOfLine, Math.max(0, rndY + clusterProbability*tmpY));
                }
                // in this case this new point should be on the bottom right of the center
                else {
                    newX = (int) Math.min(width - widthOfLine, Math.max(0, rndX + clusterProbability*tmpX));
                    newY = (int) Math.min(height - widthOfLine, Math.max(0, rndY + clusterProbability*tmpY));
                }

                tmp.add(new Point(newX, newY));
            }
            return tmp;
        }
        return null;
    }


    void printInfo(){
        System.out.println("(x=" + x + " , y=" + y + ")");
    }
}
