package chapter10;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import com.sun.j3d.utils.behaviors.keyboard.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestKeyBehavior extends Applet {
  public static void main(String[] args) {
    new MainFrame(new TestKeyBehavior(), 480, 480);
  }

  public void init() {
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    TextArea ta = new TextArea("",3,30,TextArea.SCROLLBARS_NONE);
    ta.setText("Rotation: left, right, PgUp, PgDn\n");
    ta.append("Translation: up, down, Alt-left, Alt-right, Alt-PgUp, Alt-PgDn\n");
    ta.append("Reset: =\n");
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
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    root.addChild(spin);
    // texture mapped globe
    Appearance ap = createAppearance();
    spin.addChild(new Sphere(0.7f, Primitive.GENERATE_TEXTURE_COORDS, 50, ap));
    // key behavior
    KeyNavigatorBehavior behavior = new KeyNavigatorBehavior(spin);
    BoundingSphere bounds = new BoundingSphere();
    behavior.setSchedulingBounds(bounds);
    spin.addChild(behavior);
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
