package chapter3;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.font.*;
import java.awt.geom.*;

public class GlyphClip extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Glyph and Clip");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new GlyphClip();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new GlyphPanel();
    getContentPane().add(panel);
  }
}

class GlyphPanel extends JPanel {
  public GlyphPanel() {
    setPreferredSize(new Dimension(500, 400));
    setBackground(Color.white);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    Font font = new Font("Serif", Font.BOLD, 144);
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, "Java");
    Shape glyph = gv.getOutline(100,200);
    g2.setClip(glyph);
    g2.setColor(Color.red);
    for (int i = 0; i < 2000; i++) {
      Shape shape = new Ellipse2D.Double(Math.random()*500, Math.random()*400, 30, 20);
      g2.draw(shape);
    }
  }
}
