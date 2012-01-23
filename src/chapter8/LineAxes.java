
package chapter8;

import javax.media.j3d.*;
import javax.vecmath.*;

public class LineAxes extends LineArray{
  public LineAxes(float len) {
    super(6, LineArray.COORDINATES | LineArray.COLOR_3);
    setCoordinate(0, new Point3f(-len,0f,0f));
    setCoordinate(1, new Point3f(len,0,0f));
    setCoordinate(2, new Point3f(0f,-len,0f));
    setCoordinate(3, new Point3f(0f,len,0f));
    setCoordinate(4, new Point3f(0f,0f,-len));
    setCoordinate(5, new Point3f(0f,0f,len));
    Color3f c0 = new Color3f(0f, 0f, 0f);
    Color3f c1 = new Color3f(1f, 0f, 0f);
    Color3f c2 = new Color3f(0f, 1f, 0f);
    Color3f c3 = new Color3f(0f, 0f, 1f);
    setColor(0, c0);
    setColor(1, c1);
    setColor(2, c0);
    setColor(3, c2);
    setColor(4, c0);
    setColor(5, c3);
  }  
}
