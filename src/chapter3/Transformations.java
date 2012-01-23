package chapter3;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;

public class Transformations extends JApplet implements ActionListener {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Affine Transforms");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Transformations();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  TransformPanel panel = null;

  public void init() {
    JMenuBar mb = new JMenuBar();
    setJMenuBar(mb);
    JMenu menu = new JMenu("Transforms");
    mb.add(menu);
    JMenuItem mi = new JMenuItem("Translation");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Rotation");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Scaling");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Shearing");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Reflection");
    mi.addActionListener(this);
    menu.add(mi);

    panel = new TransformPanel();
    getContentPane().add(panel);
  }

  public void actionPerformed(ActionEvent ev) {
    String command = ev.getActionCommand();
    if ("Translation".equals(command)) {
      panel.transformType = panel.TRANSLATION;
    } else if ("Rotation".equals(command)) {
      panel.transformType = panel.ROTATION;
    } else if ("Scaling".equals(command)) {
      panel.transformType = panel.SCALING;
    } else if ("Shearing".equals(command)) {
      panel.transformType = panel.SHEARING;
    } else if ("Reflection".equals(command)) {
      panel.transformType = panel.REFLECTION;
    }
  }
}

class TransformPanel extends JPanel implements MouseListener, MouseMotionListener {
  static final int NONE = 0;
  static final int TRANSLATION = 1;
  static final int ROTATION = 2;
  static final int SCALING = 3;
  static final int SHEARING = 4;
  static final int REFLECTION = 5;

  int transformType = NONE;
  Shape drawShape = null;
  Shape tempShape = null;
  Point p = null;
  int x0 = 400;
  int y0 = 300;

  public TransformPanel() {
    super();
    setPreferredSize(new Dimension(800, 600));
    setBackground(Color.white);
    drawShape = new Rectangle(-50,-50,100,100);
    addMouseListener(this);
    addMouseMotionListener(this);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.translate(x0, y0);
    g2.drawLine(-200,0,200,0);
    g2.drawLine(0,-200,0,200);
    g2.draw(drawShape);
  }

  public void mouseClicked(MouseEvent ev) {
  }

  public void mouseEntered(MouseEvent ev) {
  }

  public void mouseExited(MouseEvent ev) {
  }

  public void mousePressed(MouseEvent ev) {
    p = ev.getPoint();
  }

  public void mouseReleased(MouseEvent ev) {
    Graphics g = getGraphics();
    Point p1 = ev.getPoint();
    AffineTransform tr = new AffineTransform();
    switch (transformType) {
      case TRANSLATION:
        tr.setToTranslation(p1.x-p.x, p1.y-p.y);
        break;
      case ROTATION:
        double a = Math.atan2(p1.y-y0, p1.x-x0) - Math.atan2(p.y-y0, p.x-x0);
        tr.setToRotation(a);
        break;
      case SCALING:
        double sx = Math.abs((double)(p1.x-x0)/(p.x-x0));
        double sy = Math.abs((double)(p1.y-y0)/(p.y-y0));
        tr.setToScale(sx, sy);
        break;
      case SHEARING:
        double shx = ((double)(p1.x-x0)/(p.x-x0))-1;
        double shy = ((double)(p1.y-y0)/(p.y-y0))-1;
        tr.setToShear(shx, shy);
        break;
      case REFLECTION:
        tr.setTransform(-1,0,0,1,0,0);
        break;
    }
    drawShape = tr.createTransformedShape(drawShape);
    repaint();
  }

  public void mouseMoved(MouseEvent ev) {
  }

  public void mouseDragged(MouseEvent ev) {
    Point p1 = ev.getPoint();
    AffineTransform tr = new AffineTransform();
    switch (transformType) {
      case TRANSLATION:
        tr.setToTranslation(p1.x-p.x, p1.y-p.y);
        break;
      case ROTATION:
        double a = Math.atan2(p1.y-y0, p1.x-x0) - Math.atan2(p.y-y0, p.x-x0);
        tr.setToRotation(a);
        break;
      case SCALING:
        double sx = Math.abs((double)(p1.x-x0)/(p.x-x0));
        double sy = Math.abs((double)(p1.y-y0)/(p.y-y0));
        tr.setToScale(sx, sy);
        break;
      case SHEARING:
        double shx = ((double)(p1.x-x0)/(p.x-x0))-1;
        double shy = ((double)(p1.y-y0)/(p.y-y0))-1;
        tr.setToShear(shx, shy);
        break;
      case REFLECTION:
        tr.setTransform(-1,0,0,1,0,0);
        break;
    }
    Graphics2D g = (Graphics2D)getGraphics();
    g.setXORMode(Color.white);
    g.translate(x0, y0);
    if (tempShape != null)
      g.draw(tempShape);
    tempShape = tr.createTransformedShape(drawShape);
    g.draw(tempShape);
  }
}