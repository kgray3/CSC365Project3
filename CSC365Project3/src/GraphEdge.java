public class GraphEdge {
    
    //Init. fields
    int intensityDiff;
    PixelNode a; 
    PixelNode b;

    //Constructor for graph edge -- args = two pixels (vertices) to be connected by an edge
    public GraphEdge(PixelNode first, PixelNode second) {
        a = first;
        b = second;
        intensityDiff = (int) Math.sqrt(Math.abs(Math.pow(first.getRed()-second.getRed(), 2) 
        + Math.pow(first.getGreen()-second.getGreen(),2) + 
        Math.pow(first.getBlue()-second.getBlue(),2))); //edge weight = Euclidean distance between R, G, and B, values
    }

    
    //returns Euclidean distance
    public int getIntensityDifference() {
        return intensityDiff;
    }
    //returns first vertex of edge
    public PixelNode getA() {
        return a;
    }
    //returns second vertex of edge
    public PixelNode getB() {
        return b;
    
    }
    
}
