package chapter2;
// Rev.: Knut Saua Mathiesen, 21.01.2009.

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.geom.*;

public class CustomPath extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("GeneralPath and Winding Rules");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new CustomPath();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }
  
  public void init() {
    JPanel panel = new PathPanel();
    getContentPane().add(panel);
  }
}

class PathPanel extends JPanel {
  public PathPanel() {
    setPreferredSize(new Dimension(640, 480));
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D)g;
    GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD);
    
    float x0 = 1.0f;
    float y0 = 0.0f;
    float x1 = (float)Math.cos(2*Math.PI/5.0);
    float y1 = (float)Math.sin(2*Math.PI/5.0);
    float x2 = (float)Math.cos(4*Math.PI/5.0);
    float y2 = (float)Math.sin(4*Math.PI/5.0);
    float x3 = (float)Math.cos(6*Math.PI/5.0);
    float y3 = (float)Math.sin(6*Math.PI/5.0);
    float x4 = (float)Math.cos(8*Math.PI/5.0);
    float y4 = (float)Math.sin(8*Math.PI/5.0);
    path.moveTo(x2,y2);
    path.lineTo(x0,y0);
    path.lineTo(x3,y3);
    path.lineTo(x1,y1);
    path.lineTo(x4,y4);
    path.closePath();
    AffineTransform tr = new AffineTransform();
    tr.setToScale(100,100);
    g2.translate(120,120);

    // tr.createTransformedShape returnerer en Shape og ikke GenerelPath, som den gamle koden ville ha
    // Dette ga iallfall meg og noen andre en exception.
    
    // Det jeg har gjort er å lage en Shape i stedenfor som jeg kaller pathshape.
    // For hver gang path endres settes pathshape på nytt. 
    // Tilslutt har jeg bare endret i fra den gamle variabelen path til den nye pathshape på alle plasser den skal tegnes.
    Shape pathshape; // ekstra variabel
    pathshape = (Shape)tr.createTransformedShape(path); // endret
    g2.draw(pathshape); // endret
    g2.translate(200,0);
    g2.fill(pathshape); // endret
    path.setWindingRule(GeneralPath.WIND_NON_ZERO);
    pathshape = (Shape)tr.createTransformedShape(path); // Lagt til
    g2.translate(200,0);
    g2.fill(pathshape); // endret

    path.reset();
    path.moveTo(x0, y0);
    path.lineTo(x1, y1);
    path.lineTo(x2, y2);
    path.lineTo(x3, y3);
    path.lineTo(x4, y4);
    path.closePath();
    path.moveTo(x0, y0);
    path.quadTo(x4, y4, x1, y1);
    path.quadTo(x2, y2, x3, y3);
    path.closePath();
    path.moveTo(x4,y4);
    path.curveTo(x1,y1,x3,y3,x2,y2);
    path.curveTo(x1,y1,x3,y3,x4,y4);
    pathshape = (Shape)tr.createTransformedShape(path); // endret
    g2.translate(-400,220);
    g2.draw(pathshape); // endret
    path.setWindingRule(GeneralPath.WIND_EVEN_ODD);
    pathshape = (Shape)tr.createTransformedShape(path); // Lagt til
    g2.translate(200,0);
    g2.fill(pathshape); // endret
    path.setWindingRule(GeneralPath.WIND_NON_ZERO);
    pathshape = (Shape)tr.createTransformedShape(path); // Lagt til
    g2.translate(200,0);
    g2.fill(pathshape); // endret
  }
}
