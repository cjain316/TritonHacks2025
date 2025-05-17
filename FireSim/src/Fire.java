import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Fire {
    public ArrayList<Point> firePoints;
    public double ROC;
    private double duePoints;

    public Fire(int initx, int inity) {
        firePoints = new ArrayList<Point>();
        ROC = 57;
        firePoints.add(new Point(initx, inity));
    }

    public void update() {
        this.duePoints += ROC;
        System.out.println(duePoints);
        for (int i = 0; i < (int)(duePoints); i++) {
            ArrayList<Point> points = getAdjacentPoints();
            if (points != null) {
                firePoints.add(points.get((int)(Math.random()*points.size())));
            }
            this.duePoints -= 1;
        }
    }

    public void paint(Graphics g) {
        g.setColor(new Color(250, 130, 0));
        for (Point p: firePoints) {
            g.fillRect(p.x, p.y, 1,  1);
        }
    }

    private ArrayList<Point> getAdjacentPoints() {
        if (firePoints == null || firePoints.isEmpty()) return null;

        Set<Point> pointSet = new HashSet<>(firePoints); // eliminate duplicates
        Set<Point> adjacentSet = new HashSet<>();

        for (Point p : pointSet) {
            int x = p.x;
            int y = p.y;

            Point[] neighbors = {
                    new Point(x + 1, y),
                    new Point(x - 1, y),
                    new Point(x, y + 1),
                    new Point(x, y - 1)
            };

            for (Point neighbor : neighbors) {
                if (!pointSet.contains(neighbor)) {
                    adjacentSet.add(neighbor);
                }
            }
        }

        return adjacentSet.isEmpty() ? null : new ArrayList<>(adjacentSet);
    }
}
