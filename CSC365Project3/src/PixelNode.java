import java.awt.Color;
public class PixelNode {

    int x, y;
    Color intensity;
    public PixelNode(int x, int y, Color i) {
        this.x = x;
        this.y = y;
        intensity = i;
    }

    public int getRGB() {
        return intensity.getRGB();
    }

    public int getRed() {
        return intensity.getRed();
    }

    public int getGreen() {
        return intensity.getGreen();
    }

    public int getBlue() {
        return intensity.getBlue();
    }

    public Color getIntensity() {
        return intensity;
    }
    public int getX() {
        return this.x;
    }
    public int getY() {
        return this.y;
    }

    public String toCoords() {
        return "(" + this.x + "," + this.y + ")";
    }

    
     
    
}
