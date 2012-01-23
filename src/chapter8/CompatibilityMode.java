package chapter8;

import javax.vecmath.*;
import java.awt.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import chapter6.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class CompatibilityMode extends Applet {
  public static void main(String[] args) {
    new MainFrame(new CompatibilityMode(), 640, 650);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    VirtualUniverse universe = new VirtualUniverse();
    Locale locale = new Locale(universe);
    BranchGroup bg = createView(cv);
    locale.addBranchGraph(bg);
    bg = createContent();
    bg.compile();
    locale.addBranchGraph(bg);
  }

  private BranchGroup createView(Canvas3D cv) {
    BranchGroup bg = new BranchGroup();
    ViewPlatform platform = new ViewPlatform();
    bg.addChild(platform);    
    View view = new View();
    view.addCanvas3D(cv);
    view.setCompatibilityModeEnable(true);
    view.attachViewPlatform(platform);
    Transform3D projection = new Transform3D();
    projection.frustum(-0.1, 0.1, -0.1, 0.1, 0.2, 10);
    view.setLeftProjection(projection);
    Transform3D viewing = new Transform3D();
    Point3d eye = new Point3d(0,0,1);
    Point3d look = new Point3d(0,0,-1);
    Vector3d up = new Vector3d(0,1,0);
    viewing.lookAt(eye, look, up);
    view.setVpcToEc(viewing);
    PhysicalBody body = new PhysicalBody();
    view.setPhysicalBody(body);
    PhysicalEnvironment env = new PhysicalEnvironment();
    view.setPhysicalEnvironment(env);
    return bg;
  }
  
  private BranchGroup createContent() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Shape3D(new Tetrahedron(), ap);
    //rotating object
    Transform3D tr = new Transform3D();
    tr.setScale(0.25);
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(shape);
    Alpha alpha = new Alpha(-1, 4000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //light and background
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