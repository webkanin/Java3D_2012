package chapter4;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.color.*;
import java.awt.geom.*;
import java.io.*;
import javax.swing.*;
import javax.imageio.*;

public class ImageProcessing extends JFrame implements ActionListener {
  public static void main(String[] args) {
    JFrame frame = new ImageProcessing();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);
  }

  ImagePanel imageSrc, imageDst;
  JFileChooser fc = new JFileChooser();

  public ImageProcessing() {
    JMenuBar mb = new JMenuBar();
    setJMenuBar(mb);

    JMenu menu = new JMenu("File");
    JMenuItem mi = new JMenuItem("Open image");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Open image (awt)");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Save image");
    mi.addActionListener(this);
    menu.add(mi);
    menu.addSeparator();
    mi = new JMenuItem("Exit");
    mi.addActionListener(this);
    menu.add(mi);
    mb.add(menu);

    menu = new JMenu("Process");
    mi = new JMenuItem("Copy");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Smooth");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Sharpen");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Edge");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Rescale");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Rotate");
    mi.addActionListener(this);
    menu.add(mi);
    mi = new JMenuItem("Gray scale");
    mi.addActionListener(this);
    menu.add(mi);
    mb.add(menu);

    Container cp = this.getContentPane();
    cp.setLayout(new FlowLayout());
    imageSrc = new ImagePanel();
    imageDst = new ImagePanel();
    cp.add(imageSrc);
    cp.add(imageDst);
  }

  public void actionPerformed(ActionEvent ev) {
    String cmd = ev.getActionCommand();
    if ("Open image".equals(cmd)) {
      int retval = fc.showOpenDialog(this);
      if (retval == JFileChooser.APPROVE_OPTION) {
        try {
          BufferedImage bi = ImageIO.read(fc.getSelectedFile());
          imageSrc.setImage(bi);
          pack();
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    } else if ("Open image (awt)".equals(cmd)) {
      int retval = fc.showOpenDialog(this);
      if (retval == JFileChooser.APPROVE_OPTION) {
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage(fc.getSelectedFile().getPath());
        MediaTracker tracker = new MediaTracker(new Component() {});
        tracker.addImage(img, 0);
        try {
          tracker.waitForID(0);
        } catch (InterruptedException ex) {}
        BufferedImage bi = new BufferedImage(img.getWidth(this), img.getHeight(this), BufferedImage.TYPE_INT_RGB);
        bi.getGraphics().drawImage(img, 0, 0, this);
        imageSrc.setImage(bi);
      }
    } else if ("Save image".equals(cmd)) {
      int retval = fc.showSaveDialog(this);
      if (retval == JFileChooser.APPROVE_OPTION) {
        try{
          ImageIO.write(imageDst.getImage(), "png", fc.getSelectedFile());
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      }
    } else if ("Exit".equals(cmd)) {
      System.exit(0);
    } else if ("Copy".equals(cmd)) {
      imageSrc.setImage(imageDst.getImage());
    } else {
      process(cmd);
    }
  }

  void process(String opName) {
    BufferedImageOp op = null;
    if (opName.equals("Smooth")) {
      float[] data = new float[9];
      for (int i = 0; i < 9; i++) data[i] = 1.0f/9.0f;
      Kernel ker = new Kernel(3,3,data);
      op = new ConvolveOp(ker);
    } else if (opName.equals("Sharpen")) {
      float[] data = {0f, -1f, 0f, -1f, 5f, -1f, 0f, -1f, 0f};
      Kernel ker = new Kernel(3,3,data);
      op = new ConvolveOp(ker);
    } else if (opName.equals("Edge")) {
      float[] data = {0f, -1f, 0f, -1f, 4f, -1f, 0f, -1f, 0f};
      Kernel ker = new Kernel(3,3,data);
      op = new ConvolveOp(ker);
    } else if (opName.equals("Rescale")) {
      op = new RescaleOp(1.5f, 0.0f, null);
    } else if (opName.equals("Gray scale")) {
      op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
    } else if (opName.equals("Rotate")) {
      AffineTransform xform = new AffineTransform();
      xform.setToRotation(Math.PI/6);
      op = new AffineTransformOp(xform, AffineTransformOp.TYPE_BILINEAR);
    }
    BufferedImage bi = op.filter(imageSrc.getImage(), null);
    imageDst.setImage(bi);
    pack();
  }
}

class ImagePanel extends JPanel {
  BufferedImage image = null;

  public ImagePanel() {
    image = null;
    setPreferredSize(new Dimension(256, 256));
  }

  public ImagePanel(BufferedImage bi) {
    image = bi;
  }

  public void paintComponent(Graphics g) {
    Graphics2D g2 = (Graphics2D)g;
    if (image != null)
      g2.drawImage(image, 0, 0, this);
    else
      g2.drawRect(0, 0, getWidth()-1, getHeight()-1);
  }

  public BufferedImage getImage() {
    return image;
  }

  public void setImage(BufferedImage bi) {
    image = bi;
    setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
    invalidate();
    repaint();
  }
}