import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Main extends JPanel implements KeyListener, ActionListener, MouseListener {
    Fire f = new Fire(920,560);
    FPSCounter fps = new FPSCounter();
    Topography top = new Topography();

    public static void main(String[] args) throws IOException {
        //Request req = new Request();
        //System.out.println(req.get("json?locations=39.7391536%2C-104.9847034"));
        Main m = new Main();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 2000, 2000);
        top.paint(g);
        //f.update();
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

    public Main() {
        JFrame f = new JFrame("LaFireSim2025");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        f.add(this);
        f.addKeyListener(this);
        f.addMouseListener(this);

        f.setResizable(true);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        t = new Timer(1, this);
        t.start();
        f.setVisible(true);

        top.addPoint(new Point3D(100,100,100));
        top.addPoint(new Point3D(1000,1000, 200));
        top.addPoint(new Point3D(1000,100, 200));
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