
package appendixB;

import java.awt.*;
import java.awt.event.*;

public class AWTProg extends Frame implements ActionListener, AdjustmentListener {
  
  public static void main(String[] args) {
    Frame frame = new AWTProg();
    frame.setVisible(true);
  }
  
  CircleCanvas circle;
  Scrollbar scrollR;
  Scrollbar scrollG;
  Scrollbar scrollB;
  
  public AWTProg() {
    setSize(500,350);
    MenuBar menuBar = new MenuBar();
    setMenuBar(menuBar);
    Menu fileMenu = new Menu("File");
    menuBar.add(fileMenu);
    MenuItem exitItem = new MenuItem("Exit");
    exitItem.addActionListener(this);
    fileMenu.add(exitItem);
    // exit when the frame is closed
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent ev){
        System.exit(0);
      }
    });
    setLayout(new BorderLayout());
    // center
    Panel panel = new Panel();
    panel.setLayout(new BorderLayout());
    add(panel, BorderLayout.CENTER);
    circle = new CircleCanvas();
    panel.add(circle, BorderLayout.CENTER);
    TextArea textArea = new TextArea(3,10);
    textArea.setText("TextArea");
    panel.add(textArea, BorderLayout.SOUTH);
    // north
    Label label = new Label("Label");
    add(label, BorderLayout.NORTH);
    // south
    TextField textField = new TextField("TextField");
    add(textField, BorderLayout.SOUTH);
    // west
    panel = new Panel();
    panel.setLayout(new GridLayout(1,3));
    add(panel, BorderLayout.WEST);
    scrollR = new Scrollbar(Scrollbar.VERTICAL,0,1,0,255);
    scrollR.addAdjustmentListener(this);
    panel.add(scrollR);
    scrollG = new Scrollbar(Scrollbar.VERTICAL,0,1,0,255);
    scrollG.addAdjustmentListener(this);
    panel.add(scrollG);
    scrollB = new Scrollbar(Scrollbar.VERTICAL,0,1,0,255);
    scrollB.addAdjustmentListener(this);
    panel.add(scrollB);
    // east
    panel = new Panel();
    panel.setBackground(Color.lightGray);
    panel.setLayout(new GridLayout(4,1));
    add(panel, BorderLayout.EAST);
    Panel chPanel = new Panel();
    panel.add(chPanel);
    Choice choice = new Choice();
    choice.add("red");
    choice.add("green");
    choice.add("blue");
    chPanel.add(choice);
    Panel cbPanel = new Panel();
    cbPanel.setLayout(new GridLayout(3,1));
    panel.add(cbPanel);
    CheckboxGroup group = new CheckboxGroup();
    Checkbox cbR = new Checkbox("Red", group, true);
    cbPanel.add(cbR);
    Checkbox cbG = new Checkbox("Green", group, false);
    cbPanel.add(cbG);
    Checkbox cbB = new Checkbox("Blue", group, false);
    cbPanel.add(cbB);
    Panel btPanel = new Panel();
    panel.add(btPanel);
    Button button = new Button("Exit");
    button.addActionListener(this);
    btPanel.add(button);
    List list = new List(3);
    list.add("Red");
    list.add("Green");
    list.add("Blue");
    panel.add(list);
  }
  
  public void actionPerformed(ActionEvent ev) {
    String cmd = ev.getActionCommand();
    if ("Exit".equals(cmd))
      System.exit(0);
  }
  
  public void adjustmentValueChanged(AdjustmentEvent ev) {
    int r = scrollR.getValue();
    int g = scrollG.getValue();
    int b = scrollB.getValue();
    circle.setColor(new Color(r, g, b));
  }
}

class CircleCanvas extends Canvas {
  private Color color = Color.black;
  
  public CircleCanvas() {
    setBackground(new Color(220,220,220));
  }
  
  public void paint(Graphics g) {
    super.paint(g);
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
