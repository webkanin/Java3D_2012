package chapter10;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.picking.PickTool;
import com.sun.j3d.utils.picking.behaviors.*;
import chapter7.Axes;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestPickBehavior extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestPickBehavior(), 480, 480);
  }

  public void init() {
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    TextArea ta = new TextArea("",3,30,TextArea.SCROLLBARS_NONE);
    ta.setText("Rotation: Drag with left button\n");
    ta.append("Translation: Drag with right button\n");
    ta.append("Zoom: Hold Alt key and drag with left button");
    ta.setEditable(false);
    add(ta, BorderLayout.SOUTH);
    BranchGroup bg = createSceneGraph(cv);
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph(Canvas3D cv) {
    BranchGroup root = new BranchGroup();
    // add 8 seagulls
    for (int i = 0; i < 8; i++)
      root.addChild(createObject());
    
    BoundingSphere bounds = new BoundingSphere();
    // rotation
    PickRotateBehavior rotator = new PickRotateBehavior(root, cv, bounds, 
      PickTool.GEOMETRY);
    root.addChild(rotator);
    // translation
    PickTranslateBehavior translator = new PickTranslateBehavior(root, cv,
      bounds, PickTool.GEOMETRY);
    root.addChild(translator);
    // zoom
    PickZoomBehavior zoom = new PickZoomBehavior(root, cv, bounds,
      PickTool.GEOMETRY);
    root.addChild(zoom);
    //light
    AmbientLight light = new AmbientLight(true, new Color3f(Color.blue));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
      new Point3f(0f,0f,2f), new Point3f(1f,0.3f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    //background
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }
  
  private Node createObject() {
    // transform
    Transform3D trans = new Transform3D();
    trans.setTranslation(new Vector3d(Math.random()-0.5, Math.random()-0.5,
      Math.random()-0.5));
    trans.setScale(0.3);
    TransformGroup spin = new TransformGroup(trans);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    spin.setCapability(TransformGroup.ENABLE_PICK_REPORTING);
    // visual objects
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Shape3D(new GullCG(), ap);
    PickTool.setCapabilities(shape, PickTool.INTERSECT_FULL);
    spin.addChild(shape);
    return spin;
  }  
}
