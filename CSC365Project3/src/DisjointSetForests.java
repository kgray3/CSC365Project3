import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
public class DisjointSetForests {
    public Map<String, PixelNode> parentList = new HashMap<String, PixelNode>();
    public Map<String, Long> cardinalityList = new HashMap<String, Long>();
    public Map<String, Integer> internalDifferences = new HashMap<String, Integer>();
    public Map<String, Integer> rank = new HashMap<String, Integer>();
    public Map<String, Integer> colorList = new HashMap<String, Integer>();
    PixelNode[] vertexArray;

    public DisjointSetForests(PixelNode[] vertexArr) {
        vertexArray = vertexArr;
        for(int i = 0; i < vertexArr.length; i++) {
            PixelNode v = vertexArr[i];
            parentList.put(v.toCoords(), v);
            cardinalityList.put(v.toCoords(), (long)1);
            rank.put(v.toCoords(), 1);
            internalDifferences.put(v.toCoords(), 0);
            colorList.put(v.toCoords(), randomColor());
        }
    }

    public PixelNode findSet(PixelNode test) {
        if(parentList.get(test.toCoords()).toCoords().equals(test.toCoords())) {
            return test;
        } else {
            return findSet(parentList.get(test.toCoords()));
        }
    }

    public void union(PixelNode x, PixelNode y, int internalDiff) {
            PixelNode xSetRep = findSet(x);
            PixelNode ySetRep = findSet(y);
            long newCardinality = cardinalityList.get(xSetRep.toCoords()) + cardinalityList.get(ySetRep.toCoords());
            if(newCardinality > 100000) {
                System.out.println("ughhghhghgh");
            }
            parentList.replace(x.toCoords(), y);
            parentList.replace(xSetRep.toCoords(), ySetRep);
            PixelNode newSetRep = findSet(x);
            internalDifferences.replace(newSetRep.toCoords(), internalDiff);
            //rank.replace(x.toCoords(), rank.get(x.toCoords()) + 1);
            //colorList.put(newSetRep.toCoords(), randomColor());
            cardinalityList.replace(newSetRep.toCoords(), newCardinality);


    }

    public static int randomColor() {
        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        return new Color(r, g, b).getRGB();
    }

    public int getInternalDiff(PixelNode x) {
        return internalDifferences.get(x.toCoords());
    }

    public long getCardinality(PixelNode x) {
        return cardinalityList.get(x.toCoords());
    }

    public int getRank(PixelNode x) {
        return rank.get(x.toCoords());
    }
    public int getColor(PixelNode x) {
        return colorList.get(x.toCoords());
    }

    
    
    
}
