package chapter8;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.picking.*;
import chapter6.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Picking extends Applet implements MouseListener{
  public static void main(String[] args) {
    new MainFrame(new Picking(), 640, 480);
  }

  PickCanvas pc;
  Appearance lit = new Appearance();

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    cv.addMouseListener(this);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    pc = new PickCanvas(cv, bg);
    pc.setMode(PickTool.GEOMETRY);
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }

  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    //appearance
    Appearance wireframe = new Appearance();
    wireframe.setPolygonAttributes(new PolygonAttributes(
      PolygonAttributes.POLYGON_LINE, PolygonAttributes.CULL_BACK, 0f));
    wireframe.setColoringAttributes(new ColoringAttributes(
      0f, 0f, 0f, ColoringAttributes.SHADE_FLAT));
    lit.setMaterial(new Material());
    // objects
    Box box = new Box(1.2f, 0.3f, 0.8f, Primitive.ENABLE_GEOMETRY_PICKING |
      Primitive.ENABLE_APPEARANCE_MODIFY |
      Primitive.GENERATE_NORMALS, wireframe);
    Sphere sphere = new Sphere(1f, Primitive.ENABLE_GEOMETRY_PICKING |
      Primitive.ENABLE_APPEARANCE_MODIFY |
      Primitive.GENERATE_NORMALS, wireframe);
    Cylinder cylinder = new Cylinder(1.0f, 2.0f,
      Primitive.ENABLE_GEOMETRY_PICKING | Primitive.ENABLE_APPEARANCE_MODIFY |
      Primitive.GENERATE_NORMALS, wireframe);
    Cone cone = new Cone(1.0f, 2.0f, Primitive.ENABLE_GEOMETRY_PICKING |
      Primitive.ENABLE_APPEARANCE_MODIFY |
      Primitive.GENERATE_NORMALS, wireframe);
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
    Shape3D tetra = new Shape3D(new Tetrahedron(), wireframe);
    PickTool.setCapabilities(tetra, PickTool.INTERSECT_TEST);
    tetra.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    tr = new Transform3D();
    tr.setScale(0.12);
    tr.setTranslation(new Vector3f(0f, 0f, -0.4f));
    tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(tetra);
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(wireframe);
    PickTool.setCapabilities(shape, PickTool.INTERSECT_TEST);
    shape.setCapability(Shape3D.ALLOW_APPEARANCE_WRITE);
    tr = new Transform3D();
    tr.setScale(0.12);
    tr.setTranslation(new Vector3f(0f, 0f, 0.4f));
    tg = new TransformGroup(tr);
    spin.addChild(tg);
    tg.addChild(shape);
    // rotation
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

  public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
    pc.setShapeLocation(mouseEvent);
    PickResult[] results = pc.pickAll();
    for (int i = 0; (results != null) && (i < results.length); i++) {
      Node node = results[i].getObject();
      if (node instanceof Shape3D) {
        ((Shape3D)node).setAppearance(lit);
        System.out.println(node.toString());
      }
    }
  }

  public void mouseEntered(java.awt.event.MouseEvent mouseEvent) {
  }

  public void mouseExited(java.awt.event.MouseEvent mouseEvent) {
  }

  public void mousePressed(java.awt.event.MouseEvent mouseEvent) {
  }

  public void mouseReleased(java.awt.event.MouseEvent mouseEvent) {
  }
}