
package chapter11;

import javax.media.j3d.*;

public class MorphingBehavior extends Behavior {
  Morph morph;
  Alpha alpha;

  public MorphingBehavior(Morph m, Alpha a) {
    morph = m;
    alpha = a;
  }

  public void initialize() {
    wakeupOn(new WakeupOnElapsedFrames(10));
  }

  public void processStimulus(java.util.Enumeration enumeration) {
    double[] w = new double[4];
    double a = alpha.value();
    w[0] = 0;
    w[1] = 0;
    w[2] = 0;
    w[3] = 0;
    int index = (int)(a*3);
    if (index > 2) index = 2;
    w[index+1] = (a-index/3.0)*3;
    w[index] = 1.0-w[index+1];
    morph.setWeights(w);
    wakeupOn(new WakeupOnElapsedFrames(10));
  }
}
