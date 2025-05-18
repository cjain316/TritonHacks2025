import java.awt.*;
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

    public void addPoint(Point3D point) {
        points.add(point);
    }

    public void paint(Graphics g) {
        int minZ = Integer.MAX_VALUE;
        int maxZ = Integer.MIN_VALUE;

        if (interpolateZ(0,0) == -1.111111) {
            return;
        }

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

    public WorldPoint getWorldCoords(Point p) {
        double x = (topLeft.longitude + ((bottomRight.longitude - topLeft.longitude) / (double) WIDTH) * (double)p.x);
        double y = bottomRight.latitude + ((bottomRight.latitude - topLeft.latitude) /(double) HEIGHT) * (double)p.y;
        return new WorldPoint(x, y);
    }

    public Point worldToScreen(WorldPoint point) {
        // Longitude maps to X axis
        double xRatio = (point.longitude - topLeft.longitude) / (bottomRight.longitude - topLeft.longitude);

        // Latitude maps to Y axis - note that latitude decreases as you go down the screen
        double yRatio = (topLeft.latitude - point.latitude) / (topLeft.latitude - bottomRight.latitude);

        int x = (int) (xRatio * WIDTH);
        int y = (int) (yRatio * HEIGHT) - HEIGHT;

        return new Point(x, y);
    }

    public double interpolateZ(double x, double y) {
        if (points.isEmpty()) {
            return -1.111111;
        }

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
