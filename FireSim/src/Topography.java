import java.awt.*;
import java.util.ArrayList;

public class Topography {
    private ArrayList<Point3D> points;
    private static final double EPSILON = 1e-6;

    public Point topLeft = new Point(119,37);
    public Point bottomRight = new Point(111, 35);

    public final int WIDTH = 1920;
    public final int HEIGHT = 1080;

    public Topography() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point3D point) {
        points.add(point);
    }

    public void paint(Graphics g) {
        // Step 1: Find min and max Z
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;

        for (Point3D p : this.points) {
            if (p.z < minZ) minZ = p.z;
            if (p.z > maxZ) maxZ = p.z;
        }

        double rangeZ = maxZ - minZ;
        if (rangeZ == 0) rangeZ = 1; // avoid division by zero

        // Step 2: Render pixel by pixel
        for (int x = 0; x < 1920; x++) {
            for (int y = 0; y < 1200; y++) {
                double interpolatedZ = interpolateZ(x, y);
                double normalized = (interpolatedZ - minZ) / rangeZ;

                int red = (int)(normalized * 255);
                int green = (int)((1 - normalized) * 255);

                g.setColor(new Color(red, green, 0));
                g.fillRect(x, y, 1, 1);
            }
        }

        for (Point3D p: points) {
            g.setColor(Color.BLACK);
            g.fillRect(p.x-2,p.y-2,4,4);
        }
    }

    public Point GetWorldCoords(Point p) {
        double x = topLeft.x + (double) (topLeft.x - bottomRight.x) /WIDTH;
        double y = bottomRight.y + (double) (bottomRight.y - topLeft.y) /HEIGHT;
        return new Point((int)x, (int)y);
    }

    public double interpolateZ(double x, double y) {
        if (points.isEmpty()) {
            throw new IllegalStateException("No points in topography.");
        }

        double numerator = 0.0;
        double denominator = 0.0;

        for (Point3D p : points) {
            double dx = p.x - x;
            double dy = p.y - y;
            double distanceSquared = dx * dx + dy * dy;

            // If the point is exactly at the requested location, return its z
            if (distanceSquared < EPSILON) {
                return p.z;
            }

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
