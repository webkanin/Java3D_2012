package chapter9;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import chapter6.Dodecahedron;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestFog extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestFog(), 640, 480);
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
    //object
    for (int i = 0; i < 5; i++) {
      for (int j = 0; j < 5; j++) {
        Vector3f pos = new Vector3f(-0.8f+0.4f*i,-0.2f+0.2f*j,-0.4f*j);
        Node shape = createShape(pos);
        root.addChild(shape);
      }
    }
    //lights
    BoundingSphere bounds = new BoundingSphere(new Point3d(), Double.MAX_VALUE);
    Background background = new Background(.6f, .6f, .6f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    DirectionalLight dLight = new DirectionalLight(new Color3f(Color.white), new Vector3f(1f,0f,-1f));
    dLight.setInfluencingBounds(bounds);
    root.addChild(dLight);
    //fog
    LinearFog fog = new LinearFog(.6f, .6f, .6f, 2f, 4f);
    fog.setInfluencingBounds(bounds);
    root.addChild(fog);
    return root;
  }

  private Node createShape(Vector3f pos) {
    Material mat = new Material();
    Appearance ap = new Appearance();
    ap.setMaterial(mat);
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(ap);
    Transform3D tr = new Transform3D();
    tr.setScale(0.1);
    tr.setTranslation(pos);
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(shape);
    return tg;
  }
}

