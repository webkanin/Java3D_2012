
package chapter3;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;

public class TestClip extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Clip Path");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new TestClip();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new ClipPanel();
    getContentPane().add(panel);
  }
}

class ClipPanel extends JPanel {
  public ClipPanel() {
    setPreferredSize(new Dimension(500, 500));
    setBackground(Color.white);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    path.moveTo(100,200);
    path.quadTo(250, 50, 400, 200);
    path.lineTo(400,400);
    path.quadTo(250,250,100,400);
    path.closePath();
    g2.clip(path);
    g2.setColor(new Color(200,200,200));
    g2.fill(path);
    g2.setColor(Color.black);
    g2.setFont(new Font("Serif", Font.BOLD, 60));
    g2.drawString("Clip Path Demo",80,200);
    g2.drawOval(50, 250, 400, 100);
  }
}