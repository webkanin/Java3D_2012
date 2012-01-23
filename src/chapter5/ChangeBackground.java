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

public class ChangeBackground extends Applet {
  public static void main(String[] args) {
    new MainFrame(new ChangeBackground(), 640, 480);
  }
  
  Background background = null;
  ImageComponent2D image = null;
  
  public void init() {
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    cv.addMouseListener(new MouseAdapter() {
      //change background color and image on mouse click
      public void mouseClicked(MouseEvent ev) {
        if (background.getImage() == null)
          background.setImage(image);
        else {
          background.setImage(null);
          float r = (float)Math.random();
          float g = (float)Math.random();
          float b = (float)Math.random();
          background.setColor(r, g, b);
        }
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
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Font3D font = new Font3D(new Font("SansSerif", Font.PLAIN, 1),
    new FontExtrusion());
    Text3D text = new Text3D(font, "Hello 3D");
    Shape3D shape = new Shape3D(text, ap);
    //transformation
    Transform3D tr = new Transform3D();
    tr.setScale(0.5);
    tr.setTranslation(new Vector3f(-0.95f, -0.2f, 0f));
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(shape);
    //light
    PointLight light = new PointLight(new Color3f(Color.white),
    new Point3f(1f,1f,1f),
    new Point3f(1f,0.1f,0f));
    BoundingSphere bounds = new BoundingSphere();
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    //background
    background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    //load image
    URL url = getClass().getClassLoader().getResource("images/bg.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);    
    //set capacity bit to allow color and image change
    background.setCapability(Background.ALLOW_COLOR_WRITE);
    background.setCapability(Background.ALLOW_IMAGE_READ);
    background.setCapability(Background.ALLOW_IMAGE_WRITE);
    root.addChild(background);
    return root;
  }
}