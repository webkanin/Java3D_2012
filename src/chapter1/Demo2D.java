package chapter1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.font.*;
import java.awt.geom.*;

public class Demo2D extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Java 2D Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Demo2D();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new Panel2D();
    getContentPane().add(panel);
  }
}

class Panel2D extends JPanel{
  public Panel2D() {
    setPreferredSize(new Dimension(500, 400));
    setBackground(Color.white);
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    // draw an ellipse
    Shape ellipse = new Ellipse2D.Double(150, 100, 200, 200);
    GradientPaint paint = new GradientPaint(100,100, Color.white, 400, 400, Color.gray);
    g2.setPaint(paint);
    g2.fill(ellipse);
    // set transparency
    AlphaComposite ac = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);
    g2.setComposite(ac);
    g2.setColor(Color.blue);
    // draw transparent text
    Font font = new Font("Serif", Font.BOLD, 120);
    g2.setFont(font);
    g2.drawString("Java", 120, 200);
    // get outline of text glyph
    FontRenderContext frc = g2.getFontRenderContext();
    GlyphVector gv = font.createGlyphVector(frc, "2D");
    Shape glyph = gv.getOutline(150,300);
    // draw rotated glyph
    g2.rotate(Math.PI/6, 200, 300);
    g2.fill(glyph);
  }
}
