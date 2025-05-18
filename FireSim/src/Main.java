import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main extends JPanel implements KeyListener, ActionListener, MouseListener {
    Fire f = new Fire(920,560);
    FPSCounter fps = new FPSCounter();
    Topography top = new Topography();
    Request req = new Request();

    public static void main(String[] args) throws IOException {
        Main m = new Main();
    }

    public ArrayList<WorldPoint> getRequestPoints() {
        ArrayList<WorldPoint> loc = new ArrayList<WorldPoint>();
        for (int x = 0; x < 1920; x += 10) {
            for (int y = 0; y < 1200; y += 10) {
                loc.add(top.getWorldCoords(new Point(x, y)));
            }
        }
        return loc;
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 2000, 2000);
        top.paint(g);
        f.update();
        f.paint(g2);
        updateFPS(g2);
        g2.drawString("Total Points: " + f.firePoints.size(),10,20);
    }

    public void updateFPS(Graphics2D g2) {
        fps.update();
        g2.setColor(Color.BLACK);
        g2.drawString("FPS: " + fps.FPS,10,10);
    }

    Timer t;

    public void calculateTopFromReqs() throws IOException {
        ArrayList<WorldPoint> locations = getRequestPoints();
        System.out.println(locations.size());

        String temp = "json?locations=39.7391536%2C-104.9847034|32.0%2C117.0";
        String url = "json?locations=";
        for (int i = 0; i < locations.size(); i++) {
            //double lat = (double)Math.round(p.latitude * 10000000d) / 10000000d;
            //double lon = (double)Math.round(p.longitude * 10000000d) / 10000000d;
            //System.out.println(locations.get(i).longitude);
            url += ""+locations.get(i).longitude+"%2C"+locations.get(i).latitude+"|";
            if (i % 400 == 0 && i != 0) {
                url = url.substring(0,url.length()-1);
                System.out.println(url);
                String fin = req.get(url);
                System.out.println(fin);
                Json  j = new Json(fin);

                System.out.println(j.getMap());
                System.out.println(j.getMap().get("results"));

                ArrayList<HashMap<String, Object>> burg = (ArrayList<HashMap<String, Object>>) j.getMap().get("results");
                for (HashMap<String, Object> h: burg) {
                    double altitude = Double.parseDouble(h.get("elevation").toString());
                    HashMap<String, Object> location = (HashMap<String, Object>) h.get("location");
                    double latitude = Double.parseDouble(location.get("lat").toString());
                    double longitude = Double.parseDouble(location.get("lng").toString());

                    WorldPoint wp = new WorldPoint(latitude, longitude);
                    Point tp = top.worldToScreen(wp);
                    top.addPoint(new Point3D(tp.x, tp.y, (int) altitude));
                    System.out.println(altitude);
                }
                req = new Request();
                url = "json?locations=";
            }
        }
        top.savePoints();
    }

    public Main() throws IOException {
        JFrame f = new JFrame("LaFireSim2025");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(this);
        f.addKeyListener(this);
        f.addMouseListener(this);

        ImageIcon img = new ImageIcon("favicon.png");
        f.setIconImage(img.getImage());

        f.setResizable(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        t = new Timer(1, this);
        t.start();
        f.setVisible(true);

        top = new Topography("28.75 85.0 28.6 85.25");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}