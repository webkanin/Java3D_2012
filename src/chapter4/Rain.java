
package chapter4;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Rain extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Rain");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Rain();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new RainPanel();
    getContentPane().add(panel);
  }
}

class RainPanel extends JPanel implements Runnable{
  Point2D.Double[] pts = new Point2D.Double[1200];
  
  public RainPanel() {
    setPreferredSize(new Dimension(640, 480));
    setBackground(Color.gray);
    for (int i = 0; i < pts.length; i++) {
      pts[i] = new Point2D.Double(Math.random(), Math.random());
    }
    Thread thread = new Thread(this);
    thread.start();
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(Color.white);
    for (int i = 0; i < pts.length; i++) {
      int x = (int)(640*pts[i].x);
      int y = (int)(480*pts[i].y);
      int h = (int)(25*Math.random());
      g.drawLine(x, y, x, y+h);
    }
  }
  
  public void run() {
    while(true) {
      for (int i = 0; i < pts.length; i++) {
        double x = pts[i].getX();
        double y = pts[i].getY();
        y += 0.1*Math.random();
        if (y > 1) {
          y = 0.3*Math.random();
          x = Math.random();
        }
        pts[i].setLocation(x, y);
      }
      repaint();
      try {
        Thread.sleep(100);
      } catch (InterruptedException ex) {}
    }
  }  
}
