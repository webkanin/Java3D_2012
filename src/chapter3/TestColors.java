
package chapter3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.geom.*;

public class TestColors extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Colors");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new TestColors();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  ColorPanel panel;
  public void init() {
    panel = new ColorPanel();
    Container cp = getContentPane();
    cp.setLayout(new BorderLayout());
    cp.add(panel, BorderLayout.CENTER);
    JPanel p = new JPanel();
    cp.add(p,BorderLayout.EAST);
    p.setLayout(new GridLayout(1,3,30,10));
    JSlider slider = new JSlider(JSlider.VERTICAL,0,255,100);
    p.add(slider);
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ev) {
        panel.red = ((JSlider)(ev.getSource())).getValue();
        panel.repaint();
      }
    });
    slider = new JSlider(JSlider.VERTICAL,0,255,100);
    p.add(slider);
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ev) {
        panel.green = ((JSlider)(ev.getSource())).getValue();
        panel.repaint();
      }
    });
    slider = new JSlider(JSlider.VERTICAL,0,255,100);
    p.add(slider);
    slider.addChangeListener(new ChangeListener() {
      public void stateChanged(ChangeEvent ev) {
        panel.blue = ((JSlider)(ev.getSource())).getValue();
        panel.repaint();
      }
    });    
  }
}

class ColorPanel extends JPanel{
  int red = 100;
  int green = 100;
  int blue = 100;
  
  public ColorPanel() {
    setPreferredSize(new Dimension(500, 500));
    setBackground(Color.white);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    Shape rc = new Ellipse2D.Double(100, 113, 200, 200);
    Shape gc = new Ellipse2D.Double(50, 200, 200, 200);
    Shape bc = new Ellipse2D.Double(150, 200, 200, 200);
    Area ra = new Area(rc);
    Area ga = new Area(gc);
    Area ba = new Area(bc);
    Area rga = new Area(rc);
    rga.intersect(ga);
    Area gba = new Area(gc);
    gba.intersect(ba);
    Area bra = new Area(bc);
    bra.intersect(ra);
    Area rgba = new Area(rga);
    rgba.intersect(ba);
    ra.subtract(rga);
    ra.subtract(bra);
    ga.subtract(rga);
    ga.subtract(gba);
    ba.subtract(bra);
    ba.subtract(gba);
    // fill the color regions
    g2.setColor(new Color(red,0,0));
    g2.fill(ra);
    g2.setColor(new Color(0,green,0));
    g2.fill(ga);
    g2.setColor(new Color(0,0,blue));
    g2.fill(ba);
    g2.setColor(new Color(red,green,0));
    g2.fill(rga);
    g2.setColor(new Color(0,green,blue));
    g2.fill(gba);
    g2.setColor(new Color(red,0,blue));
    g2.fill(bra);
    g2.setColor(new Color(red,green,blue));
    g2.fill(rgba);
    // draw three circles
    g2.setColor(Color.black);
    g2.draw(rc);
    g2.draw(gc);
    g2.draw(bc);
  }
}
