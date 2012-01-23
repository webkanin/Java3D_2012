
package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import chapter6.Dodecahedron;

public class OffScreen extends Frame{
  public static void main(String[] args) {
    Frame frame = new OffScreen();
    frame.setTitle("Off Screen Rendering");
    frame.setSize(640, 480);
    frame.setVisible(true);
  }
  
  private Canvas3D cv;
  private Canvas3D offScreenCanvas;
  private View view;
  
  public OffScreen() {
    WindowListener l = new WindowAdapter() {
      public void windowClosing(WindowEvent ev) {
        System.exit(0);
      }
    };
    this.addWindowListener(l);
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
    // create off screen canvas
    view = su.getViewer().getView();
    offScreenCanvas = new Canvas3D(gc, true);
    Screen3D sOn = cv.getScreen3D();
    Screen3D sOff = offScreenCanvas.getScreen3D();
    Dimension dim = sOn.getSize();
    sOff.setSize(dim);
    sOff.setPhysicalScreenWidth(sOn.getPhysicalScreenWidth());
    sOff.setPhysicalScreenHeight(sOn.getPhysicalScreenHeight());
    Point loc = cv.getLocationOnScreen();
    offScreenCanvas.setOffScreenLocation(loc);
    // button
    Button button = new Button("Save image");
    add(button, BorderLayout.SOUTH);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        BufferedImage bi = capture();
        save(bi);
      }
    });
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(ap);
    Transform3D tr = new Transform3D();
    tr.setScale(0.25);
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(shape);
    Alpha alpha = new Alpha(-1, 10000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //background and light
    URL url = getClass().getClassLoader().getResource("images/bg.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
    Background background = new Background(image);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.green),
    new Point3f(3f,3f,3f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    PointLight ptlight2 = new PointLight(new Color3f(Color.orange),
    new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
    ptlight2.setInfluencingBounds(bounds);
    root.addChild(ptlight2);
    return root;
  }
  
  public BufferedImage capture() {
    // render off screen image
    Dimension dim = cv.getSize();
    view.stopView();
    view.addCanvas3D(offScreenCanvas);
    BufferedImage bImage =
    new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_RGB);
    ImageComponent2D buffer =
    new ImageComponent2D(ImageComponent.FORMAT_RGB, bImage);
    offScreenCanvas.setOffScreenBuffer(buffer);
    view.startView();
    offScreenCanvas.renderOffScreenBuffer();
    offScreenCanvas.waitForOffScreenRendering();
    bImage = offScreenCanvas.getOffScreenBuffer().getImage();
    view.removeCanvas3D(offScreenCanvas);
    return bImage;
  }
  
  public void save(BufferedImage bImage) {
    // save image to file
    JFileChooser chooser = new JFileChooser();
    chooser.setCurrentDirectory(new File("."));
    if (chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
      File oFile = chooser.getSelectedFile();
      try {
        ImageIO.write(bImage, "jpeg", oFile);
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
