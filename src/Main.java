import javax.swing.*;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        if(args.length == 0)
            throw new RuntimeException("Please enter a valid absolute path to the input file as argument.");

        String filePath = args[0];

        PolygonalChainDetector polygonalChainDetector;
        try {
            polygonalChainDetector = new PolygonalChainDetector(filePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File not found. Please enter a valid absolute path to the input file.", e);
        }
        ArrayList<PolygonalChain> polygonalChains = polygonalChainDetector.createPolygonalChains();

        System.out.println("Polygonal chains sorted by length:");
        List<PolygonalChain> sortedPolygonChains = polygonalChains
                .stream()
                .sorted(Comparator.comparing(PolygonalChain::getLength).reversed())
                .collect(Collectors.toList());

        sortedPolygonChains.forEach(System.out::println);

        SwingUtilities.invokeLater(() ->
                new PolygonalChainsDrawingHelper((ArrayList<PolygonalChain>) sortedPolygonChains)
                        .setVisible(true));
    }
}