package chapter12;

import javax.vecmath.*;

public class PerlinNoise {
  final static int B = 0x100;
  int[] p = new int[2*B+2];
  Vector3d[] g3 = new Vector3d[2*B+2];
  
  public PerlinNoise() {
    for (int i = 0; i < B; i++) {
      p[i] = i;
      double x = 2.0*Math.random() - 1.0;
      double y = 2.0*Math.random() - 1.0;
      double z = 2.0*Math.random() - 1.0;
      g3[i] = new Vector3d(x, y, z);
      g3[i].normalize();
    }
    for (int i = 0; i < B; i++) {
      int k = p[i];
      int j = (int)(Math.random()*B);
      if (j >= B) j = B-1;
      p[i] = p[j];
      p[j] = k;
    }
    for (int i = 0; i < B+2; i++) {
      p[B+i] = p[i];
      g3[B+i] = g3[i];
    }
  }
  
  public double noise(Point3d point) {
    int bx0, bx1, by0, by1, bz0, bz1, b00, b10, b01, b11;
    double rx0, rx1, ry0, ry1, rz0, rz1, sy, sz, a, b, c, d, t, u, v;
    Vector3d q = null;
    int i, j;
    
    t = point.x + 0x1000;
    bx0 = ((int)t) & 0xff;
    bx1 = (bx0+1) & 0xff;
    rx0 = t-(int)t;
    rx1 = rx0-1;
    
    t = point.y + 0x1000;
    by0 = ((int)t) & 0xff;
    by1 = (by0+1) & 0xff;
    ry0 = t-(int)t;
    ry1 = ry0-1;

    t = point.z + 0x1000;
    bz0 = ((int)t) & 0xff;
    bz1 = (bz0+1) & 0xff;
    rz0 = t-(int)t;
    rz1 = rz0-1;

    i = p[bx0];
    j = p[bx1];
    
    b00 = p[i+by0];
    b10 = p[j+by0];
    b01 = p[i+by1];
    b11 = p[j+by1];
    
    t = rx0*rx0*(3-2*rx0);
    sy = ry0*ry0*(3-2*ry0);
    sz = rz0*rz0*(3-2*rz0);
    
    q = g3[b00+bz0]; u = rx0*q.x + ry0*q.y + rz0*q.z;
    q = g3[b10+bz0]; v = rx1*q.x + ry0*q.y + rz0*q.z;
    a = u + t*(v-u);
    
    q = g3[b01+bz0]; u = rx0*q.x + ry1*q.y + rz0*q.z;
    q = g3[b11+bz0]; v = rx1*q.x + ry1*q.y + rz0*q.z;
    b = u + t*(v-u);
    
    c = a + sy*(b-a);
    
    q = g3[b00+bz1]; u = rx0*q.x + ry0*q.y + rz1*q.z;
    q = g3[b10+bz1]; v = rx1*q.x + ry0*q.y + rz1*q.z;
    a = u + t*(v-u);
    
    q = g3[b01+bz1]; u = rx0*q.x + ry1*q.y + rz1*q.z;
    q = g3[b11+bz1]; v = rx1*q.x + ry1*q.y + rz1*q.z;
    b = u + t*(v-u);
    
    d = a + sy*(b-a);
    
    return c+sz*(d-c);
  }
  
  public double perlinNoise(Point3d pt, double alpha, double beta, int n) {
    double val;
    double sum = 0;
    double scale = 1;
    
    for (int i = 0; i < n; i++) {
      val = noise(pt);
      sum += val / scale;
      scale *= alpha;
      pt.scale(beta);
    }
    return sum;
  }

  public double turbulence(Point3d pt, double alpha, double beta, int n) {
    double val;
    double sum = 0;
    double scale = 1;
    
    for (int i = 0; i < n; i++) {
      val = noise(pt);
      sum += Math.abs(val) / scale;
      scale *= alpha;
      pt.scale(beta);
    }
    return sum;
  }
}
