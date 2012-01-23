
package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.net.URL;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import chapter6.Dodecahedron;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class MovingShadow extends Applet {
  public static void main(String[] args) {
    new MainFrame(new MovingShadow(), 640, 480);
  }
  
  private TransformGroup spin = null;
  private Transform3D shadowProj = null;
  private GeometryArray geom = null;
  private GeometryArray shadowGeom = null;
  private ShadowUpdater updater = null;

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    updater = new MovingShadow.ShadowUpdater();
    BranchGroup bg = createSceneGraph();
    bg.compile();
    SimpleUniverse su = new SimpleUniverse(cv);
    su.getViewingPlatform().setNominalViewingTransform();
    su.addBranchGraph(bg);
  }
  
  private BranchGroup createSceneGraph() {
    BranchGroup root = new BranchGroup();
    //object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Dodecahedron();
    shape.setAppearance(ap);
    geom = (GeometryArray)shape.getGeometry();
    geom.setCapability(GeometryArray.ALLOW_COORDINATE_READ);
    //transform
    Transform3D tr = new Transform3D();
    tr.rotY(-0.2);
    tr.setScale(0.2);
    TransformGroup tg = new TransformGroup(tr);
    root.addChild(tg);
    spin = new TransformGroup();
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    spin.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    tg.addChild(spin);
    spin.addChild(shape);
    //rotator
    Alpha alpha = new Alpha(-1, 8000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    //light and background
    Background background = new Background(1.0f, 1.0f, 1.0f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.red));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    Point3f lightPos = new Point3f(10f,3f,1f);
    PointLight ptlight = new PointLight(new Color3f(Color.green),
    lightPos, new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    tg.addChild(ptlight);
    // wall
    Shape3D wall = createWall();
    tg.addChild(wall);
    // shadow
    shadowGeom = createShadow(geom, lightPos, new Point3f(-2f, 3f, 1f));
    ap = new Appearance();
    ColoringAttributes colorAttr = new ColoringAttributes(0.1f, 0.1f, 0.1f, 
      ColoringAttributes.FASTEST);
    ap.setColoringAttributes(colorAttr);
    TransparencyAttributes transAttr = new TransparencyAttributes(
      TransparencyAttributes.BLENDED,0.35f);
    ap.setTransparencyAttributes(transAttr);
    PolygonAttributes polyAttr = new PolygonAttributes();
    polyAttr.setCullFace(PolygonAttributes.CULL_NONE);
    ap.setPolygonAttributes(polyAttr);
    shape = new Shape3D(shadowGeom, ap);
    tg.addChild(shape);
    // shadow update
    ShadowBehavior sb = new MovingShadow.ShadowBehavior();
    sb.setSchedulingBounds(bounds);
    tg.addChild(sb);
    return root;
  }
  
  private Shape3D createWall() {
    URL url = getClass().getClassLoader().getResource("images/stone.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
    Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA,
    image.getWidth(), image.getHeight());
    texture.setImage(0, image);
    texture.setEnable(true);
    texture.setMagFilter(Texture.BASE_LEVEL_LINEAR);
    texture.setMinFilter(Texture.BASE_LEVEL_LINEAR);
    Appearance appear = new Appearance();
    appear.setTexture(texture);
    QuadArray rect = new QuadArray(4, QuadArray.COORDINATES|QuadArray.TEXTURE_COORDINATE_2);
    rect.setCoordinate(0, new Point3d(-2,3,2));
    rect.setCoordinate(1, new Point3d(-2,-3,2));
    rect.setCoordinate(2, new Point3d(-2,-3,-3));
    rect.setCoordinate(3, new Point3d(-2,3,-3));
    rect.setTextureCoordinate(0,0, new TexCoord2f(0f, 0f));
    rect.setTextureCoordinate(0,1, new TexCoord2f(0f, 1f));
    rect.setTextureCoordinate(0,2, new TexCoord2f(1f, 1f));
    rect.setTextureCoordinate(0,3, new TexCoord2f(1f, 0f));
    return new Shape3D(rect, appear);
  }
  
  private GeometryArray createShadow(GeometryArray ga, Point3f light, Point3f plane) {
    GeometryInfo gi = new GeometryInfo(ga);
    gi.convertToIndexedTriangles();
    IndexedTriangleArray ita = (IndexedTriangleArray)gi.getIndexedGeometryArray();
    Vector3f v = new Vector3f();
    v.sub(plane, light);
    double[] mat = new double[16];
    for (int i = 0; i < 16; i++) {
      mat[i] = 0;
    }
    mat[0] = 1;
    mat[5] = 1;
    mat[10] = 1-0.001;
    mat[14] = -1/v.length();
    Transform3D proj = new Transform3D();
    proj.set(mat);
    Transform3D u = new Transform3D();
    u.lookAt(new Point3d(light), new Point3d(plane), new Vector3d(0,1,0));
    proj.mul(u);
    shadowProj = new Transform3D();
    u.invert();
    shadowProj.mul(u, proj);
    int n = ita.getVertexCount();
    int count = ita.getIndexCount();
    IndexedTriangleArray shadow = new IndexedTriangleArray(n,
    GeometryArray.COORDINATES | GeometryArray.BY_REFERENCE, count);
    shadow.setCapability(GeometryArray.ALLOW_REF_DATA_READ);
    shadow.setCapability(GeometryArray.ALLOW_REF_DATA_WRITE);
    double[] vert = new double[3*n];
    Point3d p = new Point3d();
    for (int i = 0; i < n; i++) {
      ga.getCoordinate(i, p);
      Vector4d v4 = new Vector4d(p);
      v4.w = 1;
      shadowProj.transform(v4);
      Point4d p4 = new Point4d(v4);
      p.project(p4);
      vert[3*i] = p.x;
      vert[3*i+1] = p.y;
      vert[3*i+2] = p.z;
    }
    shadow.setCoordRefDouble(vert);
    int[] indices = new int[count];
    ita.getCoordinateIndices(0, indices);
    shadow.setCoordinateIndices(0, indices);
    return shadow;
  }
  
  class ShadowUpdater implements GeometryUpdater {    
    public void updateData(Geometry geometry) {
      double[] vert = ((GeometryArray)geometry).getCoordRefDouble();
      int n = vert.length/3;
      Transform3D rot = new Transform3D();
      spin.getTransform(rot);
      Transform3D tr = new Transform3D(shadowProj);
      tr.mul(rot);
      Point3d p = new Point3d();
      for (int i = 0; i < n; i++) {
        geom.getCoordinate(i, p);
        Vector4d v4 = new Vector4d(p);
        v4.w = 1;
        tr.transform(v4);
        Point4d p4 = new Point4d(v4);
        p.project(p4);
        vert[3*i] = p.x;
        vert[3*i+1] = p.y;
        vert[3*i+2] = p.z;
      }
    }
  }
  
  class ShadowBehavior extends Behavior {
    WakeupOnElapsedFrames wakeup = null;
    
    public ShadowBehavior() {
      wakeup = new WakeupOnElapsedFrames(0);
    }
    
    public void initialize() {
      wakeupOn(wakeup);
    }
    
    public void processStimulus(java.util.Enumeration enumeration) {
      shadowGeom.updateData(updater);
      wakeupOn(wakeup);
    }    
  }
}
