package chapter8;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import chapter6.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class RotateView extends Applet {
  public static void main(String[] args) {
    new MainFrame(new RotateView(), 640, 480);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    SimpleUniverse su = new SimpleUniverse(cv, 2);
    su.getViewingPlatform().setNominalViewingTransform();
    BranchGroup bg = createSceneGraph(su.getViewingPlatform().
      getMultiTransformGroup().getTransformGroup(0));
    bg.compile();
    su.addBranchGraph(bg);
  }

  private BranchGroup createSceneGraph(TransformGroup vtg) {
    BranchGroup root = new BranchGroup();
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(ap);
    Transform3D tr = new Transform3D();
    tr.setScale(0.25);
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(shape);
    //view rotator
    Alpha alpha = new Alpha(-1, 4000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, vtg);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    root.addChild(rotator);
    //lights
    Background background = new Background(1.0f, 1.0f, 1.0f);
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
}