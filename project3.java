//Developers:
//Hesham Elshafey
//Ahmed Nassar

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.swing.JFrame;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;

class Edge {

    Road road;
    Edge next;
}

class Intersection {

    String IntersectionID;
    double distance;
    double longitude;
    double latitude;
    boolean known;
    Intersection path;
}

class Node {

    Intersection intersection;
    Node next;
    Edge edge;
}

class Road {

    String roadID;
    String intersect1;
    String intersect2;
    double distance;

    public Road(String road, String int1, String int2, double dist) {
        roadID = road;
        intersect1 = int1;
        intersect2 = int2;
        distance = dist;
    }

}

@SuppressWarnings("serial")
class MapGUI extends JPanel {

    public static ArrayList<Road> roads;
    public static HashMap<String, Intersection> intersectionMap;
    public static boolean thickLines = false;
    public static double minLat, minLong, maxLat, maxLong;
    public static double xScale, yScale;

    public MapGUI(ArrayList<Road> roads, HashMap<String, Intersection> intersectMap, double minimumLat,
            double maximumLat, double minimumLong, double maximumLong) {
        MapGUI.roads = roads;
        MapGUI.intersectionMap = intersectMap;
        minLat = minimumLat;
        maxLat = maximumLat;
        minLong = minimumLong;
        maxLong = maximumLong;
        setPreferredSize(new Dimension(800, 800));
    }

    public void paintComponent(Graphics page) {

        Graphics2D page2 = (Graphics2D) page;
        super.paintComponent(page2);
        page2.setColor(Color.BLACK);
        xScale = this.getWidth() / (maxLong - minLong);
        yScale = this.getHeight() / (maxLat - minLat);
        Intersection int1, int2;
        double x1, y1, x2, y2;
        for (Road r : roads) {

            Rescale();
            int1 = intersectionMap.get(r.intersect1);
            int2 = intersectionMap.get(r.intersect2);
            x1 = int1.longitude;
            y1 = int1.latitude;
            x2 = int2.longitude;
            y2 = int2.latitude;
            page2.draw(new Line2D.Double((x1 - minLong) * xScale, getHeight() - ((y1 - minLat) * yScale),
                    (x2 - minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
        }

        if (Map.dijkstraPath != null) {
            page2.setColor(Color.GREEN);
            for (int i = 0; i < Map.dijkstraPath.length - 1; i++) {
                x1 = Map.dijkstraPath[i].longitude;
                y1 = Map.dijkstraPath[i].latitude;
                x2 = Map.dijkstraPath[i + 1].longitude;
                y2 = Map.dijkstraPath[i + 1].latitude;
                page2.draw(new Line2D.Double((x1 - minLong) * xScale, getHeight() - ((y1 - minLat) * yScale),
                        (x2 - minLong) * xScale, getHeight() - ((y2 - minLat) * yScale)));
            }
        }
    }

    public void Rescale() {
        xScale = this.getWidth() / (maxLong - minLong);
        yScale = this.getHeight() / (maxLat - minLat);
    }
}

public class project3 {

    public static void main(String[] args) throws FileNotFoundException {

        long startTime = System.currentTimeMillis();
        File mapData = new File(args[0]);
        Scanner obj = new Scanner(mapData);
        int intersects_number = 0;
        while (obj.nextLine().startsWith("i")) {
            intersects_number++;
        }
        obj.close();
        Intersection intersect;
        Scanner obj2 = new Scanner(mapData);
        Map map = new Map(intersects_number);
        String currentLine = obj2.nextLine();
        String[] info;

        while (currentLine.startsWith("i")) {

            info = currentLine.split("\t");
            intersect = new Intersection();
            intersect.distance = Integer.MAX_VALUE;
            intersect.IntersectionID = info[1];
            intersect.latitude = Double.parseDouble(info[2]);
            intersect.longitude = Double.parseDouble(info[3]);
            intersect.known = false;
            currentLine = obj2.nextLine();
            map.insertIntersection(intersect);
        }
        Intersection x1;
        Intersection x2;
        double distance;
        while (currentLine.startsWith("r")) {

            info = currentLine.split("\t");
            x1 = Map.intersectLookup(info[2]);
            x2 = Map.intersectLookup(info[3]);
            distance = Map.RoadDistance(x1, x2);
            map.insertRoad(new Road(info[1], info[2], info[3], distance));
            if (obj2.hasNextLine() == false) {
                break;
            }
            currentLine = obj2.nextLine();
        }

        String frametitle = "";
        if (args[0].equals("ur.txt")) {
            frametitle = "University of Rochester Campus";
        } else {
            if (args[0].equals("monroe.txt")) {
                frametitle = "Monroe County";
            } else {
                if (args[0].equals("nys.txt")) {
                    frametitle = "New York State";
                }
            }
        }
        boolean showMap = false;
        boolean dijkstras = false;
        String directionsStart = "i0";
        String directionsEnd = "i1";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-show")) {
                showMap = true;
            }
            if (args[i].equals("-directions")) {
                dijkstras = true;
                directionsStart = args[i + 1];
                directionsEnd = args[i + 2];
            }
        }
        if (dijkstras == true) {

            map.dijkstra(directionsStart);

            System.out.println("\nThe shortest path from " + directionsStart + " to " + directionsEnd + " is: ");
            System.out.print(Map.formPath(directionsEnd));

            System.out.println("The Length of this path from " + directionsStart + " to " + directionsEnd + " is: "
                    + Map.dijkstraPathLength() + " miles.");
        }
        if (showMap == true) {

            JFrame frame = new JFrame(frametitle);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            frame.getContentPane()
                    .add(new MapGUI(Map.roads, Map.intersectionMap, Map.minLat, Map.maxLat, Map.minLong, Map.maxLong));
            frame.pack();
            frame.setVisible(true);
        }
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println("\n\nTime elapsed: " + elapsedTime / 1000 + " seconds.");
        obj2.close();
    }

}
