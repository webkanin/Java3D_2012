package chapter10;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import com.sun.j3d.utils.behaviors.vp.*;
import chapter7.Axes;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class MoveView extends Applet {
  public static void main(String[] args) {
    new MainFrame(new MoveView(), 480, 480);
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
    BranchGroup root = new BranchGroup();
    // axes
    Transform3D tr = new Transform3D();
    tr.setScale(0.5);
    tr.setTranslation(new Vector3d(-0.8, -0.7, -0.5));
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    Axes axes = new Axes();
    tg.addChild(axes);
    // texture mapped globe
    Appearance ap = createAppearance();
    root.addChild(new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, ap));
    BoundingSphere bounds = new BoundingSphere();
    //light
    AmbientLight light = new AmbientLight(true, new Color3f(Color.blue));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
    new Point3f(0f,0f,2f), new Point3f(1f,0.3f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    //background
    Background background = createBackground();
    background.setApplicationBounds(bounds);
    root.addChild(background);
    root.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    // viewplatform motion
    OrbitBehavior orbit = new OrbitBehavior(cv);
    orbit.setSchedulingBounds(new BoundingSphere());
    su.getViewingPlatform().setViewPlatformBehavior(orbit);
    
    su.addBranchGraph(root);
  }
  
  Appearance createAppearance(){
    Appearance appear = new Appearance();
    URL filename =
    getClass().getClassLoader().getResource("images/earth.jpg");
    TextureLoader loader = new TextureLoader(filename, this);
    Texture texture = loader.getTexture();
    appear.setTexture(texture);
    return appear;
  }
  
  Background createBackground(){
    Background background = new Background();
    BranchGroup bg = new BranchGroup();
    Sphere sphere = new Sphere(1.0f, Sphere.GENERATE_NORMALS |
    Sphere.GENERATE_NORMALS_INWARD |
    Sphere.GENERATE_TEXTURE_COORDS, 60);
    Appearance ap = sphere.getAppearance();
    bg.addChild(sphere);
    background.setGeometry(bg);
    
    URL filename =
    getClass().getClassLoader().getResource("images/stars.jpg");
    TextureLoader loader = new TextureLoader(filename, this);
    Texture texture = loader.getTexture();
    ap.setTexture(texture);
    return background;
  }
}


