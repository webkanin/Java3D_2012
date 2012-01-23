
package chapter9;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.net.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import chapter6.Dodecahedron;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class CubeTexture extends Applet implements ActionListener{
  public static void main(String[] args) {
    new MainFrame(new CubeTexture(), 640, 480);
  }

  private Appearance ap = null;

  public void init() {
    setLayout(new BorderLayout());
    Panel panel = new Panel();
    panel.setLayout(new GridLayout(5,1));
    add(panel, BorderLayout.EAST);
    Button button = new Button("OBJECT_LINEAR");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("EYE_LINEAR");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("SPHERE_MAP");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("NORMAL_MAP");
    button.addActionListener(this);
    panel.add(button);
    button = new Button("REFLECTION_MAP");
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
    TransformGroup spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    root.addChild(spin);
    //object
    ap = createTextureAppearance();
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(ap);
    Transform3D tr = new Transform3D();
    tr.setScale(0.4);
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(shape);
    spin.addChild(tg);
    //rotator
    Alpha alpha = new Alpha(-1, 18000);
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
    ImageComponent2D image1 = loader.getImage();
    filename = getClass().getClassLoader().getResource("images/stone.jpg");
    loader = new TextureLoader(filename, this);
    ImageComponent2D image2 = loader.getImage();
    filename = getClass().getClassLoader().getResource("images/sky.jpg");
    loader = new TextureLoader(filename, this);
    ImageComponent2D image3 = loader.getImage();

    TextureCubeMap texture = new TextureCubeMap(Texture.BASE_LEVEL, Texture.RGBA,
    image1.getWidth());
    texture.setImage(0, TextureCubeMap.NEGATIVE_X, image3);
    texture.setImage(0, TextureCubeMap.NEGATIVE_Y, image1);
    texture.setImage(0, TextureCubeMap.NEGATIVE_Z, image2);
    texture.setImage(0, TextureCubeMap.POSITIVE_X, image3);
    texture.setImage(0, TextureCubeMap.POSITIVE_Y, image1);
    texture.setImage(0, TextureCubeMap.POSITIVE_Z, image2);
   
    texture.setEnable(true);
    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
    ap.setTexture(texture);
    
    TexCoordGeneration tcg = new TexCoordGeneration(TexCoordGeneration.OBJECT_LINEAR, 
    TexCoordGeneration.TEXTURE_COORDINATE_3);
    tcg.setPlaneR(new Vector4f(2, 0, 0, 0));
    tcg.setPlaneS(new Vector4f(0, 2, 0, 0));
    tcg.setPlaneT(new Vector4f(0, 0, 2, 0));
    ap.setTexCoordGeneration(tcg);
    ap.setCapability(Appearance.ALLOW_TEXGEN_WRITE);
    return ap;
  }
  
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    TexCoordGeneration tcg = new TexCoordGeneration();
    if ("OBJECT_LINEAR".equals(cmd)) {
      tcg.setGenMode(TexCoordGeneration.OBJECT_LINEAR);
    } else if ("EYE_LINEAR".equals(cmd)) {
      tcg.setGenMode(TexCoordGeneration.EYE_LINEAR);
    } else if ("SPHERE_MAP".equals(cmd)) {
      tcg.setGenMode(TexCoordGeneration.SPHERE_MAP);
    } else if ("NORMAL_MAP".equals(cmd)) {
      tcg.setGenMode(TexCoordGeneration.NORMAL_MAP);
    } else if ("REFLECTION_MAP".equals(cmd)) {
      tcg.setGenMode(TexCoordGeneration.REFLECTION_MAP);
    }
    tcg.setFormat(TexCoordGeneration.TEXTURE_COORDINATE_3);
    tcg.setPlaneR(new Vector4f(2, 0, 0, 0));
    tcg.setPlaneS(new Vector4f(0, 2, 0, 0));
    tcg.setPlaneT(new Vector4f(0, 0, 2, 0));
    ap.setTexCoordGeneration(tcg);
  }  
}
