package chapter2;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class DrawShapes extends JApplet implements ActionListener {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Drawing Geometric Shapes");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new DrawShapes();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }

  JavaDraw2DPanel panel = null;

  public void init() {
    JMenuBar mb = new JMenuBar();
    setJMenuBar(mb);
    JMenu menu = new JMenu("Objects");
    mb.add(menu);
    JMenuItem mi = new JMenuItem("Rectangle");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("RoundRectangle");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Ellipse");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Arc");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Line");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("QuadCurve");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("CubicCurve");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Polygon");
    mi.addActionListener(this);
    menu.add(mi);
    panel = new JavaDraw2DPanel();
    getContentPane().add(panel);
  }

  public void actionPerformed(ActionEvent ev) {
    String command = ev.getActionCommand();
    if ("Rectangle".equals(command)) {
      panel.shapeType = panel.RECTANGLE;
    } else if ("RoundRectangle".equals(command)) {
      panel.shapeType = panel.ROUNDRECTANGLE2D;
    } else if ("Ellipse".equals(command)) {
      panel.shapeType = panel.ELLIPSE2D;
    } else if ("Arc".equals(command)) {
      panel.shapeType = panel.ARC2D;
    } else if ("Line".equals(command)) {
      panel.shapeType = panel.LINE2D;
    } else if ("QuadCurve".equals(command)) {
      panel.shapeType = panel.QUADCURVE2D;
    } else if ("CubicCurve".equals(command)) {
      panel.shapeType = panel.CUBICCURVE2D;
    } else if ("Polygon".equals(command)) {
      panel.shapeType = panel.POLYGON;
    }
  }
}

class JavaDraw2DPanel extends JPanel implements MouseListener, MouseMotionListener {
  private Vector shapes = new Vector();
  static final int RECTANGLE = 0;
  static final int ROUNDRECTANGLE2D = 1;
  static final int ELLIPSE2D = 2;
  static final int ARC2D = 3;
  static final int LINE2D = 4;
  static final int QUADCURVE2D = 5;
  static final int CUBICCURVE2D = 6;
  static final int POLYGON = 7;
  static final int GENERAL = 8;
  static final int AREA = 9;

  int shapeType = RECTANGLE;
  // vector of input points
  Vector points = new Vector();
  int pointIndex = 0;
  Shape partialShape = null;
  Point p = null;

  public JavaDraw2DPanel() {
    super();
    setBackground(Color.white);
    setPreferredSize(new Dimension(640, 480));
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    for (int i = 0; i < shapes.size(); i++) {
      Shape s = (Shape)shapes.get(i);
      g2.draw(s);
    }
  }

  public void mouseClicked(MouseEvent ev) {
  }

  public void mouseEntered(MouseEvent ev) {
  }

  public void mouseExited(MouseEvent ev) {
  }

  public void mousePressed(MouseEvent ev) {
    points.add(ev.getPoint());
    pointIndex++;
    p = null;
  }

  public void mouseReleased(MouseEvent ev) {
    Graphics g = getGraphics();
    Point p1 = (Point)(points.get(pointIndex-1));
    p = ev.getPoint();
    Shape s = null;
    switch (shapeType) {
      case RECTANGLE:
        s = new Rectangle(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        break;
      case ROUNDRECTANGLE2D:
        s = new RoundRectangle2D.Float(p1.x, p1.y, p.x-p1.x, p.y-p1.y, 10, 10);
        break;
      case ELLIPSE2D:
        s = new Ellipse2D.Float(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        break;
      case ARC2D:
        s = new Arc2D.Float(p1.x, p1.y, p.x-p1.x, p.y-p1.y, 30, 120, Arc2D.OPEN);
        break;
      case LINE2D:
        s = new Line2D.Float(p1.x, p1.y, p.x, p.y);
        break;
      case QUADCURVE2D:
        if (pointIndex > 1) {
          Point p2 = (Point)points.get(0);
          s = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
        }
        break;
      case CUBICCURVE2D:
        if (pointIndex > 2) {
          Point p2 = (Point)points.get(pointIndex-2);
          Point p3 = (Point)points.get(pointIndex-3);
          s = new CubicCurve2D.Float(p3.x, p3.y, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
        }
        break;
      case POLYGON:
        if (ev.isShiftDown()) {
          s = new Polygon();
          for (int i = 0; i < pointIndex; i++) ((Polygon)s).addPoint(((Point)points.get(i)).x, ((Point)points.get(i)).y);
          ((Polygon)s).addPoint(p.x, p.y);
        }

    }
    if (s != null) {
      shapes.add(s);
      points.clear();
      pointIndex = 0;
      p = null;
      repaint();
    }
  }

  public void mouseMoved(MouseEvent ev) {
  }

  public void mouseDragged(MouseEvent ev) {
    Graphics2D g = (Graphics2D)getGraphics();
    g.setXORMode(Color.white);
    Point p1 = (Point)points.get(pointIndex-1);
    switch (shapeType) {
      case RECTANGLE:
        if (p != null) g.drawRect(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        p = ev.getPoint();
        g.drawRect(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        break;
      case ROUNDRECTANGLE2D:
        if (p != null) g.drawRoundRect(p1.x, p1.y, p.x-p1.x, p.y-p1.y,10,10);
        p = ev.getPoint();
        g.drawRoundRect(p1.x, p1.y, p.x-p1.x, p.y-p1.y,10,10);
        break;
      case ELLIPSE2D:
        if (p != null) g.drawOval(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        p = ev.getPoint();
        g.drawOval(p1.x, p1.y, p.x-p1.x, p.y-p1.y);
        break;
      case ARC2D:
        if (p != null) g.drawArc(p1.x, p1.y, p.x-p1.x, p.y-p1.y, 30, 120);
        p = ev.getPoint();
        g.drawArc(p1.x, p1.y, p.x-p1.x, p.y-p1.y, 30, 120);
        break;
      case LINE2D:
      case POLYGON:
        if (p != null) g.drawLine(p1.x, p1.y, p.x, p.y);
        p = ev.getPoint();
        g.drawLine(p1.x, p1.y, p.x, p.y);
        break;
      case QUADCURVE2D:
        if (pointIndex == 1) {
          if (p != null) g.drawLine(p1.x, p1.y, p.x, p.y);
          p = ev.getPoint();
          g.drawLine(p1.x, p1.y, p.x, p.y);
        } else {
          Point p2 = (Point)points.get(pointIndex-2);
          if (p != null) g.draw(partialShape);
          p = ev.getPoint();
          partialShape = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
          g.draw(partialShape);
        }
        break;
      case CUBICCURVE2D:
        if (pointIndex == 1) {
          if (p != null) g.drawLine(p1.x, p1.y, p.x, p.y);
          p = ev.getPoint();
          g.drawLine(p1.x, p1.y, p.x, p.y);
        } else if (pointIndex == 2) {
          Point p2 = (Point)points.get(pointIndex-2);
          if (p != null) g.draw(partialShape);
          p = ev.getPoint();
          partialShape = new QuadCurve2D.Float(p2.x, p2.y, p1.x, p1.y, p.x, p.y);
          g.draw(partialShape);
        } else {
          Point p2 = (Point)points.get(pointIndex-2);
          Point p3 = (Point)points.get(pointIndex-3);
          if (p != null) g.draw(partialShape);
          p = ev.getPoint();
          partialShape = new CubicCurve2D.Float(p3.x, p3.y, p2.x, p2.y, p1.x, p1.y, p.x, p.y);
          g.draw(partialShape);
        }
        break;
    }
  }
}
