package chapter7;

import javax.vecmath.*;
import javax.media.j3d.*;

public class Arrow extends IndexedTriangleArray {
  float w = 1f;
  float h = 0.15f;
  float d = 0.1f;
  
  public Arrow() {
    super(5, TriangleArray.COORDINATES | TriangleArray.NORMALS, 12);
    Point3f[] pts = {new Point3f(0f,0f,d),
      new Point3f(w,0f,0f),
      new Point3f(h,h,0f),
      new Point3f(h,-h,0f),
      new Point3f(0f,0f,-d)};
    setCoordinates(0, pts);
    int[] coords = {0,1,2,0,3,1,4,1,3,4,2,1};
    setCoordinateIndices(0, coords);
    Vector3f v1 = new Vector3f();
    v1.sub(pts[1], pts[0]);
    v1.normalize();
    Vector3f v2 = new Vector3f();
    v2.sub(pts[2], pts[0]);
    v2.normalize();
    Vector3f v = new Vector3f();
    v.cross(v1, v2);
    setNormal(0, v);
    v.y = -v.y;
    setNormal(1, v);
    v.z = -v.z;
    setNormal(2, v);
    v.y = -v.y;
    setNormal(3, v);
    int[] norms = {0,0,0,1,1,1,2,2,2,3,3,3};
    setNormalIndices(0, norms);
  }
}