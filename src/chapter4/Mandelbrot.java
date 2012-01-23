package chapter4;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class Mandelbrot extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Mandelbrot set");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new Mandelbrot();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new MandelbrotPanel();
    getContentPane().add(panel);
  }
}

class MandelbrotPanel extends JPanel{
  BufferedImage bi;

  public MandelbrotPanel() {
    int w = 500;
    int h = 500;
    setPreferredSize(new Dimension(w, h));
    setBackground(Color.white);
    bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
    WritableRaster raster = bi.getRaster();
    int[] rgb = new int[3];
    float xmin = -2;
    float ymin = -2;
    float xscale = 4f/w;
    float yscale = 4f/h;
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        float cr = xmin + j * xscale;
        float ci = ymin + i * yscale;
        int count = iterCount(cr, ci);
        rgb[0] = (count & 0x07) << 5;
        rgb[1] = ((count >> 3) & 0x07) << 5;
        rgb[2] = ((count >> 6) & 0x07) << 5;
        raster.setPixel(j, i, rgb);
      }
    }
  }

  private int iterCount(float cr, float ci) {
    int max = 512;
    float zr = 0;
    float zi = 0;
    float lengthsq = 0;
    int count = 0;
    while ((lengthsq < 4.0) && (count < max)) {
      float temp = zr * zr - zi * zi + cr;
      zi = 2 * zr * zi + ci;
      zr = temp;
      lengthsq = zr * zr + zi * zi;
      count++;
    }
    return max-count;
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(bi, 0, 0, this);
  }
}