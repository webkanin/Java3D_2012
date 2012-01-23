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
import chapter7.Axes;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class MoveGlobe extends Applet {
  public static void main(String[] args) {
    new MainFrame(new MoveGlobe(), 480, 480);
  }

  public void init() {
    // create canvas
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
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    // axes
    Transform3D tr = new Transform3D();
    tr.setScale(0.5);
    tr.setTranslation(new Vector3d(-0.8, -0.7, -0.5));
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    Axes axes = new Axes();
    tg.addChild(axes);
    // transform
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    root.addChild(spin);
    // texture mapped globe
    Appearance ap = createAppearance();
    spin.addChild(new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, ap));
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
  
  Appearance createAppearance(){    
    Appearance appear = new Appearance();    
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
    appear.setTexture(texture);
    return appear;
  }
}
