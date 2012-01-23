
package chapter10;

import java.util.*;
import javax.media.j3d.*;

public class ClockBehavior extends Behavior {
  TransformGroup tgH;
  TransformGroup tgM;
  TransformGroup tgS;
  
  public ClockBehavior(TransformGroup transH, TransformGroup transM, TransformGroup transS) {
    tgH = transH;
    tgM = transM;
    tgS = transS;
  }
  
  public void initialize() {
    wakeupOn(new WakeupOnElapsedTime(500));
  }
  
  public void processStimulus(java.util.Enumeration enumeration) {
    int hour = Calendar.getInstance().get(Calendar.HOUR);
    int min = Calendar.getInstance().get(Calendar.MINUTE);
    int sec = Calendar.getInstance().get(Calendar.SECOND);
    Transform3D tr = new Transform3D();
    tr.rotZ(-Math.PI * (hour+min/60.0)/6.0);
    tgH.setTransform(tr);
    tr.rotZ(-Math.PI * min /30.0);
    tgM.setTransform(tr);
    tr.rotZ(-Math.PI * sec /30.0);
    tgS.setTransform(tr);
    wakeupOn(new WakeupOnElapsedTime(500));
  }
}
