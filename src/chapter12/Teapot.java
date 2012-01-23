package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;
import java.net.URL;
import java.io.*;
import java.util.StringTokenizer;

public class Teapot extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Teapot(), 640, 480);
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
    // teapot data
    int n = 0;
    int[][] idx = null;
    int np = 0;
    Point3d[] pts = null;
    URL url = getClass().getClassLoader().getResource("images/teapot");
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
      String line = br.readLine();
      n = Integer.parseInt(line);
      idx = new int[n][16];
      for (int i = 0; i < n; i++) {
          line = br.readLine();
          StringTokenizer st = new StringTokenizer(line, ", \n");
          for (int j = 0; j < 16; j++) {
              idx[i][j] = Integer.parseInt(st.nextToken());
          }
      }
      line = br.readLine();
      np = Integer.parseInt(line);
      pts = new Point3d[np];
      for (int i = 0; i < np; i++) {
          line = br.readLine();
          StringTokenizer st = new StringTokenizer(line, ", \n");
          double x = Double.parseDouble(st.nextToken());
          double y = Double.parseDouble(st.nextToken());
          double z = Double.parseDouble(st.nextToken());
          pts[i] = new Point3d(x, y, z);
      }
      br.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }

    // surface
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Transform3D tr = new Transform3D();
    tr.rotX(-Math.PI*0.5);
    tr.setScale(0.25);
    tr.setTranslation(new Vector3d(0,-0.5,0));
    TransformGroup tg = new TransformGroup(tr);
    spin.addChild(tg);
    Point3d[][] ctrlPts = new Point3d[4][4];
    for (int k = 0; k < n; k++) {
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          ctrlPts[i][j] = pts[idx[k][i+4*j]-1];
        }
      }
      Shape3D shape = new BezierSurface(ctrlPts);
      shape.setAppearance(ap);
      tg.addChild(shape);
    }
    // rotation interpolator
    Alpha alpha = new Alpha(-1, 10000);
    RotationInterpolator rotator = new RotationInterpolator(alpha, spin);
    BoundingSphere bounds = new BoundingSphere();
    bounds.setRadius(10);
    rotator.setSchedulingBounds(bounds);
    spin.addChild(rotator);
    // background and lights
    Background background = new Background(1f, 1f, 1f);
    background.setApplicationBounds(bounds);
    root.addChild(background);
    AmbientLight light = new AmbientLight(true, new Color3f(Color.white));
    light.setInfluencingBounds(bounds);
    root.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white),
    new Point3f(0.7f,1.8f,1.8f), new Point3f(1f,0.2f,0f));
    ptlight.setInfluencingBounds(bounds);
    root.addChild(ptlight);
    return root;
  }
}


