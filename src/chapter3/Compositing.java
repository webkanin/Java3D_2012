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

public class Compositing extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Compositing Rules");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Compositing();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new CompositPanel();
    getContentPane().add(panel);
  }
}

class CompositPanel extends JPanel implements MouseListener {
  BufferedImage image;
  int[] rules = {AlphaComposite.CLEAR, AlphaComposite.SRC_OVER,
    AlphaComposite.DST_OVER, AlphaComposite.SRC_IN,
    AlphaComposite.DST_IN, AlphaComposite.SRC_OUT,
    AlphaComposite.DST_OUT, AlphaComposite.SRC,
    AlphaComposite.DST, AlphaComposite.SRC_ATOP,
    AlphaComposite.DST_ATOP, AlphaComposite.XOR};
  int ruleIndex = 0;

  public CompositPanel() {
    setPreferredSize(new Dimension(500, 400));
    setBackground(Color.white);
    URL url = getClass().getClassLoader().getResource("images/earth.jpg");
    try {
      image = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    addMouseListener(this);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    g2.drawImage(image, 100, 100, this);
    AlphaComposite ac = AlphaComposite.getInstance(rules[ruleIndex], 0.4f);
    g2.setComposite(ac);
    Shape ellipse = new Ellipse2D.Double(50, 50, 120, 120);
    g2.setColor(Color.red);
    g2.fill(ellipse);
    g2.setColor(Color.orange);
    Font font = new Font("Serif", Font.BOLD, 144);
    g2.setFont(font);
    g2.drawString("Java", 90, 240);
  }

  public void mouseClicked(MouseEvent e) {
    ruleIndex++;
    ruleIndex %= 12;
    repaint();
  }
  public void mousePressed(MouseEvent e) {
  }
  public void mouseReleased(MouseEvent e) {
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }
}
