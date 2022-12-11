import java.awt.geom.Line2D;
import java.util.ArrayList;


public class PolygonalChain {

    private static int index = 1;
    private final String name;
    private final ArrayList<Line2D> chain;
    private final int length;

    public PolygonalChain(ArrayList<Line2D> chain, int length) {
        this.chain = chain;
        this.name = String.format("polygonal chain 00%d", index);
        this.length = length;

        index++;
    }

    public ArrayList<Line2D> getChain() {
        return chain;
    }

    public int getLength() {
        return length;
    }

    @Override
    public String toString() {
        return name + ", length=" + length;
    }
}