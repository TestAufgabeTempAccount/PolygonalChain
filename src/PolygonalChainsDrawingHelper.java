import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;

public class PolygonalChainsDrawingHelper extends JFrame {

    private final ArrayList<PolygonalChain> polygonalChains;
    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;

    public PolygonalChainsDrawingHelper(ArrayList<PolygonalChain> polygonalChains) {
        super("Polygonal Chains");

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        this.polygonalChains = polygonalChains;
    }

    public void paint(Graphics g) {
        super.paint(g);
        drawLines(g);
    }

    private void drawLines(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(3.0f));

        int numberOfColors = polygonalChains.size();
        ArrayList<Color> colors = getColors(numberOfColors);

        for (int i = 0; i < polygonalChains.size(); i++) {
            PolygonalChain polygonalChain = polygonalChains.get(i);
            g2d.setColor(colors.get(i));
            g2d.setFont(new Font("Courier", Font.PLAIN, 15));
            g2d.drawString(polygonalChain.toString(), 44, 80 + i * 20);
            for (Line2D line : polygonalChain.getChain()) {
                g2d.draw(line);
            }
        }
    }

    private ArrayList<Color> getColors(float numberOfColors) {
        ArrayList<Color> colors = new ArrayList<>();

        for (float i = 0; i < 1; i += 1 / numberOfColors) {
            Color hsbColor = Color.getHSBColor(i, (float) (0.9f + Math.random() * 0.1f), (float) (0.8f + Math.random() * 0.1f));
            colors.add(new Color(hsbColor.getRGB()));
        }
        return colors;
    }
}
