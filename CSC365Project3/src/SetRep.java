import java.awt.Color;
public class SetRep {
    String coords;
    Color color;
    long cardinality;
    int maxInternalDiff;

    public SetRep(String coords, Color color, long cardinality, int maxInternalDiff ) {
        this.coords = coords;
        this.color = color;
        this.cardinality = cardinality;
        this.maxInternalDiff = maxInternalDiff;
    }

    public void setColor(Color c) {
        this.color = c;
    }

    public void setCardinality(long c) {
        this.cardinality = c;
    }

    public void setInternalDiff(int i) {
        this.cardinality = i;
    }

    public String getSetRepCoords() {
        return this.coords;
    }

    public Color getSetRepColor() {
        return this.color;
    }

    public long getSetRepCardinality() {
        return this.cardinality;
    }

    public int getSetRepInternalDiff() {
        return this.maxInternalDiff;
    }
    
}
