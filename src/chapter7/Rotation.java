package chapter7;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Rotation extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Rotation(), 640, 480);
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
    Background background = new Background(1.0f, 1.0f, 1.0f);
    BoundingSphere bounds = new BoundingSphere();
    background.setApplicationBounds(bounds);
    root.addChild(background);
    Shape3D shape;
    Appearance ap = new Appearance();
    PolygonAttributes pa = new PolygonAttributes();
    ap.setPolygonAttributes(pa);
    ColoringAttributes ca = new ColoringAttributes(0f, 0f, 0f,
      ColoringAttributes.SHADE_FLAT);
    ap.setColoringAttributes(ca);

    LineArray axis = new LineArray(2, LineArray.COORDINATES);
    axis.setCoordinate(0, new Point3d(-0.8, -0.8, -0.8));
    axis.setCoordinate(1, new Point3d(0.5, 0.5, 0.5));
    
    Shape3D axisG = new Shape3D(axis, ap); 
    root.addChild(axisG);
    
    Transform3D tr = new Transform3D();
    tr.setTranslation(new Vector3f(-0.5f, 0f, 0f));
    tr.setScale(0.1);
    TransformGroup tg;
    TransformGroup rot;
    for (int i = 0; i < 8; i++) {
      shape = new ColorCube();
      shape.setAppearance(ap);
      tg = new TransformGroup(tr);
      Transform3D trRot = new Transform3D();
      trRot.set(new AxisAngle4d(0.5,0.5,0.5, Math.PI/4*i));
      rot = new TransformGroup(trRot);
      root.addChild(rot);
      rot.addChild(tg);
      tg.addChild(shape); 
    }
    return root;
  }
}