package chapter2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class AreaGeometry extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Constructive Area Geometry");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new AreaGeometry();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new AreaPanel();
    getContentPane().add(panel);
  }
}

class AreaPanel extends JPanel {
  public AreaPanel() {
    setPreferredSize(new Dimension(760, 400));
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    Shape s1 = new Ellipse2D.Double(0, 0, 100, 100);
    Shape s2 = new Ellipse2D.Double(60, 0, 100, 100);
    Area a1;
    Area a2 = new Area(s2);
    g2.translate(20, 50);
    g2.draw(s1);
    g2.draw(s2);
    g2.translate(0,200);
    a1 = new Area(s1);
    a1.add(a2);
    g2.fill(a1);
    g2.translate(180,0);
    a1 = new Area(s1);
    a1.intersect(a2);
    g2.fill(a1);
    g2.translate(180,0);
    a1 = new Area(s1);
    a1.subtract(a2);
    g2.fill(a1);
    g2.translate(180,0);
    a1 = new Area(s1);
    a1.exclusiveOr(a2);
    g2.fill(a1);
   }
}