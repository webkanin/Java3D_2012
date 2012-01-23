package chapter7;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Logo extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Logo(), 640, 480);
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
    
    Transform3D tr = new Transform3D();
    tr.setScale(0.9);
    tr.setRotation(new AxisAngle4d(1, 0, 0, Math.PI/2));
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    //torus
    Shape3D torus = new Torus(0.04, 0.6);
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    torus.setAppearance(ap);
    tg.addChild(torus);
    // shared group of 4 arrows
    SharedGroup sg = new SharedGroup();
    Shape3D arrow;
    Transform3D tra;
    TransformGroup tga;
    for (int i = 0; i < 4; i++) {
      arrow = new Shape3D(new Arrow(), ap);
      tra = new Transform3D();
      tra.setRotation(new AxisAngle4d(0, 0, 1, i*Math.PI/2));
      tga = new TransformGroup(tra);
      sg.addChild(tga);
      tga.addChild(arrow);
    }
    // four links to shared group
    Link link = new Link();
    link.setSharedGroup(sg);
    tr = new Transform3D();
    tr.setScale(0.675);
    tg = new TransformGroup(tr);
    tg.addChild(link);
    spin.addChild(tg);

    link = new Link();
    link.setSharedGroup(sg);
    tr = new Transform3D();
    tr.setScale(0.55);
    tr.setRotation(new AxisAngle4d(0, 0, 1, Math.PI/4));
    tg = new TransformGroup(tr);
    tg.addChild(link);
    spin.addChild(tg);
    
    link = new Link();
    link.setSharedGroup(sg);
    tr = new Transform3D();
    tr.setScale(0.4);
    tr.setRotation(new AxisAngle4d(0, 0, 1, Math.PI/8));
    tg = new TransformGroup(tr);
    tg.addChild(link);
    spin.addChild(tg);
    
    link = new Link();
    link.setSharedGroup(sg);
    tr = new Transform3D();
    tr.setScale(0.4);
    tr.setRotation(new AxisAngle4d(0, 0, 1, 3*Math.PI/8));
    tg = new TransformGroup(tr);
    tg.addChild(link);
    spin.addChild(tg);
    //rotation
    Alpha alpha = new Alpha(-1, 8000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //background and lights
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
    new Point3f(2f,2f,2f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    return root;
  }
}