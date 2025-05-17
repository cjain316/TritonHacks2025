import java.awt.*;
import java.util.ArrayList;

public class Fire {
    public ArrayList<Point> firePoints;
    public double ROC;

    public Fire(int initx, int inity) {
        firePoints = new ArrayList<Point>();
        ROC = 1;
        firePoints.add(new Point(initx, inity));
    }

    public void update() {
        
    }

    public void paint(Graphics g) {
        g.setColor(new Color(250, 130, 0));
        for (Point p: firePoints) {
            g.fillRect(p.x, p.y, 1,  1);
        }
    }


}
