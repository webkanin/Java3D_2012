package chapter7;

import java.awt.*;
import java.awt.event.*;
import javax.vecmath.*;
import java.applet.*;
import com.sun.j3d.utils.applet.MainFrame;

public class TestMatrix extends Applet implements ActionListener {
  public static void main(String[] args) {
    new MainFrame(new TestMatrix(), 600, 200);
  }
  
  MatrixPanel mp;
  Matrix4d m = new Matrix4d();
  TextField tf;
  
  public void init() {
    this.setLayout(new BorderLayout());
    
    mp = new MatrixPanel();
    add(mp, BorderLayout.CENTER);
    
    Panel p = new Panel();
    p.setLayout(new GridLayout(6,1));
    Button button = new Button("Identity");
    button.addActionListener(this);
    p.add(button);
    button = new Button("Zero");
    button.addActionListener(this);
    p.add(button);
    button = new Button("Negate");
    button.addActionListener(this);
    p.add(button);
    button = new Button("Transpose");
    button.addActionListener(this);
    p.add(button);
    button = new Button("Invert");
    button.addActionListener(this);
    p.add(button);
    button = new Button("Determinant");
    button.addActionListener(this);
    p.add(button);
    this.add(p, BorderLayout.EAST);
    
    tf = new TextField();
    add(tf, BorderLayout.SOUTH);
  }
  
  public void actionPerformed(ActionEvent e) {
    String cmd = e.getActionCommand();
    if ("Identity".equals(cmd)) {
      mp.get(m);
      m.setIdentity();
      mp.set(m);
    } else if ("Zero".equals(cmd)) {
      mp.get(m);
      m.setZero();
      mp.set(m);
    } else if ("Negate".equals(cmd)) {
      mp.get(m);
      m.negate();
      mp.set(m);
    } else if ("Transpose".equals(cmd)) {
      mp.get(m);
      m.transpose();
      mp.set(m);
    } else if ("Invert".equals(cmd)) {
      mp.get(m);
      m.invert();
      mp.set(m);
    } else if ("Determinant".equals(cmd)) {
      mp.get(m);
      tf.setText("" + m.determinant());
    }      
  }
}