package chapter6;

import javax.vecmath.*;
import javax.media.j3d.*;

public class ColorTetrahedron extends IndexedTriangleArray{
  public ColorTetrahedron() {
    super(4, TriangleArray.COORDINATES | TriangleArray.NORMALS |
          GeometryArray.COLOR_3, 12);
    setCoordinate(0, new Point3f(1f,1f,1f));
    setCoordinate(1, new Point3f(1f,-1,-1f));
    setCoordinate(2, new Point3f(-1f,1f,-1f));
    setCoordinate(3, new Point3f(-1f,-1f,1f));
    int[] coords = {0,1,2,0,3,1,1,3,2,2,3,0};
    float n = (float)(1.0/Math.sqrt(3));
    setNormal(0, new Vector3f(n,n,-n));
    setNormal(1, new Vector3f(n,-n,n));
    setNormal(2, new Vector3f(-n,-n,-n));
    setNormal(3, new Vector3f(-n,n,n));
    int[] norms = {0,0,0,1,1,1,2,2,2,3,3,3};
    setCoordinateIndices(0, coords);
    setNormalIndices(0, norms);
    setColor(0, new Color3f(1f, 0f, 0f));
    setColor(1, new Color3f(0f, 1f, 0f));
    setColor(2, new Color3f(0f, 0f, 1f));
    setColor(3, new Color3f(1f, 1f, 1f));
    setColorIndices(0, coords);
  }
}