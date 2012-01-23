
package chapter10;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.picking.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import chapter7.Axes;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class DataViewer3D extends Applet {
  public static void main(String[] args) {
    new MainFrame(new DataViewer3D(), 640, 480);
  }
  
  PointArray geom;
  PickCanvas pc;
  TextField text;
  
  public void init() {
    setLayout(new BorderLayout());
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    add(cv, BorderLayout.CENTER);
    cv.addMouseListener(new MouseAdapter() {
      public void mouseClicked(java.awt.event.MouseEvent mouseEvent) {
        pick(mouseEvent);
      }
    });
    text = new TextField();
    add(text, BorderLayout.SOUTH);
    BranchGroup bg = createSceneGraph(cv);
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph(Canvas3D cv) {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    // axes
    Transform3D tr = new Transform3D();
    tr.setScale(0.3);
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    Axes axes = new Axes();
    tg.addChild(axes);
    //appearance
    Appearance ap = new Appearance();
    ap.setPointAttributes(new PointAttributes(10f, true));
    // objects
    int n = 20;
    geom = new PointArray(n, PointArray.COORDINATES | PointArray.COLOR_4);
    geom.setCapability(PointArray.ALLOW_COORDINATE_READ);
    geom.setCapability(PointArray.ALLOW_FORMAT_READ);
    geom.setCapability(PointArray.ALLOW_COLOR_READ);
    geom.setCapability(PointArray.ALLOW_COLOR_WRITE);
    geom.setCapability(PointArray.ALLOW_COUNT_READ);
    Point3f[] coords = new Point3f[n];
    Color4f[] colors = new Color4f[n];
    for (int i = 0; i < n; i++) {
      coords[i] = new Point3f((float)(Math.random()-0.5),
      (float)(Math.random()-0.5),(float)(Math.random()-0.5));
      colors[i] = new Color4f((float)(Math.random()),
      (float)(Math.random()),(float)(Math.random()),1f);
    }
    geom.setCoordinates(0, coords);
    geom.setColors(0, colors);
    BranchGroup bg = new BranchGroup();
    spin.addChild(bg);
    pc = new PickCanvas(cv, bg);
    pc.setTolerance(5);
    pc.setMode(PickTool.GEOMETRY_INTERSECT_INFO);
    Shape3D shape = new Shape3D(geom, ap);
    bg.addChild(shape);
    PickTool.setCapabilities(shape, PickTool.INTERSECT_TEST);
    shape.setCapability(Shape3D.ALLOW_GEOMETRY_READ);
    // rotation
    MouseRotate rotator = new MouseRotate(spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    // translation
    MouseTranslate translator = new MouseTranslate(spin);
    translator.setSchedulingBounds(bounds);
    spin.addChild(translator);
    // zoom
    MouseZoom zoom = new MouseZoom(spin);
    zoom.setSchedulingBounds(bounds);
    spin.addChild(zoom);
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
  
  private void pick(MouseEvent mouseEvent) {
    Color4f color = new Color4f();
    pc.setShapeLocation(mouseEvent);
    PickResult[] results = pc.pickAll();
    for (int i = 0; (results != null) && (i < results.length); i++) {
      PickIntersection inter = results[i].getIntersection(0);
      Point3d pt = inter.getClosestVertexCoordinates();
      int[] ind = inter.getPrimitiveCoordinateIndices();
      text.setText("vertex " + ind[0] + ": (" + pt.x + ", "+ pt.y + ", " + pt.z + ")");
      geom.getColor(ind[0], color);
      color.x = 1f - color.x;
      color.y = 1f - color.y;
      color.z = 1f - color.z;
      if (color.w > 0.8) color.w = 0.5f;
      else color.w = 1f;
      geom.setColor(ind[0], color);
    }
  }
}
