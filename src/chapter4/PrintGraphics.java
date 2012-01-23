
package chapter4;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.print.*;
import javax.swing.*;

public class PrintGraphics extends JFrame implements ActionListener {
  public static void main(String[] args) {
    JFrame frame = new PrintGraphics();
    frame.setTitle("Printing");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }
  
  PrinterJob pj;
  PrintPanel painter;
  
  public void actionPerformed(ActionEvent e) {
    if (pj.printDialog()) {
      try {
        pj.print();
      } catch (PrinterException ex) {
        ex.printStackTrace();
      }
    }
  }
  
  public PrintGraphics() {
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    JButton button = new JButton("Print");
    cp.add(button, BorderLayout.SOUTH);
    button.addActionListener(this);
    painter = new PrintPanel();
    cp.add(painter, BorderLayout.CENTER);
    pj = PrinterJob.getPrinterJob();
    pj.setPrintable(painter);
  }
}

class PrintPanel extends JPanel implements Printable {
  public PrintPanel() {
    setPreferredSize(new Dimension(800, 400));
    setBackground(Color.white);
  }
  
  public int print(Graphics g, PageFormat pf, int pageIndex) {
    switch (pageIndex) {
      case 0:
        draw(g);
        break;
      case 1:
        g.translate(-(int)pf.getImageableWidth(), 0);
        draw(g);
        break;
      default:
        return NO_SUCH_PAGE;
    }
    return PAGE_EXISTS;
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    draw(g);
  }
  
  private void draw(Graphics g) {
    g.setFont(new Font("Serif", Font.BOLD, 144));
    g.drawString("Welcome!",  200, 300);
  }
}