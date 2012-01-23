package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import javax.imageio.*;
import java.awt.image.*;
import java.io.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Marble extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Marble(), 480, 480);
  }
  
  PerlinNoise pnoise = new PerlinNoise();

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
    Shape3D shape = new Shape3D(createGeometry(), ap);
    spin.addChild(shape);
    //rotator
    Alpha alpha = new Alpha(-1, 8000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }
  
  GeometryArray createGeometry() {
    IndexedTriangleArray ga = new IndexedTriangleArray(4,
    TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.TEXTURE_COORDINATE_3, 12);
    ga.setCoordinate(0, new Point3f(0.5f,0.5f,0.5f));
    ga.setCoordinate(1, new Point3f(0.5f,-0.5f,-0.5f));
    ga.setCoordinate(2, new Point3f(-0.5f,0.5f,-0.5f));
    ga.setCoordinate(3, new Point3f(-0.5f,-0.5f,0.5f));
    int[] coords = {0,1,2,0,3,1,1,3,2,2,3,0};
    ga.setNormal(0, new Vector3f(1f,1f,-1f));
    ga.setNormal(1, new Vector3f(1f,-1f,1f));
    ga.setNormal(2, new Vector3f(-1f,-1f,-1f));
    ga.setNormal(3, new Vector3f(-1f,1f,1f));
    int[] norms = {0,0,0,1,1,1,2,2,2,3,3,3};
    ga.setCoordinateIndices(0, coords);
    ga.setNormalIndices(0, norms);
    ga.setTextureCoordinate(0, 0, new TexCoord3f(1f,1f,1f));
    ga.setTextureCoordinate(0, 1, new TexCoord3f(1f,0f,0f));
    ga.setTextureCoordinate(0, 2, new TexCoord3f(0f,1f,0f));
    ga.setTextureCoordinate(0, 3, new TexCoord3f(0f,0f,1f));
    ga.setTextureCoordinateIndices(0,0,coords);
    return ga;
  }
  
  Appearance createTextureAppearance(){    
    Appearance ap = new Appearance();
    BufferedImage[] img = new BufferedImage[128];
    for (int i = 0; i < 128; i++) {
      img[i] = new BufferedImage(128,128, BufferedImage.TYPE_INT_ARGB);
      for (int r = 0; r < 128; r++) {
        for (int c = 0; c < 128; c++) {
          double v = pnoise.turbulence(new Point3d(c/32.0, r/32.0, i/32.0),2,2,8);
          int rgb = (int)((0.55+0.35*Math.sin(3*(c/32.0+v)))*256);
          rgb = ((rgb << 16) | (rgb << 8) | rgb);
          img[i].setRGB(c, r, rgb);
        }
      }
    }
    ImageComponent3D image = new ImageComponent3D(ImageComponent3D.FORMAT_RGB, img);
    Texture3D texture = new Texture3D(Texture.BASE_LEVEL, Texture.RGBA,
    image.getWidth(), image.getHeight(), image.getDepth());
    texture.setImage(0, image);
    texture.setEnable(true);
    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
    ap.setTexture(texture);
    return ap;
  }
}
