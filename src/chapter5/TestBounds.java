package chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.awt.image.*;
import java.io.*;
import java.net.URL;
import javax.imageio.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestBounds extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestBounds(), 640, 480);
  }

  Light light = null;
  Bounds[] bounds = new Bounds[3];
  int bIndex = 0;
  
  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    cv.addMouseListener(new MouseAdapter() {
      //change background color and image on mouse click
      public void mouseClicked(MouseEvent ev) {
        bIndex = (bIndex+1) % 3;
        System.out.println(bIndex);
        light.setInfluencingBounds(bounds[bIndex]);
      }
    });
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    // first sphere
    Sphere sphere = new Sphere();
    Transform3D tr = new Transform3D();
    tr.setScale(0.1);
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(sphere);
    // second sphere
    sphere = new Sphere();
    tr.setTranslation(new Vector3f(-0.4f, 0f, 0f));
    tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(sphere);
    // third sphere
    sphere = new Sphere();
    tr.setTranslation(new Vector3f(-0.8f, 0f, 0f));
    tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(sphere);
    //light
    light = new PointLight(new Color3f(Color.white),
    new Point3f(1f,1f,1f),
    new Point3f(1f,0f,0f));
    light.setCapability(Light.ALLOW_INFLUENCING_BOUNDS_WRITE);
    //bounds
    bounds[0] = new BoundingSphere(new Point3d(0,0,0), 1);
    bounds[1] = new BoundingSphere(new Point3d(0,0,0), 0.6);
    bounds[2] = new BoundingSphere(new Point3d(0,0,0), 0.2);
    light.setInfluencingBounds(bounds[0]);
    root.addChild(light);
    //background
    URL url = getClass().getClassLoader().getResource("images/bg.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);    
    Background background = new Background(image);
    background.setApplicationBounds(bounds[0]);
    root.addChild(background);
    return root;
  }
}  
