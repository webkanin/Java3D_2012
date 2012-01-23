package chapter9;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Lighting extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Lighting(), 640, 480);
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
    Vector3f pos = new Vector3f(0.5f,0f,0f);
    Transform3D rotation = new Transform3D();
    rotation.rotZ(Math.PI/4);
    for (int i = 0; i < 8; i++) {
      Node shape = createShape(pos, 0.2f, .5f, (float)Math.pow(1.7,i)); 
      root.addChild(shape);
      rotation.transform(pos);
    }
    //lights
    BoundingSphere bounds = new BoundingSphere();
    // background
//    Background background = new Background(0.2f, 0.2f, 0.2f);
//    background.setApplicationBounds(bounds);
//    root.addChild(background);
    PointLight pLight = new PointLight(new Color3f(Color.white),
    new Point3f(0f,0f,1f/(float)Math.tan(Math.PI/8)), new Point3f(1f,0f,0f));
    pLight.setInfluencingBounds(bounds);
    root.addChild(pLight);
    return root;
  }
  
  private Node createShape(Vector3f pos, float size, float spec, float shine) {
    Material mat = new Material();
    mat.setDiffuseColor(0.5f,0.5f,1f);
    mat.setSpecularColor(spec, spec, spec);
    mat.setShininess(shine);
    Appearance ap = new Appearance();
    ap.setMaterial(mat);
    Sphere shape = new Sphere(size, Sphere.GENERATE_NORMALS, 50, ap);
    Transform3D tr = new Transform3D();
    tr.setTranslation(pos);
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(shape);
    return tg;
  }
}
