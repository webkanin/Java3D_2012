package chapter12;

import javax.vecmath.*;
import java.awt.*;
import java.awt.event.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.geometry.*;

public class BezierSurface extends Shape3D{
  public BezierSurface(Point3d[][] ctrlPts) {
    int m = 17;
    int n = 17;
    Point3d[] pts = new Point3d[m*n];
    int idx = 0;
    Point3d[] p = new Point3d[4];
    double du = 1.0/(m-1);
    double dv = 1.0/(n-1);
    double u = 0;
    double v = 0;
    for (int i = 0; i < m; i++) {
      for (int k = 0; k < 4; k++) {
        p[k] = deCasteljau(u, ctrlPts[k]);
      }
      v = 0;
      for (int j = 0; j < n; j++) {
        pts[idx++] = deCasteljau(v, p);
        v += dv;
      }
      u += du;
    }
    
    int[] coords = new int[2*n*(m-1)];
    idx = 0;
    for (int i = 1; i < m; i++) {
      for (int j = 0; j < n; j++) {
        coords[idx++] = i*n + j;
        coords[idx++] = (i-1)*n + j;
      }
    }
    
    int[] stripCounts = new int[m-1];
    for (int i = 0; i < m-1; i++) stripCounts[i] = 2*n;
    
    GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_STRIP_ARRAY);
    gi.setCoordinates(pts);
    gi.setCoordinateIndices(coords);
    gi.setStripCounts(stripCounts);
    
    NormalGenerator ng = new NormalGenerator();
    ng.generateNormals(gi);
    this.setGeometry(gi.getGeometryArray());
  }
  
  Point3d deCasteljau(double t, Point3d[] p) {
    Point3d[] pt = {new Point3d(p[0]),new Point3d(p[1]),new Point3d(p[2]), new Point3d(p[3])};
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3-i; j++) {
        pt[j].interpolate(pt[j+1], t);
      }
    }
    return pt[0];
  } 
}

