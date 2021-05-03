import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class Graph {
    private Map<PixelNode, List<PixelNode>> adjacencyList = new HashMap<PixelNode, List<PixelNode>>();
    
    public void addVertex(PixelNode v) {
        List<PixelNode> temp = new ArrayList<PixelNode>();
        temp.add(v);
        adjacencyList.put(v, temp);
    }

    public void addEdge(PixelNode a, PixelNode b) {
        if(adjacencyList.get(a) ==  null) {
            List<PixelNode> temp2 = new ArrayList<PixelNode>();
            temp2.add(a);
        } else {
            adjacencyList.get(a).add(b);
        }
        
        if(adjacencyList.get(b) == null) {
            List<PixelNode> temp3 = new ArrayList<PixelNode>();
            temp3.add(b);
        } else {
            adjacencyList.get(b).add(a);
        }
    }
}
