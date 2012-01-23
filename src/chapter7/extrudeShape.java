           /**
package chapter7;

import javax.vecmath.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.geometry.*;



public class extrudeShape  {

  // assume the Shape has a single continuous curve
  Geometry extrudeShape(Shape curve, float depth) {
    PathIterator iter = curve.getPathIterator(new AffineTransform());
    Vector ptsList = new Vector();
    float[] seg = new float[6];
    float x = 0, y = 0;
    float x0 = 0, y0 = 0;

    while (!iter.isDone()) {
      int segType = iter.currentSegment(seg);
      switch (segType) {
        case PathIterator.SEG_MOVETO:
          x = x0 = seg[0];
          y = y0 = seg[1];
          ptsList.add(new Point3f(x,y,0));
          break;
        case PathIterator.SEG_LINETO:
          x = seg[0];
          y = seg[1];
          ptsList.add(new Point3f(x,y,0));
          break;
        case PathIterator.SEG_QUADTO:
          for (int i = 1; i < 10; i++) {
            float t = (float)i/10f;
            float xi = (1-t)*(1-t)*x + 2*t*(1-t)*seg[0] + t*t*seg[2];
            float yi = (1-t)*(1-t)*y + 2*t*(1-t)*seg[1] + t*t*seg[3];
            ptsList.add(new Point3f(xi,yi,0));
          }
          x = seg[2];
          y = seg[3];
          ptsList.add(new Point3f(x,y,0));
          break;
        case PathIterator.SEG_CUBICTO:
          for (int i = 1; i < 20; i++) {
            float t = (float)i/20f;
            float xi = (1-t)*(1-t)*(1-t)*x + 3*t*(1-t)*(1-t)*seg[0] +
            3*t*t*(1-t)*seg[2] + t*t*t*seg[4];
            float yi = (1-t)*(1-t)*(1-t)*y + 3*t*(1-t)*(1-t)*seg[1] +
            3*t*t*(1-t)*seg[3] + t*t*t*seg[5];
            ptsList.add(new Point3f(xi,yi,0));
          }
          x = seg[2];
          y = seg[3];
          ptsList.add(new Point3f(x,y,0));
          break;
        case PathIterator.SEG_CLOSE:
          x = x0;
          y = y0;
          ptsList.add(new Point3f(x,y,0));
          break;
      }
      iter.next();
    }
    int n = ptsList.size();
    IndexedQuadArray qa = new IndexedQuadArray(2*n,
    IndexedQuadArray.COORDINATES, 4*(n-1));
    Transform3D trans = new Transform3D();
    trans.setTranslation(new Vector3f(0,0,depth));
    for (int i = 0; i < n; i++) {
      Point3f pt = (Point3f)ptsList.get(i);
      qa.setCoordinate(2*i, pt);
      trans.transform(pt);
      qa.setCoordinate(2*i+1, pt);
    }
    int quadIndex = 0;
    for (int i = 0; i < n-1; i++) {
      qa.setCoordinateIndex(quadIndex++, 2*i);
      qa.setCoordinateIndex(quadIndex++, 2*i+1);
      qa.setCoordinateIndex(quadIndex++, 2*(i+1)+1);
      qa.setCoordinateIndex(quadIndex++, 2*(i+1));
    }
    GeometryInfo gi = new GeometryInfo(qa);
    NormalGenerator ng = new NormalGenerator();
    ng.generateNormals(gi);
    return gi.getGeometryArray();
  }
     }
            */