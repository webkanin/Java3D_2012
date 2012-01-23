package chapter1;

import javax.vecmath.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.net.URL;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Demo3D extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Demo3D(), 480, 480);
  }
  
  private SimpleUniverse su;
  
  public void init() {
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv);
    BranchGroup bg = createSceneGraph();
    bg.compile();
    su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  public void destroy() {
    su.cleanup();
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    // 3d text
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Font3D font = new Font3D(new Font("Helvetica", Font.PLAIN, 1), new FontExtrusion());
    Text3D text = new Text3D(font, "Java 3D");
    Shape3D shape = new Shape3D(text, ap);
    // transform for text
    Transform3D tr = new Transform3D();
    tr.setScale(0.2);
    tr.setTranslation(new Vector3d(-0.35,-0.15,0.75));
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    tg.addChild(shape);
    // globe
    ap = createAppearance();
    spin.addChild(new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, ap));
    // rotation
    Alpha alpha = new Alpha(-1, 6000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    // background and lights
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
    new Point3f(3f,3f,3f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    return root;
  }
  
  private Appearance createAppearance(){
    Appearance ap = new Appearance();
    URL filename =
    getClass().getClassLoader().getResource("images/earth.jpg");
    TextureLoader loader = new TextureLoader(filename, this);
    ImageComponent2D image = loader.getImage();
    Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
    image.getWidth(), image.getHeight());
    texture.setImage(0, image);
    texture.setEnable(true);
    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
    ap.setTexture(texture);
    return ap;
  }
}
