import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;


public class PolygonalChainDetector {

    private final ArrayList<Point2D> points = new ArrayList<>();
    private final ArrayList<PolygonalChain> polygonalChains;

    public PolygonalChainDetector(String filePath) throws FileNotFoundException {
        ArrayList<Line2D> geometricLines = loadGeometricLinesFromInputFile(filePath);
        this.polygonalChains = createPolygonalChains(geometricLines);
    }

    public ArrayList<Point2D> getPoints() {
        return points;
    }

    public ArrayList<PolygonalChain> createPolygonalChains() {
        return this.polygonalChains;
    }

    private ArrayList<Line2D> loadGeometricLinesFromInputFile(String filePath) throws RuntimeException {
        try {
            ArrayList<Line2D> geometricLines = new ArrayList<>();

            File f = new File(filePath);
            Scanner sc = new Scanner(f);

            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.length() > 0) {
                    String[] coordinates = line.split(" ");
                    Point2D startPoint = new Point2D.Float(Float.parseFloat(coordinates[0]), Float.parseFloat(coordinates[1]));
                    Point2D endPoint = new Point2D.Float(Float.parseFloat(coordinates[2]), Float.parseFloat(coordinates[3]));
                    this.points.add(startPoint);
                    this.points.add(endPoint);
                    geometricLines.add(new Line2D.Float(startPoint, endPoint));
                }
            }
            return geometricLines;
        } catch (IOException e) {
            throw new RuntimeException("File not found. Please enter a valid absolute path to the input file.", e);
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            throw new RuntimeException("The input file does not meet the format criteria. " +
                    "Please enter four floating point numbers.", e);
        }
    }

    private ArrayList<PolygonalChain> createPolygonalChains(ArrayList<Line2D> lines) {
        final ArrayList<Line2D> endPieces = new ArrayList<>();
        final ArrayList<Line2D> middlePieces = new ArrayList<>();

        createChainLinkTypes(lines, endPieces, middlePieces);

        ArrayList<PolygonalChain> polygonalChains = new ArrayList<>();
        while (!endPieces.isEmpty()) {
            createOpenPolygonalChain(endPieces, middlePieces, polygonalChains);
        }

        while (!middlePieces.isEmpty()) {
            createClosedPolygonalChain(middlePieces, polygonalChains);
        }

        return polygonalChains;
    }

    private void createChainLinkTypes(ArrayList<Line2D> lines, ArrayList<Line2D> endPieces, ArrayList<Line2D> middlePieces) {
        for (Line2D line : lines) {
            Point2D p1 = line.getP1();
            Point2D p2 = line.getP2();

            if (isMiddlePiece(p1, p2)) {
                middlePieces.add(line);
            } else if (isEndPiece(p1, p2)) {
                endPieces.add(line);
            }
        }
    }

    private boolean isMiddlePiece(Point2D p1, Point2D p2) {
        return getPoints().stream().filter(p -> p.equals(p1)).count() == 2
                && getPoints().stream().filter(p -> p.equals(p2)).count() == 2;
    }

    private boolean isEndPiece(Point2D p1, Point2D p2) {
        return (getPoints().stream().filter(p -> p.equals(p1)).count() == 2
                && getPoints().stream().filter(p -> p.equals(p2)).count() != 2)
                || (getPoints().stream().filter(p -> p.equals(p1)).count() != 2
                && getPoints().stream().filter(p -> p.equals(p2)).count() == 2);
    }

    private void createOpenPolygonalChain(ArrayList<Line2D> endPieces, ArrayList<Line2D> middlePieces, ArrayList<PolygonalChain> polygonalChains) {
        createPolygonalChain(endPieces, middlePieces, polygonalChains);
    }

    private void createClosedPolygonalChain(ArrayList<Line2D> middlePieces, ArrayList<PolygonalChain> polygonalChains) {
        createPolygonalChain(middlePieces, middlePieces, polygonalChains);
    }

    private void createPolygonalChain(ArrayList<Line2D> lines, ArrayList<Line2D> middlePieces, ArrayList<PolygonalChain> polygonalChains) {
        ArrayList<Line2D> polygonalChain = new ArrayList<>();
        Line2D firstPiece = lines.get(0);
        Optional<Line2D> chainLink;
        Optional<Line2D> lastPiece;
        polygonalChain.add(firstPiece);
        lines.remove(0);

        do {
            int indexOfLastElement = polygonalChain.size() - 1;
            Line2D l = polygonalChain.get(indexOfLastElement);
            Point2D p1 = l.getP1();
            Point2D p2 = l.getP2();

            chainLink = getChainLink(middlePieces, p1, p2);

            if (chainLink.isPresent()) {
                addToPolygonalChain(middlePieces, polygonalChain, chainLink.get());
            } else {
                lastPiece = getChainLink(lines, p1, p2);

                lastPiece.ifPresent(line2D -> addToPolygonalChain(lines, polygonalChain, line2D));
            }
        } while (chainLink.isPresent());

        if (polygonalChain.size() > 1) {
            int length = getLength(polygonalChain);
            polygonalChains.add(new PolygonalChain(polygonalChain, length));
        }
    }

    private Optional<Line2D> getChainLink(ArrayList<Line2D> lines, Point2D p1, Point2D p2) {
        return lines.stream().filter(mp -> mp.getP1().equals(p1)
                        || mp.getP1().equals(p2)
                        || mp.getP2().equals(p1)
                        || mp.getP2().equals(p2))
                .findFirst();
    }

    private void addToPolygonalChain(ArrayList<Line2D> lines, ArrayList<Line2D> polygonalChain, Line2D chainLink) {
        polygonalChain.add(chainLink);
        lines.remove(chainLink);
    }

    private int getLength(ArrayList<Line2D> lines) {
        int length = 0;
        for (Line2D line : lines)
            length += line.getP1().distance(line.getP2());

        return length;
    }
}