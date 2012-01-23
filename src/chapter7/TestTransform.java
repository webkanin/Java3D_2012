package chapter7;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestTransform extends Applet implements ActionListener {
  public static void main(String[] args) {
    new MainFrame(new TestTransform(), 640, 300);
  }
  
  TransformGroup trGroup;
  Transform3D transform = new Transform3D();
  MatrixPanel mp = new MatrixPanel();
  TextField rx = new TextField();
  TextField ry = new TextField();
  TextField rz = new TextField();
  TextField ra = new TextField();
  TextField tx = new TextField();
  TextField ty = new TextField();
  TextField tz = new TextField();
  TextField sx = new TextField();
  TextField sy = new TextField();
  TextField sz = new TextField();
  
  public void init() {  
    setLayout(new BorderLayout());
    
    Panel eastPanel = new Panel();
    eastPanel.setLayout(new BorderLayout());
    eastPanel.add(mp, BorderLayout.NORTH);
    add(eastPanel, BorderLayout.EAST);
    
    Button button = new Button("Transform");
    button.addActionListener(this);
    Panel p = new Panel();
    p.add(button);
    eastPanel.add(p, BorderLayout.EAST);
    
    p = new Panel();
    p.setLayout(new GridLayout(4,5));
    p.add(new Label("x"));
    p.add(new Label("y"));
    p.add(new Label("z"));
    p.add(new Label("angle"));
    p.add(new Label(""));
    
    p.add(rx);
    p.add(ry);
    p.add(rz);
    p.add(ra);
    button = new Button("Rotate");
    button.addActionListener(this);
    p.add(button);
    
    p.add(tx);
    p.add(ty);
    p.add(tz);
    p.add(new Panel());
    button = new Button("Translate");
    button.addActionListener(this);
    p.add(button);
    
    p.add(sx);
    p.add(sy);
    p.add(sz);
    p.add(new Panel());
    button = new Button("Scale");
    button.addActionListener(this);
    p.add(button);
    
    eastPanel.add(p, BorderLayout.SOUTH);
    
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
    trGroup = new TransformGroup();
    trGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(trGroup);
    
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Group shape = new Axes();
    
    Transform3D tr = new Transform3D();
    tr.setScale(0.5);
    TransformGroup tg = new TransformGroup(tr);
    trGroup.addChild(tg);
    tg.addChild(shape);
        
    //background and light
    BoundingSphere bounds = new BoundingSphere();
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.green), new Point3f(3f,3f,3f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    PointLight ptlight2 = new PointLight(new Color3f(Color.orange), new Point3f(-2f,2f,2f), new Point3f(1f,0f,0f));
    ptlight2.setInfluencingBounds(bounds);
    root.addChild(ptlight2);
    return root;
  }
  
  public void actionPerformed(ActionEvent e) {
    Matrix4d m = new Matrix4d();
    mp.get(m);
    transform.set(m);
    String cmd = e.getActionCommand();
    if ("Transform".equals(cmd)) {
      Quat4d quat = new Quat4d();
      Vector3d translation = new Vector3d();
      transform.get(quat, translation);
      Vector3d scale = new Vector3d();
      transform.getScale(scale);
      AxisAngle4d rotation = new AxisAngle4d();
      rotation.set(quat);
      rx.setText("" + rotation.x);
      ry.setText("" + rotation.y);
      rz.setText("" + rotation.z);
      ra.setText("" + rotation.angle);
      tx.setText("" + translation.x);
      ty.setText("" + translation.y);
      tz.setText("" + translation.z);
      sx.setText("" + scale.x);
      sy.setText("" + scale.y);
      sz.setText("" + scale.z);
      trGroup.setTransform(transform);
    } else {
      if ("Rotate".equals(cmd)) {
        double x = Double.parseDouble(rx.getText());
        double y = Double.parseDouble(ry.getText());
        double z = Double.parseDouble(rz.getText());
        double a = Double.parseDouble(ra.getText());
        transform.setRotation(new AxisAngle4d(x, y, z, a));
      } else if ("Translate".equals(cmd)) {
        double x = Double.parseDouble(tx.getText());
        double y = Double.parseDouble(ty.getText());
        double z = Double.parseDouble(tz.getText());
        transform.setTranslation(new Vector3d(x, y, z));
      } else if ("Scale".equals(cmd)) {
        double x = Double.parseDouble(sx.getText());
        double y = Double.parseDouble(sy.getText());
        double z = Double.parseDouble(sz.getText());
        transform.setScale(new Vector3d(x, y, z));
      }
      transform.get(m);
      mp.set(m);
    }
  }  
}