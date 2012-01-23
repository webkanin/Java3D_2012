
package appendixB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;

public class SwingProg extends JFrame implements ActionListener, ChangeListener {
  
  public static void main(String[] args) {
    JFrame frame = new SwingProg();
    frame.setVisible(true);
  }
  
  CirclePanel circle;
  JSlider scrollR;
  JSlider scrollG;
  JSlider scrollB;
  
  public SwingProg() {
    setSize(500,350);
    JMenuBar menuBar = new JMenuBar();
    setJMenuBar(menuBar);
    JMenu fileMenu = new JMenu("File");
    menuBar.add(fileMenu);
    JMenuItem exitItem = new JMenuItem("Exit");
    exitItem.addActionListener(this);
    fileMenu.add(exitItem);
    // exit when the frame is closed
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    // center
    JPanel panel = new JPanel();
    panel.setLayout(new BorderLayout());
    cp.add(panel, BorderLayout.CENTER);
    circle = new CirclePanel();
    panel.add(circle, BorderLayout.CENTER);
    JTextArea textArea = new JTextArea(3,10);
    textArea.setText("TextArea");
    panel.add(textArea, BorderLayout.SOUTH);
    // north
    JLabel label = new JLabel("Label");
    cp.add(label, BorderLayout.NORTH);
    // south
    JTextField textField = new JTextField("TextField");
    cp.add(textField, BorderLayout.SOUTH);
    // west
    panel = new JPanel();
    panel.setLayout(new GridLayout(1,3));
    cp.add(panel, BorderLayout.WEST);
    scrollR = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    scrollR.addChangeListener(this);
    panel.add(scrollR);
    scrollG = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    scrollG.addChangeListener(this);
    panel.add(scrollG);
    scrollB = new JSlider(JSlider.VERTICAL, 0, 255, 0);
    scrollB.addChangeListener(this);
    panel.add(scrollB);
    // east
    panel = new JPanel();
    panel.setBackground(Color.lightGray);
    panel.setLayout(new GridLayout(4,1));
    cp.add(panel, BorderLayout.EAST);
    JPanel chPanel = new JPanel();
    panel.add(chPanel);
    JComboBox choice = new JComboBox();
    choice.addItem("red");
    choice.addItem("green");
    choice.addItem("blue");
    chPanel.add(choice);
    JPanel cbPanel = new JPanel();
    cbPanel.setLayout(new GridLayout(3,1));
    panel.add(cbPanel);
    ButtonGroup group = new ButtonGroup();
    JRadioButton cbR = new JRadioButton("Red", true);
    group.add(cbR);
    cbPanel.add(cbR);
    JRadioButton cbG = new JRadioButton("Green", false);
    group.add(cbG);
    cbPanel.add(cbG);
    JRadioButton cbB = new JRadioButton("Blue", false);
    group.add(cbB);
    cbPanel.add(cbB);
    JPanel btPanel = new JPanel();
    panel.add(btPanel);
    JButton button = new JButton("Exit");
    button.addActionListener(this);
    btPanel.add(button);
    String[] listItems = {"Red", "Green", "Blue"};
    JList list = new JList(listItems);
    panel.add(list);
  }
  
  public void actionPerformed(ActionEvent ev) {
    String cmd = ev.getActionCommand();
    if ("Exit".equals(cmd))
      System.exit(0);
  }
  
  public void stateChanged(ChangeEvent ev) {
    int r = scrollR.getValue();
    int g = scrollG.getValue();
    int b = scrollB.getValue();
    circle.setColor(new Color(r, g, b));
  }
  
}

class CirclePanel extends JPanel {
  private Color color = Color.black;
  
  public CirclePanel() {
    setBackground(new Color(220,220,220));
  }
  
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.setColor(color);
    int w = getWidth();
    int h = getHeight();
    int d = (w > h)? h : w;
    d -= 30;
    g.fillOval((w-d)/2,(h-d)/2, d, d);
  }
  
  public void setColor(Color c) {
    color = c;
    repaint();
  }
}
