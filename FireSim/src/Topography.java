import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class Topography {
    private ArrayList<Point3D> points;
    private static final double EPSILON = 1e-6;

    public WorldPoint topLeft = new WorldPoint(28.75, 85);
    public WorldPoint bottomRight = new WorldPoint(28.6, 85.25);

    public final int WIDTH = 1920;
    public final int HEIGHT = 1080;

    public Topography() {
        this.points = new ArrayList<>();
    }

    public Topography(String filename) {
        String[] vals = filename.split("\\s+");
        this.topLeft = new WorldPoint(Double.parseDouble(vals[0]), Double.parseDouble(vals[1]));
        this.bottomRight = new WorldPoint(Double.parseDouble(vals[2]), Double.parseDouble(vals[3]));

        this.points = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");
                if (tokens.length != 3) continue;

                int x = Integer.parseInt(tokens[0]);
                int y = Integer.parseInt(tokens[1]);
                int z = Integer.parseInt(tokens[2]);

                points.add(new Point3D(x, y, z));
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

    }

    public void addPoint(Point3D point) {
        points.add(point);
    }

    public void paint(Graphics g) {
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;

        if (interpolateZ(0,0) == -1000000) {return;}

        for (Point3D p : this.points) {
            if (p.z < minZ) minZ = p.z;
            if (p.z > maxZ) maxZ = p.z;
        }

        double rangeZ = maxZ - minZ;
        if (rangeZ == 0) rangeZ = 1;

        for (int x = 0; x < 1920; x++) {
            for (int y = 0; y < 1200; y++) {
                double interpolatedZ = interpolateZ(x, y);
                double normalized = (interpolatedZ - minZ) / rangeZ;

                int red = (int)(normalized * 255);
                if (red > 255) {red = 255;}
                if (red < 0) {red = 0;}
                int green = (int)((1 - normalized) * 255);
                if (green > 255) {green = 255;}
                if (green < 0) {green = 0;}

                g.setColor(new Color(red, green, 0));
                g.fillRect(x, y, 1, 1);
            }
        }

//        for (Point3D p: points) {
//            g.setColor(Color.BLACK);
//            g.fillRect(p.x-2,p.y-2,4,4);
//        }
    }

    public void savePoints() {
        String filePath = topLeft.latitude + " " + topLeft.longitude + " " + bottomRight.latitude + " " + bottomRight.longitude;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Point3D point : points) {
                String line = point.x + " " + point.y + " " + point.z;
                writer.write(line);
                writer.newLine();
            }
            System.out.println("Points written successfully to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }

    public WorldPoint getWorldCoords(Point p) {
        double x = (topLeft.longitude + ((bottomRight.longitude - topLeft.longitude) / (double) WIDTH) * (double)p.x);
        double y = bottomRight.latitude + ((bottomRight.latitude - topLeft.latitude) /(double) HEIGHT) * (double)p.y;
        return new WorldPoint(x, y);
    }

    public Point worldToScreen(WorldPoint point) {
        double xRatio = (point.longitude - topLeft.longitude) / (bottomRight.longitude - topLeft.longitude);
        double yRatio = (topLeft.latitude - point.latitude) / (topLeft.latitude - bottomRight.latitude);
        int x = (int) (xRatio * WIDTH);
        int y = (int) (yRatio * HEIGHT) - HEIGHT;
        return new Point(x, y);
    }

    public double interpolateZ(double x, double y) {
        if (points.isEmpty()) {return -1000000;}

        double numerator = 0.0;
        double denominator = 0.0;

        for (Point3D p : points) {
            double dx = p.x - x;
            double dy = p.y - y;
            double distanceSquared = dx * dx + dy * dy;

            if (distanceSquared < EPSILON) {return p.z;}

            double weight = 1.0 / distanceSquared;
            numerator += weight * p.z;
            denominator += weight;
        }

        return numerator / denominator;
    }

    public ArrayList<Point3D> getPoints() {
        return points;
    }
}
