package chapter9;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TextureMapping extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TextureMapping(), 640, 480);
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
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    //object
    Appearance ap = createTextureAppearance();
    Sphere shape = new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, ap);
    spin.addChild(shape);
    //rotator
    Alpha alpha = new Alpha(-1, 6000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //background
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }
  
  Appearance createTextureAppearance(){    
    Appearance ap = new Appearance();
    URL filename = 
        getClass().getClassLoader().getResource("images/earth.jpg");
    TextureLoader loader = new TextureLoader(filename, this);
    ImageComponent2D image = loader.getImage();
    if(image == null) {
      System.out.println("can't find texture file.");
    }
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
