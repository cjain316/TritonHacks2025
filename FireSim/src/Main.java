import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener, ActionListener, MouseListener {
    Fire f = new Fire(920,560);
    FPSCounter fps = new FPSCounter();

    public static void main(String[] args) {
        Main m = new Main();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, 2000, 2000);
        f.update();
        f.paint(g2);
        fps.update();
        g2.setColor(Color.BLACK);
        g2.drawString("FPS: " + fps.FPS,10,10);
        g2.drawString("Total Points: " + f.firePoints.size(),10,20);
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