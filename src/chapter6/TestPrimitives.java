package chapter6;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestPrimitives extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestPrimitives(), 640, 480);
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

    //primitives
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Box box = new Box(1.2f, 0.3f, 0.8f, ap);
    Sphere sphere = new Sphere();
    Cylinder cylinder = new Cylinder();
    Cone cone = new Cone();

    Transform3D tr = new Transform3D();
    tr.setScale(0.2);
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(box);
    tr.setIdentity();
    tr.setTranslation(new Vector3f(0f,1.5f,0f));
    TransformGroup tgSphere = new TransformGroup(tr);
    tg.addChild(tgSphere);
    tgSphere.addChild(sphere);
    tr.setTranslation(new Vector3f(-1f,-1.5f,0f));
    TransformGroup tgCylinder = new TransformGroup(tr);
    tg.addChild(tgCylinder);
    tgCylinder.addChild(cylinder);
    tr.setTranslation(new Vector3f(1f,-1.5f,0f));
    TransformGroup tgCone = new TransformGroup(tr);
    tg.addChild(tgCone);
    tgCone.addChild(cone);

    Alpha alpha = new Alpha(-1, 4000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);

    //background and light
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