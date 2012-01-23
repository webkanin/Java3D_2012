package chapter3;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class Composition extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Transformation Composition");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Composition();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new CompositionPanel();
    getContentPane().add(panel);
  }
}

class CompositionPanel extends JPanel {
  public CompositionPanel() {
    setPreferredSize(new Dimension(640, 480));
    this.setBackground(Color.white);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.translate(100,100);
    Shape e = new Ellipse2D.Double(300, 200, 200, 100);
    g2.setColor(new Color(160,160,160));
    g2.fill(e);
    AffineTransform transform = new AffineTransform();
    transform.translate(-400,-250);
    e = transform.createTransformedShape(e);
    g2.setColor(new Color(220,220,220));
    g2.fill(e);
    g2.setColor(Color.black);
    g2.drawLine(0, 0, 150, 0);
    g2.drawLine(0, 0, 0, 150);
    transform.setToRotation(Math.PI / 6.0);
    e = transform.createTransformedShape(e);
    g2.setColor(new Color(100,100,100));
    g2.draw(e);
    transform.setToTranslation(400, 250);
    e = transform.createTransformedShape(e);
    g2.setColor(new Color(0,0,0));
    g2.draw(e);
  }
}