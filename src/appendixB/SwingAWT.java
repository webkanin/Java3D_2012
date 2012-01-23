
package appendixB;

import javax.swing.*;
import java.awt.*;

public class SwingAWT extends JFrame {
  public static void main(String[] args) {
    JFrame frame = new SwingAWT();
    frame.setSize(300, 200);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }
  
  public SwingAWT() {
    //set up menu
    JMenuBar mb = new JMenuBar();
    setJMenuBar(mb);
    JMenu menu = new JMenu("File");
    mb.add(menu);
    menu.add(new JMenuItem("Open"));
    menu.add(new JMenuItem("Save"));
    menu.addSeparator();
    menu.add(new JMenuItem("Exit"));
    //add content
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    JSplitPane sp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    JPanel panel = new JPanel();
    sp.add(panel);
    cp.add(sp);
    sp.add(new TextArea("AWT TextArea",10,10));
  }  
}
