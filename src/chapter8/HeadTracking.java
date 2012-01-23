
package chapter8;

import java.awt.*;
import com.sun.j3d.utils.geometry.ColorCube;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.net.URL;
import java.awt.image.*;
import javax.imageio.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class HeadTracking extends Applet {
  public static void main(String[] args) {
    new MainFrame(new HeadTracking(), 500, 500);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);  
    BranchGroup scene = createSceneGraph();
    SimpleUniverse su = new SimpleUniverse(cv);
    // add avatar
    LineAxes axes = new LineAxes(0.2f);
    Shape3D shape = new Shape3D(axes);
    ViewerAvatar va = new ViewerAvatar();
    va.addChild(shape);
    su.getViewer().setAvatar(va);
    // install the VirtualInputDevice
    String[] args = new String[0];
    InputDevice device = new VirtualInputDevice( args );
    device.initialize();
    PhysicalEnvironment pe = su.getViewer().getPhysicalEnvironment();
    pe.addInputDevice(device);
    pe.setSensor(0, device.getSensor(0));
    // set up head tracking
    su.getViewingPlatform().setNominalViewingTransform();
    pe.setCoexistenceCenterInPworldPolicy(View.NOMINAL_HEAD);
    View view = su.getViewer().getView();
    view.setUserHeadToVworldEnable(true);
    view.setCoexistenceCenteringEnable(false);
    Screen3D screen = cv.getScreen3D();
    Transform3D tr = new Transform3D();
    tr.setTranslation(new Vector3d(0.1, 0.1, 0.0));
    screen.setTrackerBaseToImagePlate(tr);
    view.setTrackingEnable(true);
    su.addBranchGraph(scene);
  }
  
  public BranchGroup createSceneGraph() {
    BranchGroup objRoot = new BranchGroup();
    TransformGroup objTrans = new TransformGroup();
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objRoot.addChild(objTrans);
    objTrans.addChild(new ColorCube(0.2));
    Transform3D yAxis = new Transform3D();
    Alpha rotationAlpha = new Alpha(-1, Alpha.INCREASING_ENABLE,
    0, 0,
    4000, 0, 0,
    0, 0, 0);
    RotationInterpolator rotator =
    new RotationInterpolator(rotationAlpha, objTrans, yAxis,
    0.0f, (float) Math.PI*2.0f);
    BoundingSphere bounds =
    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    rotator.setSchedulingBounds(bounds);
    objTrans.addChild(rotator);
    //background
    URL url = getClass().getClassLoader().getResource("images/bg.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
    Background background = new Background(image);
    background.setApplicationBounds(bounds);
    objRoot.addChild(background);
    return objRoot;
  }
}
