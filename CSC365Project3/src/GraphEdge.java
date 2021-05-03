public class GraphEdge {
    
    int intensityDiff;
    PixelNode a; 
    PixelNode b;

    public GraphEdge(PixelNode first, PixelNode second) {
        a = first;
        b = second;
        intensityDiff = (int) Math.sqrt(Math.abs(Math.pow(first.getRed()-second.getRed(), 2) 
        + Math.pow(first.getGreen()-second.getGreen(),2) + 
        Math.pow(first.getBlue()-second.getBlue(),2)));
    }

    

    public int getIntensityDifference() {
        return intensityDiff;
    }

    public PixelNode getA() {
        return a;
    }
    public PixelNode getB() {
        return b;
    
    }
    
}
