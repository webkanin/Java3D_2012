package chapter10;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Clock extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Clock(), 640, 480);
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
    // clock face
    Appearance apFace = new Appearance();
    Material matFace = new Material();
    matFace.setAmbientColor(new Color3f(0f,0f,0f));
    matFace.setDiffuseColor(new Color3f(0.15f,0.15f,0.25f));
    apFace.setMaterial(matFace);
    Cylinder face = new Cylinder(0.6f, 0.01f, Cylinder.GENERATE_NORMALS, 50, 2, apFace);
    Transform3D tr = new Transform3D();
    tr.rotX(Math.PI/2);
    tr.setTranslation(new Vector3d(0,0,-0.01));
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(face);
    root.addChild(tg);
    // hour
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shapeHour = new Shape3D(createGeometry(0.4, 0.02, 0.02), ap);
    TransformGroup spinHour = new TransformGroup();
    spinHour.addChild(shapeHour);
    spinHour.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spinHour);
    // minute
    Shape3D shapeMin = new Shape3D(createGeometry(0.5, 0.02, 0.02), ap);
    TransformGroup spinMin = new TransformGroup();
    spinMin.addChild(shapeMin);
    spinMin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spinMin);
    // second
    Shape3D shapeSec = new Shape3D(createGeometry(0.5, 0.01, 0.01), ap);
    TransformGroup spinSec = new TransformGroup();
    spinSec.addChild(shapeSec);
    spinSec.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spinSec);
    // Behavior node
    ClockBehavior rotator = new ClockBehavior(spinHour, spinMin, spinSec);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    root.addChild(rotator);
    //light
    AmbientLight light = new AmbientLight(true, new Color3f(Color.blue));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white), new Point3f(0.7f,0.7f,2f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    //background
    Background background = new Background(0.7f, 0.7f, 0.7f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }
  
  GeometryArray createGeometry(double l, double w, double h) {
    GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
    Point3d[] pts = new Point3d[4];
    pts[0] = new Point3d(0, 0, h);
    pts[1] = new Point3d(-w, 0, 0);
    pts[2] = new Point3d(w, 0, 0);
    pts[3] = new Point3d(0, l, 0);
    gi.setCoordinates(pts);
    int[] indices = {0,1,2,0,3,1,0,2,3,2,1,3};
    gi.setCoordinateIndices(indices);
    NormalGenerator ng = new NormalGenerator();
    ng.generateNormals(gi);
    return gi.getGeometryArray();
  }
}
