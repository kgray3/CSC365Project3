import java.util.Map;
import java.util.HashMap;
import java.awt.Color;
public class DisjointSetForests {
    //hashmap for DisjointSetForest -- stores where pixel points too in set (note: could be a SetRep, but not always)
    public Map<String, SetRep> parentList = new HashMap<String, SetRep>();
    public Map<Integer, Boolean> uniqueColor = new HashMap<Integer, Boolean>(); //hashmap to check for unique colors for sets
   
   
    /* IMPLEMENTATION BEFORE ADDING SETREP OBJECT FOR STORAGE
   // public Map<String, Long> cardinalityList = new HashMap<String, Long>();
   // public Map<String, Integer> internalDifferences = new HashMap<String, Integer>();
   // public Map<String, Integer> rank = new HashMap<String, Integer>();
   // public Map<String, Integer> colorList = new HashMap<String, Integer>(); */
    
   PixelNode[] vertexArray; //array of vertices

   /* DisjointSetForests contructor takes array of vertices as parameter and initializes
      -All vertices start off in their own sets, with random colors, length 1 cardinality, and 0 internalDifference
   */
    public DisjointSetForests(PixelNode[] vertexArr) {
        vertexArray = vertexArr;
        for(int i = 0; i < vertexArr.length; i++) {
            PixelNode v = vertexArr[i];
            Color temp = randomColor();
            
            //while loop to check that each set is assigned a unique color
             while(uniqueColor.get(temp.getRGB()) != null && uniqueColor.get(temp.getRGB())) {
                 temp = randomColor();
             }
        
            uniqueColor.put(temp.getRGB(), true);  //after unique color is found and set, it is no longer unique
            SetRep p = new SetRep(v.toCoords(), temp, (long) 1, 0); //create "SetRep" object with aforementioned fields
            parentList.put(v.toCoords(), p); //put "SetRep" object in hashmap at pixels String coordinate value for key
        }
    }

    //Recursive method to locate the set representative of the disjoint set forest
    public SetRep findSet(String coords) {
        if(parentList.get(coords).getSetRepCoords().equals(coords)) {
            return parentList.get(coords);
        } else {
            return findSet(parentList.get(coords).getSetRepCoords());
        }
    }

    //Method to unify two sets that satisfy the merge condition
    public void union(String xCoords, String yCoords, int internalDiff) {
            SetRep xSetRep = findSet(xCoords); //set representative of first disjoint set
            SetRep ySetRep = findSet(yCoords); //set representative of second disjoint set
            long newCardinality = xSetRep.getSetRepCardinality() + ySetRep.getSetRepCardinality(); //sum both sets' cardinalities
            parentList.replace(xCoords, ySetRep); //point current member of disjoint set towards second disjoint set member
            parentList.replace(xSetRep.getSetRepCoords(), ySetRep); //point the 1st set representative towards the second set's representative
            SetRep newSetRep = findSet(xCoords); //find the new set leader (should be ySetRep, but use redundancy to be sure)
            newSetRep.setInternalDiff(internalDiff); //set the new set representative's maximum internal difference (determined in Main class)
            newSetRep.setCardinality(newCardinality); //set new set representative's cardinality
            
            
            /* OLD IMPLEMENATION BEFORE ADDING SETREP OBJECT
            //internalDifferences.replace(newSetRep.toCoords(), internalDiff);
            //rank.replace(x.toCoords(), rank.get(x.toCoords()) + 1);
            //colorList.put(newSetRep.toCoords(), randomColor());
            //cardinalityList.replace(newSetRep.toCoords(), newCardinality); */


    }

    //Method to return a random color based on random red, green, blue values
    public static Color randomColor() {
        int r = (int) (Math.random()*256);
        int g = (int) (Math.random()*256);
        int b = (int) (Math.random()*256);
        return new Color(r, g, b);
    }
    
    
    
}
