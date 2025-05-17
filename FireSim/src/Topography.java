import java.util.ArrayList;

public class Topography {
    private ArrayList<Point3D> points;
    private static final double EPSILON = 1e-6;

    public Topography() {
        this.points = new ArrayList<>();
    }

    public void addPoint(Point3D point) {
        points.add(point);
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
