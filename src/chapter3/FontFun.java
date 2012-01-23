
package chapter3;

import java.awt.*;
import javax.swing.*;
import java.awt.geom.*;
import java.awt.font.*;

public class FontFun extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Fonts");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new FontFun();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new FontPanel();
    getContentPane().add(panel);
  }
}

class FontPanel extends JPanel {
  public FontPanel() {
    setPreferredSize(new Dimension(640, 480));
    setBackground(Color.white);
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    Font font = new Font("Serif", Font.BOLD, 36);
    AffineTransform tx = new AffineTransform();
    tx.shear(0.5, 0);
    g2.setFont(font.deriveFont(tx));
    g2.drawString("Derived font", 100, 100);
    
    g2.setFont(font);
    FontRenderContext frc = g2.getFontRenderContext();
    String str = "String bounds";
    Rectangle2D bounds = font.getStringBounds(str, frc);
    g2.translate(100, 200);
    g2.draw(bounds);
    g2.drawString(str, 0, 0);
    
    str = "Baseline, ascent, descent, leading";
    g2.translate(0,100);
    int w = (int)font.getStringBounds(str, frc).getWidth();
    LineMetrics lm = font.getLineMetrics(str, frc);
    g2.drawLine(0, 0, w, 0);
    int y = -(int)lm.getAscent();
    g2.drawLine(0, y, w, y);
    y = (int)lm.getDescent();
    g2.drawLine(0, y, w, y);
    y = (int)(lm.getDescent()+lm.getLeading());
    g2.drawLine(0, y, w, y);
    
    g2.drawString(str,0,0);
  }
}
