
package chapter4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class TestHeart extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Heart");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new TestHeart();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new JPanel() {
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Heart h = new Heart(0,0,500,500);
        g.setColor(Color.red);
        ((Graphics2D)g).fill(h);
      }
    };
    panel.setBackground(Color.white);
    panel.setPreferredSize(new Dimension(500,500));
    getContentPane().add(panel);
  }
}
