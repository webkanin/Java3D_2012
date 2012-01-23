package chapter4;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class BSpline extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("B-Spline");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new BSpline();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new BSplinePanel();
    getContentPane().add(panel);
  }
}

class BSplinePanel extends JPanel implements MouseListener, MouseMotionListener {
  Vector points = null;
  boolean completed = true;

  public BSplinePanel() {
    setPreferredSize(new Dimension(640, 480));
    setBackground(Color.white);
    addMouseListener(this);
    addMouseMotionListener(this);
    points = new Vector();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    Point p0 = null;
    Point p1 = null;
    Point p2 = null;
    Point p3 = null;
    float x1,y1,x2,y2,x3,y3, x4, y4;
    Iterator it = points.iterator();
    if (it.hasNext()) {
      p1 = (Point)(it.next());
    }
    while (it.hasNext()) {
      p2 = (Point)(it.next());
      g2.drawLine(p1.x, p1.y, p2.x, p2.y);
      p1 = p2;
    }

    GeneralPath spline = new GeneralPath();
    int n = points.size();
    if (n == 0) return;
    p1 = (Point)points.get(0);
    spline.moveTo(p1.x, p1.y);
    g2.drawRect(p1.x-3, p1.y-3, 6, 6);
    p1 = (Point)points.get(1);
    p2 = (Point)points.get(2);
    p3 = (Point)points.get(3);
    x1 = p1.x;
    y1 = p1.y;
    x2 = (p1.x + p2.x)/2.0f;
    y2 = (p1.y + p2.y)/2.0f;
    x4 = (2.0f*p2.x+p3.x)/3.0f;
    y4 = (2.0f*p2.y+p3.y)/3.0f;
    x3 = (x2+x4)/2.0f;
    y3 = (y2+y4)/2.0f;
    spline.curveTo(x1, y1, x2, y2, x3, y3);
    g2.drawRect((int)x1-3, (int)y1-3, 6, 6);
    g2.drawRect((int)x2-3, (int)y2-3, 6, 6);
    g2.drawRect((int)x3-3, (int)y3-3, 6, 6);
    for (int i = 2; i < n - 4; i++) {
      p1 = p2;
      p2 = p3;
      p3 = (Point)points.get(i+2);
      x1 = x4;
      y1 = y4;
      x2 = (p1.x+2.0f*p2.x)/3.0f;
      y2 = (p1.y+2.0f*p2.y)/3.0f;
      x4 = (2.0f*p2.x+p3.x)/3.0f;
      y4 = (2.0f*p2.y+p3.y)/3.0f;
      x3 = (x2+x4)/2.0f;
      y3 = (y2+y4)/2.0f;
      spline.curveTo(x1,y1,x2,y2,x3,y3);
      g2.drawRect((int)x1-3, (int)y1-3, 6, 6);
      g2.drawRect((int)x2-3, (int)y2-3, 6, 6);
      g2.drawRect((int)x3-3, (int)y3-3, 6, 6);
    }
    p1 = p2;
    p2 = p3;
    p3 = (Point)points.get(n-2);
    x1 = x4;
    y1 = y4;
    x2 = (p1.x+2.0f*p2.x)/3.0f;
    y2 = (p1.y+2.0f*p2.y)/3.0f;
    x4 = (p2.x+p3.x)/2.0f;
    y4 = (p2.y+p3.y)/2.0f;
    x3 = (x2+x4)/2.0f;
    y3 = (y2+y4)/2.0f;
    spline.curveTo(x1,y1,x2,y2,x3,y3);
    g2.drawRect((int)x1-3, (int)y1-3, 6, 6);
    g2.drawRect((int)x2-3, (int)y2-3, 6, 6);
    g2.drawRect((int)x3-3, (int)y3-3, 6, 6);
    p2 = p3;
    p3 = (Point)points.get(n-1);
    x1 = x4;
    y1 = y4;
    x2 = p2.x;
    y2 = p2.y;
    x3 = p3.x;
    y3 = p3.y;
    spline.curveTo(x1,y1,x2,y2,x3,y3);
    g2.drawRect((int)x1-3, (int)y1-3, 6, 6);
    g2.drawRect((int)x2-3, (int)y2-3, 6, 6);
    g2.drawRect((int)x3-3, (int)y3-3, 6, 6);
    g2.draw(spline);
  }
  public void mouseClicked(MouseEvent ev) {
  }

  public void mouseEntered(MouseEvent ev) {
  }

  public void mouseExited(MouseEvent ev) {
  }

  public void mousePressed(MouseEvent ev) {
    Graphics g = getGraphics();
    if (completed) {
      points.clear();
      completed = false;
    }
    if (ev.getClickCount() == 1) {
      Point p =ev.getPoint();
      points.add(p);
      g.fillOval(p.x-3, p.y-3, 6, 6);
    }
  }

  public void mouseReleased(MouseEvent ev) {
    if (ev.getClickCount() > 1) {
      completed = true;
      repaint();
    }
  }

  public void mouseMoved(MouseEvent ev) {
  }

  public void mouseDragged(MouseEvent ev) {
  }
}