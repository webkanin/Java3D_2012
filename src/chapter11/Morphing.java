package chapter11;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Morphing extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Morphing(), 480, 480);
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
    // geometry
    GeometryArray[] geoms = new GeometryArray[4];
    geoms[0] = createGeometry1(0.1);
    geoms[1] = createGeometry1(0.7);
    geoms[2] = createGeometry2(0.5);
    geoms[3] = createGeometry2(0.8);
    Appearance appear = new Appearance();
    appear.setMaterial(new Material());
    Morph morph = new Morph(geoms, appear);
    morph.setCapability(Morph.ALLOW_WEIGHTS_READ);
    morph.setCapability(Morph.ALLOW_WEIGHTS_WRITE);
    Transform3D tr = new Transform3D();
    tr.rotX(Math.PI/2);
    TransformGroup tg = new TransformGroup(tr);
    tg.addChild(morph);
    root.addChild(tg);
    // Behavior node
    Alpha alpha = new Alpha(-1, Alpha.INCREASING_ENABLE|Alpha.DECREASING_ENABLE,
    0,0, 8000,0,0,8000,0,0);
    MorphingBehavior mb = new MorphingBehavior(morph, alpha);
    BoundingSphere bounds = new BoundingSphere();
    mb.setSchedulingBounds(bounds);
    root.addChild(mb);
    //light
    AmbientLight light = new AmbientLight(true, new Color3f(Color.blue));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
      new Point3f(0.7f,0.7f,2f), new Point3f(1f,0f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    //background
    Background background = new Background(0.7f, 0.7f, 0.7f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    return root;
  }

  GeometryArray createGeometry1(double h) {
    double r1 = 0.1;
    double r2 = 0.5;
    int m = 20;
    int n = 40;
    Point3d[] pts = new Point3d[m];
    pts[0] = new Point3d(r1+r2, 0, 0);
    double theta = 2.0 * Math.PI / m;
    double c = Math.cos(theta);
    double s = Math.sin(theta);
    double[] mat = {c, -s, 0, r2*(1-c),
                    s, c, 0, -r2*s,
                    0, 0, 1, 0,
                    0, 0, 0, 1};
    Transform3D rot1 = new Transform3D(mat);
    for (int i = 1; i < m; i++) {
      pts[i] = new Point3d();
      rot1.transform(pts[i-1], pts[i]);
    }

    Transform3D rot2 = new Transform3D();
    rot2.set(new Vector3d(0,0,-h/n));
    IndexedQuadArray qa = new IndexedQuadArray(m*n, IndexedQuadArray.COORDINATES, 
      4*m*(n-1));
    int quadIndex = 0;
    for (int i = 0; i < n; i++) {
      qa.setCoordinates(i*m, pts);
      for (int j = 0; j < m; j++) {
        rot2.transform(pts[j]);
        int[] quadCoords = {i*m+j, ((i+1)%n)*m+j, ((i+1)%n)*m+((j+1)%m), 
          i*m+((j+1)%m)};
        if (i < n-1)
        qa.setCoordinateIndices(quadIndex, quadCoords);
        quadIndex += 4;
      }
    }
    GeometryInfo gi = new GeometryInfo(qa);
    NormalGenerator ng = new NormalGenerator();
    ng.generateNormals(gi);
    return gi.getGeometryArray();
  }

  GeometryArray createGeometry2(double h) {
    double r1 = 0.1;
    double r2 = 0.5;
    int m = 20;
    int n = 40;
    Point3d[] pts = new Point3d[m];
    pts[0] = new Point3d(r1+r2, 0, 0);
    double theta = 2.0 * Math.PI / m;
    double c = Math.cos(theta);
    double s = Math.sin(theta);
    double[] mat = {c, -s, 0, r2*(1-c),
                    s, c, 0, -r2*s,
                    0, 0, 1, 0,
                    0, 0, 0, 1};
    Transform3D rot1 = new Transform3D(mat);
    for (int i = 1; i < m; i++) {
      pts[i] = new Point3d();
      rot1.transform(pts[i-1], pts[i]);
    }

    Transform3D rot2 = new Transform3D();
    rot2.rotY(h*2*Math.PI/n);
    IndexedQuadArray qa = new IndexedQuadArray(m*n, IndexedQuadArray.COORDINATES,
      4*m*(n-1));
    int quadIndex = 0;
    for (int i = 0; i < n; i++) {
      qa.setCoordinates(i*m, pts);
      for (int j = 0; j < m; j++) {
        rot2.transform(pts[j]);
        int[] quadCoords = {i*m+j, ((i+1)%n)*m+j, ((i+1)%n)*m+((j+1)%m), 
          i*m+((j+1)%m)};
        if (i < n-1)
        qa.setCoordinateIndices(quadIndex, quadCoords);
        quadIndex += 4;
      }
    }
    GeometryInfo gi = new GeometryInfo(qa);
    NormalGenerator ng = new NormalGenerator();
    ng.generateNormals(gi);
    return gi.getGeometryArray();
  }
}

