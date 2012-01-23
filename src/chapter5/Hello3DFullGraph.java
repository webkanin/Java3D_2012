package chapter5;

import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Hello3DFullGraph extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Hello3DFullGraph(), 640, 480);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);

    // create superstructure
    VirtualUniverse vu = new VirtualUniverse();
    Locale loc = new Locale(vu);

    // create view branch
    BranchGroup bgView = createViewBranch(cv);
    bgView.compile();
    loc.addBranchGraph(bgView);

    // create content branch
    BranchGroup bg = createContentBranch();
    bg.compile();
    loc.addBranchGraph(bg);
  }

  private BranchGroup createViewBranch(Canvas3D cv) {
    View view = new View();
    view.setProjectionPolicy(View.PERSPECTIVE_PROJECTION);
    ViewPlatform vp = new ViewPlatform();
    view.addCanvas3D(cv);
    view.attachViewPlatform(vp);
    view.setPhysicalBody(new PhysicalBody());
    view.setPhysicalEnvironment(new PhysicalEnvironment());
    Transform3D trans = new Transform3D();
    Point3d eye = new Point3d(0, 0, 1/Math.tan(Math.PI/8));
    Point3d center = new Point3d(0, 0, 0);
    Vector3d vup = new Vector3d(0, 1, 0);
    trans.lookAt(eye, center, vup);
    trans.invert();
    TransformGroup tg = new TransformGroup(trans);
    tg.addChild(vp);
    BranchGroup bgView = new BranchGroup();
    bgView.addChild(tg);
    return bgView;
  }

  private BranchGroup createContentBranch() {
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
    return root;
  }
}