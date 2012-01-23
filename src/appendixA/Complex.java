
package appendixA;

public class Complex {
  public static void main(String[] args) {
    Complex z = new Complex(1, -2);
    Complex w = new Complex(3, 4);
    System.out.println("z = " + z);
    System.out.println("w = " + w);
    System.out.println("|z| = " + z.abs());
    System.out.println("|w| = " + w.abs());
    System.out.println("arg z = " + z.arg());
    System.out.println("arg w = " + w.arg());
    System.out.println("conj z = " + z.conj());
    System.out.println("conj w = " + w.conj());
    System.out.println("z + w = " + z.add(w));
    System.out.println("z - w = " + z.sub(w));
    System.out.println("z * w = " + z.mul(w));
    System.out.println("z / w = " + z.div(w));
  }
  
  private double x = 0.0;
  private double y = 0.0;
  
  public Complex() {
  }
  
  public Complex(double x, double y) {
    this.x = x;
    this.y = y;
  }
  
  public double getX() {
    return x;
  }

  public double getY() {
    return y;
  }
  
  public void setX(double x) {
    this.x = x;
  }
  
  public void setY(double y) {
    this.y = y;
  }
  
  public double abs() {
    return Math.sqrt(x*x + y*y);
  }
  
  public double arg() {
    return Math.atan2(y, x);
  }
  
  public Complex conj() {
    return new Complex(x, -y);
  }

  public Complex add(Complex other) {
    double a = x + other.getX();
    double b = y + other.getY();
    return new Complex(a, b);
  }
  
  public Complex sub(Complex other) {
    double a = x - other.getX();
    double b = y - other.getY();
    return new Complex(a, b);
  }
  
  public Complex mul(Complex other) {
    double a = x * other.getX() - y * other.getY();
    double b = x * other.getY() + y * other.getX();
    return new Complex(a, b);
  }
  
  public Complex div(Complex other) {
    double a = x * other.getX() + y * other.getY();
    double b = -x * other.getY() + y * other.getX();
    double d = other.abs();
    d = d * d;
    return new Complex(a/d, b/d);
  }
  
  public String toString() {
    if (y >= 0)
      return "" + x + "+" + y + "i";
    else
      return "" + x + "" + y + "i";      
  }
}
