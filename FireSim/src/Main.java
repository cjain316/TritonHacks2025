import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener, ActionListener, MouseListener {
    public static void main(String[] args) {
        Main m = new Main();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
    }

    Timer t;

    public Main() {
        JFrame f = new JFrame("LaFireSim2025");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(this);
        f.addKeyListener(this);
        f.addMouseListener(this);

        f.setResizable(false);
        f.setExtendedState(JFrame.MAXIMIZED_BOTH);

        t = new Timer(1, this);
        t.start();
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

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