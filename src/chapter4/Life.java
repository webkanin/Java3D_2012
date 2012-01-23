
package chapter4;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class Life extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Game of Life");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Life();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new LifePanel();
    getContentPane().add(panel);
  }
}

class LifePanel extends JPanel implements ActionListener{
  int n = 30;
  boolean[][] cells1;
  boolean[][] cells2;
  
  public LifePanel() {
    setPreferredSize(new Dimension(400, 400));
    setBackground(Color.white);
    cells1 = new boolean[n][n];
    cells2 = new boolean[n][n];
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        cells1[i][j] = Math.random() < 0.1;
        cells2[i][j] = false;
      }
    }
    Timer timer = new Timer(1000, this);
    timer.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    
    g2.setColor(Color.lightGray);
    int p = 0;
    int c = 16;
    int len = c*n;
    for (int i = 0; i <= n; i++) {
      g2.drawLine(0, p, len, p);
      g2.drawLine(p, 0, p, len);
      p += c;
    }
    g2.setColor(Color.black);
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (cells1[i][j]) {
          int x = i*c;
          int y = j*c;
          g2.fillOval(x, y, c, c);
        }
      }
    }
  }
  
  public void actionPerformed(ActionEvent e) {
    boolean[][] cells = cells1;
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        cells2[i][j] = cells[i][j];
        int nb = neighbors(cells, i, j);
        if (nb == 3)
          cells2[i][j] = true;
        if (nb < 2 || nb > 3)
          cells2[i][j] = false;
      }
    }
    
    cells1 = cells2;
    cells2 = cells;
    repaint();
  }
  
  private int neighbors(boolean[][] cells, int x, int y) {
    int x1 = (x>0)?x-1:x;
    int x2 = (x<n-1)?x+1:x;
    int y1 = (y>0)?y-1:y;
    int y2 = (y<n-1)?y+1:y;
    int count = 0;
    for (int i = x1; i <= x2; i++) {
      for (int j = y1; j <= y2; j++) {
        count += (cells[i][j])?1:0;
      }
    }
    if (cells[x][y]) count--;
    return count;
  }
}
