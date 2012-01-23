
package chapter11;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.behaviors.interpolators.*;
import chapter6.Dodecahedron;
import chapter10.GullCG;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestInterpolators extends Applet implements ActionListener {
  public static void main(String[] args) {
    new MainFrame(new TestInterpolators(), 800, 600);
  }

  private ColorInterpolator color = null;
  private TransparencyInterpolator transparency = null;
  private SwitchValueInterpolator sw = null;
  private RotationInterpolator rotator = null;
  private PositionInterpolator translator = null;
  private ScaleInterpolator zoom = null;
  private RotPosPathInterpolator path = null;
  private TCBSplinePathInterpolator spline = null;
  private Interpolator current = null;

  public void init() {
    setLayout(new BorderLayout());
    Panel panel = new Panel();
    panel.setLayout(new GridLayout(8,1));
    add(panel, BorderLayout.EAST);
    Button button;
    button = new Button("Color");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("Transparency");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("SwitchValue");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("Rotation");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("Position");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("Scale");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("RotPosPath");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("RotPosScaleTCBSplinePath");
    button.addActionListener(this);
    panel.add(button);
    
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    add(cv, BorderLayout.CENTER);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup tg = new TransformGroup();
    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tg.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    root.addChild(tg);
    // switch node
    Switch swNode = new Switch();
    swNode.setCapability(Switch.ALLOW_SWITCH_WRITE);
    tg.addChild(swNode);
    // appearance
    Appearance ap = new Appearance();
    Material material = new Material();
    material.setCapability(Material.ALLOW_COMPONENT_WRITE);
    material.setColorTarget(Material.AMBIENT);
    ap.setMaterial(material);
    TransparencyAttributes transAttr = new TransparencyAttributes(
    TransparencyAttributes.BLENDED,0.5f);
    transAttr.setCapability(TransparencyAttributes.ALLOW_VALUE_WRITE);
    ap.setTransparencyAttributes(transAttr);
    // gull
    Shape3D shape = new Shape3D(new GullCG(), ap);
    Transform3D trans = new Transform3D();
    trans.setScale(0.5);
    TransformGroup tgScale = new TransformGroup(trans);
    swNode.addChild(tgScale);
    tgScale.addChild(shape);
    // dodecahedron
    Dodecahedron dodec = new Dodecahedron();
    dodec.setAppearance(ap);
    trans.setScale(0.1);
    tgScale = new TransformGroup(trans);
    swNode.addChild(tgScale);
    tgScale.addChild(dodec);
    // interpolators    
    BoundingSphere bounds = new BoundingSphere(new Point3d(0,0,0),100);
    Alpha alpha = new Alpha(-1, 6000);
    alpha.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
    alpha.setDecreasingAlphaDuration(6000);
    // color
    color = new ColorInterpolator(alpha, material, new Color3f(1,0,0), 
      new Color3f(0,0,1));
    color.setSchedulingBounds(bounds);
    color.setEnable(true);
    root.addChild(color);
    // transparency
    transparency = new TransparencyInterpolator(alpha, transAttr);
    transparency.setSchedulingBounds(bounds);
    transparency.setEnable(false);
    root.addChild(transparency);
    // switch
    sw = new SwitchValueInterpolator(alpha, swNode);
    sw.setSchedulingBounds(bounds);
    transparency.setEnable(false);
    root.addChild(sw);
    // rotation
    rotator = new RotationInterpolator(alpha, tg);
    rotator.setSchedulingBounds(bounds);
    rotator.setEnable(false);
    root.addChild(rotator);
    // translation
    translator = new PositionInterpolator(alpha, tg);
    translator.setSchedulingBounds(bounds);
    translator.setEnable(false);
    root.addChild(translator);
    // zoom
    zoom = new ScaleInterpolator(alpha, tg);
    zoom.setSchedulingBounds(bounds);
    zoom.setEnable(false);
    root.addChild(zoom);
    // path
    Transform3D axis = new Transform3D();
    float[] knots = {0,0.25f,0.5f,0.75f,1};
    Quat4f[] rots = new Quat4f[5];
    Point3f[] ps = new Point3f[5];
    for (int i = 0; i < 5; i++) {
      rots[i] = new Quat4f((float)Math.cos(0.5*Math.PI*i),0,0,
        (float)Math.sin(0.5*Math.PI*i));
      ps[i] = new Point3f(0.25f*(i-2), (float)Math.sin(0.5*Math.PI*i), 0);
    }
    path = new RotPosPathInterpolator(alpha, tg, axis, knots, rots, ps);
    path.setSchedulingBounds(bounds);
    path.setEnable(false);
    root.addChild(path);
    // spline
    TCBKeyFrame[] frames = new TCBKeyFrame[5];
    for (int i = 0; i < 5; i++) {
      frames[i] = new TCBKeyFrame(0.25f*i, 0,
      new Point3f(0.25f*(i-2),(float)Math.sin(0.5*Math.PI*i), 0),
      new Quat4f((float)Math.cos(0.5*Math.PI*i),0,0,(float)Math.sin(0.5*Math.PI*i)),
      new Point3f(1,1,1),0,0,0);
    }
    spline = new RotPosScaleTCBSplinePathInterpolator(alpha, tg, axis, frames);
    spline.setSchedulingBounds(bounds);
    spline.setEnable(true);
    root.addChild(spline);
    current = spline;
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
  
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if ("Rotation".equals(cmd)) {
      current.setEnable(false);
      current = rotator;
      current.setEnable(true);
    } else if ("Position".equals(cmd)) {
      current.setEnable(false);
      current = translator;
      current.setEnable(true);
    } else if ("Scale".equals(cmd)) {
      current.setEnable(false);
      current = zoom;
      current.setEnable(true);
    } else if ("RotPosPath".equals(cmd)) {
      current.setEnable(false);
      current = path;
      current.setEnable(true);
    } else if ("RotPosScaleTCBSplinePath".equals(cmd)) {
      current.setEnable(false);
      current = spline;
      current.setEnable(true);
    } else if ("Color".equals(cmd)) {
      color.setEnable(!color.getEnable());
    } else if ("Transparency".equals(cmd)) {
      transparency.setEnable(!transparency.getEnable());
    } else if ("SwitchValue".equals(cmd)) {
      sw.setEnable(!sw.getEnable());
    }
  }
}
