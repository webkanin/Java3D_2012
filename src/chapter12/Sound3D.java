package chapter12;

import java.net.URL;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.io.*;
import javax.imageio.*;
import com.sun.j3d.utils.behaviors.mouse.*;
import chapter10.GullCG;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class Sound3D extends Applet {
  public static void main(String[] args) {
    new MainFrame(new Sound3D(), 640, 480);
  }

  public void init() {
    // create canvas
    GraphicsConfiguration gc = SimpleUniverse.getPreferredConfiguration();
    Canvas3D cv = new Canvas3D(gc);
    setLayout(new BorderLayout());
    add(cv, BorderLayout.CENTER);
    SimpleUniverse su = new SimpleUniverse(cv);
    AudioDevice audioDev = su.getViewer().createAudioDevice();
    BranchGroup bg = createSceneGraph();
    bg.compile();
    su.getViewingPlatform().setNominalViewingTransform();    
    su.addBranchGraph(bg);
  }
  
  public BranchGroup createSceneGraph() {
    // root
    BranchGroup objRoot = new BranchGroup();
    Transform3D trans = new Transform3D();
    trans.setTranslation(new Vector3d(Math.random()-0.5, Math.random()-0.5,
      Math.random()-0.5));
    trans.setScale(0.3);
    TransformGroup objTrans = new TransformGroup(trans);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
    objTrans.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
    objRoot.addChild(objTrans);
    // visual object
    Appearance ap = new Appearance();
    ap.setMaterial(new Material());
    Shape3D shape = new Shape3D(new GullCG(), ap);
    objTrans.addChild(shape);
    // behaviors
    BoundingSphere bounds =
    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    // rotation
    MouseRotate rotator = new MouseRotate(objTrans);
    rotator.setSchedulingBounds(bounds);
    objRoot.addChild(rotator);
    // translation
    MouseTranslate translator = new MouseTranslate(objTrans);
    translator.setSchedulingBounds(bounds);
    objTrans.addChild(translator);
    // zoom
    MouseZoom zoom = new MouseZoom(objTrans);
    zoom.setSchedulingBounds(bounds);
    objTrans.addChild(zoom);
    // sound
    PointSound sound = new PointSound();
    URL url = this.getClass().getClassLoader().getResource("images/bird.au");
    MediaContainer mc = new MediaContainer(url);
    sound.setSoundData(mc);
    sound.setLoop(Sound.INFINITE_LOOPS);
    sound.setInitialGain(1f);
    sound.setEnable(true);
    float[] distances = {1f, 20f};
    float[] gains = {1f, 0.001f};
    sound.setDistanceGain(distances, gains);
    BoundingSphere soundBounds =
    new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
    sound.setSchedulingBounds(soundBounds);
    objTrans.addChild(sound);
    //light
    AmbientLight light = new AmbientLight(true, new Color3f(Color.blue));
    light.setInfluencingBounds(bounds);
    objRoot.addChild(light);
    PointLight ptlight = new PointLight(new Color3f(Color.white), 
      new Point3f(0f,0f,2f), new Point3f(1f,0.3f,0f));
    ptlight.setInfluencingBounds(bounds);
    objRoot.addChild(ptlight);
    //background
    url = getClass().getClassLoader().getResource("images/bg.jpg");
    BufferedImage bi = null;
    try {
      bi = ImageIO.read(url);
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    ImageComponent2D image = new ImageComponent2D(ImageComponent2D.FORMAT_RGB, bi);
    Background background = new Background(image);
    background.setApplicationBounds(bounds);
    objRoot.addChild(background);
    return objRoot;
  }
}
