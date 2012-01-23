package chapter8;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class MultipleViews extends Applet {
  public static void main(String[] args) {
    new MainFrame(new MultipleViews(), 640, 480);
  }

  public void init() {
    // create 4 Canvas3D objects
    this.setLayout(new GridLayout(2,2));
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    // first view: standard
    Canvas3D cv = new Canvas3D(gc);
    add(cv);    
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    // second view: x direction
    cv = new Canvas3D(gc);
    add(cv);
    BranchGroup bgView = createView(cv, new Point3d(2.7,0,0), 
      new Point3d(0,0,0), new Vector3d(0,1,0));
    su.addBranchGraph(bgView);
    // third view: z direction
    cv = new Canvas3D(gc);
    add(cv);
    bgView = createView(cv, new Point3d(0, 0, 2.7),
      new Point3d(0,0,0), new Vector3d(0,1,0));
    su.addBranchGraph(bgView);
    // fourth view: y direction
    cv = new Canvas3D(gc);
    add(cv);
    bgView = createView(cv, new Point3d(0,2.7,0),
      new Point3d(0,0,0), new Vector3d(0,0,1));
    su.addBranchGraph(bgView);
    // content branch
    BranchGroup bg = createSceneGraph();
    bg.compile();
    su.addBranchGraph(bg);
  }

  private BranchGroup createView(Canvas3D cv, Point3d eye,
    Point3d center, Vector3d vup) {
    View view = new View();
    view.setProjectionPolicy(View.PARALLEL_PROJECTION);
    ViewPlatform vp = new ViewPlatform();
    view.addCanvas3D(cv);
    view.attachViewPlatform(vp);
    view.setPhysicalBody(new PhysicalBody());
    view.setPhysicalEnvironment(new PhysicalEnvironment());
    Transform3D trans = new Transform3D();
    trans.lookAt(eye, center, vup);
    trans.invert();
    TransformGroup tg = new TransformGroup(trans);
    tg.addChild(vp);
    BranchGroup bgView = new BranchGroup();
    bgView.addChild(tg);
    return bgView;
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    // object
    Font3D font = new Font3D(new Font("Serif", Font.PLAIN, 1),
      new FontExtrusion());
    Text3D text = new Text3D(font, "Java");    
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Shape3D(text, ap);
    Transform3D tr = new Transform3D();
    tr.setTranslation(new Vector3f(-1f, -0.25f, 0f));
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(shape);
    // rotator
    Alpha alpha = new Alpha(-1, 24000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    // background and light
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