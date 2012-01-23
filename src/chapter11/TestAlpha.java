
package chapter11;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.media.j3d.*;

public class TestAlpha extends JApplet {
  public static void main(String s[]) {
    JFrame frame = new JFrame();
    frame.setTitle("Alpha");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JApplet applet = new TestAlpha();
    applet.init();
    frame.getContentPane().add(applet);
    frame.pack();
    frame.setVisible(true);
  }

  Alpha alpha = new Alpha();
  Plot plot;
  JTextField tfLoopCount;
  JTextField tfTriggerTime;
  JTextField tfAlphaAtZeroDuration;
  JTextField tfAlphaAtOneDuration;
  JTextField tfIncreasingAlphaDuration;
  JTextField tfDecreasingAlphaDuration;
  JTextField tfIncreasingAlphaRampDuration;
  JTextField tfDecreasingAlphaRampDuration;
  
  public void init() {
    Container cp = this.getContentPane();
    cp.setLayout(new BorderLayout());
    plot = new Plot();
    cp.add(plot, BorderLayout.CENTER);
    JPanel p = new JPanel();
    p.setBorder(BorderFactory.createTitledBorder("Alpha parameters"));
    cp.add(p, BorderLayout.SOUTH);
    p.setLayout(new GridLayout(5, 4, 10, 5));
    p.add(new JLabel("loopCount"));
    tfLoopCount = new JTextField("-1");
    p.add(tfLoopCount);
    p.add(new JLabel("triggerTime"));
    tfTriggerTime = new JTextField("0");
    p.add(tfTriggerTime);
    p.add(new JLabel("alphaAtZeroDuration"));
    tfAlphaAtZeroDuration = new JTextField("0");
    p.add(tfAlphaAtZeroDuration);
    p.add(new JLabel("alphaAtOneDuration"));
    tfAlphaAtOneDuration = new JTextField("0");
    p.add(tfAlphaAtOneDuration);
    p.add(new JLabel("increasingAlphaDuration"));
    tfIncreasingAlphaDuration = new JTextField("1000");
    p.add(tfIncreasingAlphaDuration);
    p.add(new JLabel("decreasingAlphaDuration"));
    tfDecreasingAlphaDuration = new JTextField("0");
    p.add(tfDecreasingAlphaDuration);
    p.add(new JLabel("increasingAlphaRampDuration"));
    tfIncreasingAlphaRampDuration = new JTextField("0");
    p.add(tfIncreasingAlphaRampDuration);
    p.add(new JLabel("decreasingAlphaRampDuration"));
    tfDecreasingAlphaRampDuration = new JTextField("0");
    p.add(tfDecreasingAlphaRampDuration);
    p.add(new JPanel());
    JButton button = new JButton("Plot");
    p.add(button);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        setAlpha();
        repaint();
      }
    });
    p.add(new JPanel());
    button = new JButton("Reset");
    p.add(button);
    button.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ev) {
        tfLoopCount.setText("-1");
        tfTriggerTime.setText("0");
        tfAlphaAtZeroDuration.setText("0");
        tfAlphaAtOneDuration.setText("0");
        tfIncreasingAlphaDuration.setText("1000");
        tfDecreasingAlphaDuration.setText("0");
        tfIncreasingAlphaRampDuration.setText("0");
        tfDecreasingAlphaRampDuration.setText("0");
        setAlpha();
        repaint();
      }
    });
  }
  
  void setAlpha() {
    alpha.setMode(Alpha.INCREASING_ENABLE | Alpha.DECREASING_ENABLE);
    int n = Integer.parseInt(tfLoopCount.getText());
    alpha.setLoopCount(n);
    n = Integer.parseInt(tfTriggerTime.getText());
    alpha.setTriggerTime(n);
    n = Integer.parseInt(tfAlphaAtZeroDuration.getText());
    alpha.setAlphaAtZeroDuration(n);
    n = Integer.parseInt(tfAlphaAtOneDuration.getText());
    alpha.setAlphaAtOneDuration(n);
    n = Integer.parseInt(tfIncreasingAlphaDuration.getText());
    alpha.setIncreasingAlphaDuration(n);
    n = Integer.parseInt(tfDecreasingAlphaDuration.getText());
    alpha.setDecreasingAlphaDuration(n);
    n = Integer.parseInt(tfIncreasingAlphaRampDuration.getText());
    alpha.setIncreasingAlphaRampDuration(n);
    n = Integer.parseInt(tfDecreasingAlphaRampDuration.getText());
    alpha.setDecreasingAlphaRampDuration(n);
  }

  class Plot extends JPanel {
    public Plot() {
      this.setBackground(Color.white);
      this.setBorder(BorderFactory.createLoweredBevelBorder());
      this.setPreferredSize(new Dimension(800,200));
    }
    
    public void paintComponent(Graphics g) {
      super.paintComponent(g);
      long start = alpha.getStartTime();
      int x1 = 0;
      int y1 = 150;
      int x2 = 0;
      int y2 = 0;
      for (int i = 1; i < 1000; i++) {
        x2 = i;
        y2 = 150-(int)(100*alpha.value(start+i*10));
        g.drawLine(x1, y1,x2, y2);
        x1 = x2;
        y1 = y2;
      }
    }
  }
}
