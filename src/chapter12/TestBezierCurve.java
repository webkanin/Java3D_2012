package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestBezierCurve extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestBezierCurve(), 640, 480);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }

  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    // object
    Point3d p0 = new Point3d(-1,0,0.5);
    Point3d p1 = new Point3d(-0.2,0.6,-0.2);
    Point3d p2 = new Point3d(0.3,-0.8,0.3);
    Point3d p3 = new Point3d(0.9,0.1,0.6);
    Appearance ap = new Appearance();
    ap.setColoringAttributes(new ColoringAttributes(0f, 0f, 0f, 
      ColoringAttributes.FASTEST));
    Shape3D shape = new Shape3D(new BezierCurve(p0,p1,p2,p3), ap);
    spin.addChild(shape);
    // rotation interpolator
    Alpha alpha = new Alpha(-1, 10000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    // background
    Background background = new Background(1f, 1f, 1f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }
}

