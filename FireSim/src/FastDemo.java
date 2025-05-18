import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class FastDemo extends JPanel implements KeyListener, ActionListener, MouseListener {
    Fire f;
    FPSCounter fps = new FPSCounter();
    Topography top;
    Topography eftop;
    Request req = new Request();

    public static void main(String[] args) throws IOException {
        FastDemo m = new FastDemo();
    }

    public ArrayList<WorldPoint> getRequestPoints(int resolution) {
        ArrayList<WorldPoint> loc = new ArrayList<WorldPoint>();
        for (int x = 0; x < 1920; x += resolution) {
            for (int y = 0; y < 1200; y += resolution) {
                loc.add(top.getWorldCoords(new Point(x, y)));
            }
        }
        return loc;
    }

    public Image Sprite = getImage("mapoverlay.png");

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 2000, 2000);
        //eftop.paint(g);
        if (f != null) {
            f.ROC = 57;
            f.update(top);
            f.paint(g2);
        }

        AffineTransform tx = AffineTransform.getTranslateInstance(0, 0);
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
        g2.drawImage(Sprite, tx, null);

        updateFPS(g2);
        if (f != null) {g2.drawString("Total Points: " + f.firePoints.size(),10,20);}
    }

    protected Image getImage(String path) {
        Image tempImage = null;
        try {
            URL imageURL = Main.class.getResource(path);
            tempImage    = Toolkit.getDefaultToolkit().getImage(imageURL);
        } catch (Exception e) {e.printStackTrace();}
        return tempImage;
    }

    public void updateFPS(Graphics2D g2) {
        fps.update();
        g2.setColor(Color.BLACK);
        g2.drawString("FPS: " + fps.FPS,10,10);
    }

    Timer t;

    public void calculateTopFromReqs(Topography t, int resolution) throws IOException {
        ArrayList<WorldPoint> locations = getRequestPoints(resolution);
        System.out.println(locations.size());

        String temp = "json?locations=39.7391536%2C-104.9847034|32.0%2C117.0";
        String url = "json?locations=";
        for (int i = 0; i < locations.size(); i++) {
            //double lat = (double)Math.round(p.latitude * 10000000d) / 10000000d;
            //double lon = (double)Math.round(p.longitude * 10000000d) / 10000000d;
            //System.out.println(locations.get(i).longitude);
            url += ""+locations.get(i).longitude+"%2C"+locations.get(i).latitude+"|";
            if (i % 400 == 0 && i != 0 || i == locations.size() - 1) {
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
                    Point tp = t.worldToScreen(wp);
                    t.addPoint(new Point3D(tp.x, tp.y, (int) altitude));
                    System.out.println(altitude);
                }
                req = new Request();
                url = "json?locations=";
            }
        }
        t.savePoints();
    }

    public FastDemo() throws IOException {
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

        top = new Topography("32.887667 -117.2559635 32.8777387 -117.2282074");
        eftop = new Topography("32.887668 -117.2559634 32.8777386 -117.2282073");
        this.f = new Fire(250,100, top);
        //eftop = new Topography(new WorldPoint(32.887668, -117.2559634), new WorldPoint(32.8777386, -117.2282073));
        //calculateTopFromReqs(eftop, 240);
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
        f = new Fire(e.getX(), e.getY(), top);
        System.out.println(top.interpolateZ(e.getX(), e.getY()));
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