
package chapter11;

import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.behaviors.vp.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
import java.net.*;
import com.sun.j3d.utils.image.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestLOD extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestLOD(), 480, 480);
  }

  BufferedImage[] images = new BufferedImage[3];
  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    ViewingPlatform viewingPlatform = su.getViewingPlatform();
    viewingPlatform.setNominalViewingTransform();
    // orbit behavior to zoom and rotate the view
    OrbitBehavior orbit = new OrbitBehavior(cv,
      OrbitBehavior.REVERSE_ZOOM |
      OrbitBehavior.REVERSE_ROTATE |
      OrbitBehavior.DISABLE_TRANSLATE);
    BoundingSphere bounds =
      new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
    orbit.setSchedulingBounds(bounds);
    viewingPlatform.setViewPlatformBehavior(orbit);
    su.addBranchGraph(bg);
  }
  
  public BranchGroup createSceneGraph() {
    BranchGroup objRoot = new BranchGroup();    
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objRoot.addChild(objTrans); 
    // a switch to hold the different levels
    Switch sw = new Switch(0);
    sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_READ);
    sw.setCapability(javax.media.j3d.Switch.ALLOW_SWITCH_WRITE);
    objTrans.addChild(sw);
    // 4 levels of globes
    loadImages();
    Appearance ap = createAppearance(0);
    sw.addChild(new Sphere(0.4f, Primitive.GENERATE_TEXTURE_COORDS, 40, ap));
    ap = createAppearance(1);
    sw.addChild(new Sphere(0.4f, Primitive.GENERATE_TEXTURE_COORDS, 20, ap));
    ap = createAppearance(2);
    sw.addChild(new Sphere(0.4f, Primitive.GENERATE_TEXTURE_COORDS, 10, ap));
    ap = new Appearance();
    ap.setColoringAttributes(new ColoringAttributes(0f,0f,0.5f,ColoringAttributes.FASTEST));
    sw.addChild(new Sphere(0.4f, Sphere.GENERATE_NORMALS, 5, ap));
    // the DistanceLOD behavior
    float[] distances = new float[3];
    distances[0] = 5.0f;
    distances[1] = 10.0f;
    distances[2] = 25.0f;
    DistanceLOD lod = new DistanceLOD(distances);
    lod.addSwitch(sw);
    BoundingSphere bounds =
    new BoundingSphere(new Point3d(0.0,0.0,0.0), 10.0);
    lod.setSchedulingBounds(bounds);
    objTrans.addChild(lod);
    //background
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    objRoot.addChild(background);
    return objRoot;
  }
    
  void loadImages() {
    URL filename =
    getClass().getClassLoader().getResource("images/earth.jpg");
    try {
      images[0] = ImageIO.read(filename);
      AffineTransform xform = AffineTransform.getScaleInstance(0.5, 0.5);
      AffineTransformOp scaleOp = new AffineTransformOp(xform, null);
      for (int i = 1; i < 3; i++) {
          images[i] = scaleOp.filter(images[i-1], null);
      }
    } catch (IOException ex) {
        ex.printStackTrace();
    }
  }
  
  Appearance createAppearance(int i){
    Appearance appear = new Appearance();
      ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, images[i]);
      Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
      image.getWidth(), image.getHeight());
      texture.setImage(0, image);
      texture.setEnable(true);
      texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
      texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
      appear.setTexture(texture);
    return appear;
  }
}
