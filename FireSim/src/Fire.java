import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class Fire {
    private Polygon firePolygon;
    public List<Point> firePoints;  // All current outer points (used to build polygon)
    private double ROC;
    private double duePoints;

    private Random rand;

    public Fire(int initx, int inity) {
        this.firePoints = new ArrayList<>();
        this.ROC = 57;
        this.duePoints = 0;
        this.rand = new Random(System.currentTimeMillis());

        int size = 5;
        firePolygon = new Polygon();
        firePolygon.addPoint(initx - size, inity - size);
        firePolygon.addPoint(initx + size, inity - size);
        firePolygon.addPoint(initx, inity + size);

        firePoints.addAll(Arrays.asList(
                new Point(initx - size, inity - size),
                new Point(initx + size, inity - size),
                new Point(initx, inity + size)
        ));
    }

    public void update() {
        this.duePoints += ROC;

        for (int i = 0; i < (int)(duePoints); i++) {
            Point newPoint = generatePointNearBorder();
            if (newPoint != null && !firePolygon.contains(newPoint)) {
                firePoints.add(newPoint);
                rebuildPolygon();
            }
            this.duePoints -= 1;
        }
    }

    public void paint(Graphics g) {
        g.setColor(new Color(250, 130, 0));
        g.fillPolygon(firePolygon);
        g.setColor(Color.BLACK);
        for (Point p: firePoints) {
            g.fillRect(p.x-5,p.y-5,10,10);
        }
    }

    // Generate a new point at a threshold distance away from the polygon border
    private Point generatePointNearBorder() {
        int maxTries = 100;

        for (int i = 0; i < maxTries; i++) {
            // Pick a random edge of the polygon
            int n = firePolygon.npoints;
            int idx = rand.nextInt(n);
            Point p1 = new Point(firePolygon.xpoints[idx], firePolygon.ypoints[idx]);
            Point p2 = new Point(firePolygon.xpoints[(idx + 1) % n], firePolygon.ypoints[(idx + 1) % n]);

            // Get a random point along the edge
            double t = rand.nextDouble();
            int edgeX = (int)(p1.x * (1 - t) + p2.x * t);
            int edgeY = (int)(p1.y * (1 - t) + p2.y * t);

            double angle = rand.nextDouble() * 2 * Math.PI;
            int threshold = 3;
            int newX = edgeX + (int)(threshold * Math.cos(angle));
            int newY = edgeY + (int)(threshold * Math.sin(angle));
            return new Point(newX, newY);
        }

        return null;
    }

    // Rebuild the polygon from the convex hull of all points
    private void rebuildPolygon() {
        List<Point> hull = computeConvexHull(firePoints);
        firePolygon.reset();
        for (Point p : hull) {
            firePolygon.addPoint(p.x, p.y);
        }
        firePoints = new ArrayList<>(hull);
    }

    // Compute convex hull using Graham scan
    private List<Point> computeConvexHull(List<Point> points) {
        if (points.size() <= 3) return new ArrayList<>(points);

        points.sort((a, b) -> {
            if (a.x != b.x) return Integer.compare(a.x, b.x);
            return Integer.compare(a.y, b.y);
        });

        List<Point> lower = new ArrayList<>();
        for (Point p : points) {
            while (lower.size() >= 2 && cross(lower.get(lower.size() - 2), lower.getLast(), p) <= 0) {
                lower.removeLast();
            }
            lower.add(p);
        }

        List<Point> upper = new ArrayList<>();
        for (int i = points.size() - 1; i >= 0; i--) {
            Point p = points.get(i);
            while (upper.size() >= 2 && cross(upper.get(upper.size() - 2), upper.getLast(), p) <= 0) {
                upper.removeLast();
            }
            upper.add(p);
        }

        lower.removeLast();
        upper.removeLast();

        lower.addAll(upper);
        return lower;
    }

    private int cross(Point o, Point a, Point b) {
        // Cross product of OA × OB
        return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x);
    }

}
