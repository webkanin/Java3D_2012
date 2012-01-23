package chapter4;

import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;

public class Heart implements Shape {
  GeneralPath path;

  public Heart(float x, float y, float w, float h) {
    path = new GeneralPath();
    float x0 = x + 0.5f*w;
    float y0 = y + 0.3f*h;
    float x1 = x + 0.1f*w;
    float y1 = y + 0f * h;
    float x2 = x + 0f * w;
    float y2 = y + 0.6f * h;
    float x3 = x + 0.5f * w;
    float y3 = y + 0.9f * h;
    float x4 = x + 1f * w;
    float y4 = y + 0.6f * h;
    float x5 = x + 0.9f * w;
    float y5 = y + 0f * h;
    path.moveTo(x0, y0);
    path.curveTo(x1, y1, x2, y2, x3, y3);
    path.curveTo(x4, y4, x5, y5, x0, y0);
  }

  public boolean contains(Rectangle2D rect) {
    return path.contains(rect);
  }

  public boolean contains(Point2D point) {
    return path.contains(point);
  }

  public boolean contains(double x, double y) {
    return path.contains(x, y);
  }

  public boolean contains(double x, double y, double w, double h) {
    return path.contains(x, y, w, h);
  }

  public Rectangle getBounds() {
    return path.getBounds();
  }

  public Rectangle2D getBounds2D() {
    return path.getBounds2D();
  }

  public PathIterator getPathIterator(AffineTransform at) {
    return path.getPathIterator(at);
  }

  public PathIterator getPathIterator(AffineTransform at, double flatness) {
    return path.getPathIterator(at, flatness);
  }

  public boolean intersects(Rectangle2D rect) {
    return path.intersects(rect);
  }

  public boolean intersects(double x, double y, double w, double h) {
    return path.intersects(x, y, w, h);
  }
}
