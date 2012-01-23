package chapter9;

import javax.vecmath.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.net.URL;
import java.util.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;
import com.sun.j3d.utils.behaviors.mouse.*;

public class Cup extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Cup(), 640, 480);
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
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    root.addChild(spin);
    //object transform
    Transform3D tr = new Transform3D();
    tr.setScale(0.3);
    tr.setRotation(new AxisAngle4d(0, 0, 1, Math.PI/12));
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    //object
    Geometry geom = createGeometry();
    Appearance ap = createTextureAppearance();
    PolygonAttributes pa = new PolygonAttributes(PolygonAttributes.POLYGON_FILL,
    PolygonAttributes.CULL_NONE,0);
    ap.setPolygonAttributes(pa);
    Shape3D shape = new Shape3D(geom, ap);
    tg.addChild(shape);    
    // rotation
    BoundingSphere bounds = new BoundingSphere();
    Alpha alpha = new Alpha(-1, 10000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //background and lights
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.lightGray));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
    new Point3f(3f,3f,3f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    return root;
  }
  
  Geometry createGeometry() {
    int m = 120;
    int n = 2;
    QuadArray qa = new QuadArray(4*m,
    QuadArray.COORDINATES|QuadArray.NORMALS|QuadArray.TEXTURE_COORDINATE_2);
    // generate the cylinder
    Transform3D trans = new Transform3D();
    trans.rotY(2*Math.PI/m);
    Point3f pt0 = new Point3f(1,1,0);
    Point3f pt1 = new Point3f(1,-1,0);
    Vector3f normal = new Vector3f(1,0,0);
    for (int j = 0; j < m; j++) {
      qa.setCoordinate(j*4, pt0);
      qa.setCoordinate(j*4+1, pt1);
      qa.setNormal(j*4, normal);
      qa.setNormal(j*4+1, normal);
      trans.transform(pt0);
      trans.transform(pt1);
      trans.transform(normal);
      qa.setCoordinate(j*4+2, pt1);
      qa.setCoordinate(j*4+3, pt0);
      qa.setNormal(j*4+2, normal);
      qa.setNormal(j*4+3, normal);
      // set texture coordinates
      TexCoord2f tex0 = new TexCoord2f(j*1f/m, 1f);
      TexCoord2f tex1 = new TexCoord2f(j*1f/m, 0f);
      qa.setTextureCoordinate(0,j*4,tex0);
      qa.setTextureCoordinate(0,j*4+1,tex1);
      tex0 = new TexCoord2f((j+1)*1f/m, 1f);
      tex1 = new TexCoord2f((j+1)*1f/m, 0f);
      qa.setTextureCoordinate(0,j*4+2,tex1);
      qa.setTextureCoordinate(0,j*4+3,tex0);
    }
    return qa;
  }
  
  Appearance createTextureAppearance(){
    Appearance ap = new Appearance();
    BufferedImage bi = new BufferedImage(512,128, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2 = (Graphics2D)bi.getGraphics();
    g2.setColor(Color.white);
    g2.fillRect(0, 0, 512,128);
    g2.setFont(new Font("Serif", Font.BOLD, 48));
    g2.setColor(new Color(200,0,0));
    g2.drawString("Java 3D",0,100);
    ImageComponent2D image = 
      new ImageComponent2D(ImageComponent2D.FORMAT_RGBA, bi);
    Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
    image.getWidth(), image.getHeight());
    texture.setImage(0, image);
    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    ap.setTexture(texture);
    //combine texture and lighting
    TextureAttributes texatt = new TextureAttributes();
    texatt.setTextureMode(TextureAttributes.COMBINE);
    ap.setTextureAttributes(texatt);
    ap.setMaterial(new Material());
    return ap;
  }
}