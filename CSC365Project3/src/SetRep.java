import java.awt.Color;

//Object class to store info about disjoint sets using "representatives" 
//(disjoint set informs us the actual representative; SetRep is mainly for storage)
public class SetRep {
    String coords; //stores String coords for DisjointSetForests' HashMap key -> i.e. "(1,2)"
    Color color;
    long cardinality;
    int maxInternalDiff;

    public SetRep(String coords, Color color, long cardinality, int maxInternalDiff ) {
        this.coords = coords;
        this.color = color;
        this.cardinality = cardinality;
        this.maxInternalDiff = maxInternalDiff;
    }

    //defines a set's color
    public void setColor(Color c) {
        this.color = c;
    }

    //defines a set's cardinality
    public void setCardinality(long c) {
        this.cardinality = c;
    }

    //defines a set's internal difference
    public void setInternalDiff(int i) {
        this.cardinality = i;
    }

    //defines a set's coordinates
    public String getSetRepCoords() {
        return this.coords;
    }

    //defines a set's color
    public Color getSetRepColor() {
        return this.color;
    }

    //defines a set's cardinality
    public long getSetRepCardinality() {
        return this.cardinality;
    }

    //defines a set's maximum internal difference
    public int getSetRepInternalDiff() {
        return this.maxInternalDiff;
    }
    
}
