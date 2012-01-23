package chapter3;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.swing.*;
import java.awt.font.*;
import java.awt.geom.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;

public class TestPaints extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Gradient and Texture Paints");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new TestPaints();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new PaintPanel();
    getContentPane().add(panel);
  }
}

class PaintPanel extends JPanel{
  private BufferedImage image;

  public PaintPanel() {
    setPreferredSize(new Dimension(500, 500));
    setBackground(Color.white);
    URL url = getClass().getClassLoader().getResource("images/earth.jpg");
    try {
      image = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    GradientPaint gp = new GradientPaint(100,50, Color.white, 150, 50, Color.gray, true);
    g2.setPaint(gp);
    g2.fillRect(100, 40, 300, 20);
    TexturePaint tp = new TexturePaint(image, 
      new Rectangle2D.Double(100, 100, image.getWidth(), image.getHeight()));
    g2.setPaint(tp);
    Shape ellipse = new Ellipse2D.Double(100, 100, 300, 200);
    g2.fill(ellipse);
    GradientPaint paint = new GradientPaint(100,300, Color.white, 400, 400, Color.black);
    g2.setPaint(paint);
    Font font = new Font("Serif", Font.BOLD, 144);
    g2.setFont(font);
    g2.drawString("Java", 100, 400);
  }
}
