package chapter7;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Mirror extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Mirror(), 640, 480);
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
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Font3D font = new Font3D(new Font("Serif", Font.PLAIN, 1),
      new FontExtrusion());
    Shape3D shape = new Shape3D(new Text3D(font, "Java"), ap);
    //translation
    Transform3D trans = new Transform3D();
    trans.setTranslation(new Vector3d(-0.5,0,0));
    TransformGroup transg = new TransformGroup(trans);
    transg.addChild(shape);
    //rotation
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    spin.addChild(transg);
    //scaling, translation
    Transform3D tr = new Transform3D();
    tr.setScale(0.25);
    tr.setTranslation(new Vector3d(0.5,0,0));
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(spin);
    //shared group
    SharedGroup share = new SharedGroup();
    share.addChild(tg);
    //link leaf nodes to shared group
    Link link1 = new Link(share);
    Link link2 = new Link(share);
    //reflection
    Transform3D reflection = getReflection(1,1,1);
    TransformGroup reflectionGroup = new TransformGroup(reflection);
    reflectionGroup.addChild(link2);
    //the mirror
    QuadArray qa = new QuadArray(4, QuadArray.COORDINATES);
    qa.setCoordinate(0, new Point3d(0,-0.5,0.5));
    qa.setCoordinate(1, new Point3d(1,-0.5,-0.5));
    qa.setCoordinate(2, new Point3d(0,0.5,-0.5));
    qa.setCoordinate(3, new Point3d(-1,0.5,0.5));
    ap = new Appearance();
    ap.setTransparencyAttributes(
      new TransparencyAttributes(TransparencyAttributes.BLENDED, 0.7f));
    Shape3D mirror = new Shape3D(qa, ap);
    //rotator
    Alpha alpha = new Alpha(-1, 4000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    //background and lights
    Background background = new Background(0.5f, 0.5f, 0.5f);
    background.setApplicationBounds(bounds);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    PointLight ptlight = new PointLight(new Color3f(Color.green),
      new Point3f(3f,3f,3f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    PointLight ptlight2 = new PointLight(new Color3f(Color.orange),
      new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
    ptlight2.setInfluencingBounds(bounds);
    //branch group
    BranchGroup root = new BranchGroup();
    root.addChild(link1);
    root.addChild(reflectionGroup);
    root.addChild(mirror);
    root.addChild(rotator);
    root.addChild(background);
    root.addChild(light);
    root.addChild(ptlight);
    root.addChild(ptlight2);
    return root;
  }
  
  static Transform3D getReflection(double a, double b, double c) {
    Transform3D transform = new Transform3D();
    double theta = Math.acos(c/Math.sqrt(a*a+b*b+c*c));
    double r = Math.sqrt(a*a+b*b);
    Transform3D rot = new Transform3D();
    rot.set(new AxisAngle4d(b/r, -a/r, 0, theta));
    Transform3D ref = new Transform3D();
    ref.setScale(new Vector3d(1,1,-1));
    transform.mulInverse(rot);
    transform.mul(ref);
    transform.mul(rot);
    return transform;
  }
}