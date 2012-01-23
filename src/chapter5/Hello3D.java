package chapter5;

import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Hello3D extends Applet {
  public static void main(String s[]) {
    new MainFrame(new Hello3D(), 640, 480);
  }
  
  public void init() {
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