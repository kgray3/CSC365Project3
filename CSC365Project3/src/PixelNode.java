import java.awt.Color;
public class PixelNode {

    //Init. fields
    int x, y;
    Color intensity;
    //Init. constructor with x-y coordinates and color of pixel
    public PixelNode(int x, int y, Color i) {
        this.x = x;
        this.y = y;
        intensity = i;
    }

    //Method for retrieving RGB int value for BufferedImage
    public int getRGB() {
        return intensity.getRGB();
    }
    //Method for retrieving red-value of pixel
    public int getRed() {
        return intensity.getRed();
    }
    //Method for retrieving green-value of pixel
    public int getGreen() {
        return intensity.getGreen();
    }
    //Method for retrieving blue-value of pixel
    public int getBlue() {
        return intensity.getBlue();
    }
    //Method for retrieving x-coordinate of pixel
    public int getX() {
        return this.x;
    }
    //Method for retrieving y-coordinate of pixel
    public int getY() {
        return this.y;
    }
    //Method for converting coordinates to a String representation for disjoint set forest's HashMap
    public String toCoords() {
        return "(" + this.x + "," + this.y + ")";
    }

    
     
    
}
