package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * TimeEvolutionP
 */
public class TopP extends JPanel {
  private SimulationP simP;
  private boolean timePaused = false;
  private JSlider timeSlider;
  private JSlider scaleSlider;
  private JLabel multLabel;
  private JLabel scaleLabel;
  private String timeMultLabelPrefix = "Current time multiplier: x";
  private String scaleMultLabelPrefix = "Current scale multiplier: x";
  private String pauseString = "⏹ Pause";
  private String resumeString = "▶ Resume";
  private JButton timePauseB;

  /**
   * Constructor for JPanel
   *
   * @param simP Solar system to take information from
   */
  public TopP(SimulationP simP) {
    this.simP = simP;
    setBackground(Color.decode("#1f1f38"));
    SpringLayout springLayout = new SpringLayout();
    setLayout(springLayout);

    // Setup scale slider
    int maxS = 15;
    double curS = simP.getCamera().getScale();
    // Convert value into a slider valuec
    int valS = (int) Math.round(Math.log(curS) / Math.log(1.5));
    scaleSlider = new JSlider(0, maxS, valS);
    scaleSlider.setMajorTickSpacing(5);
    scaleSlider.setMinorTickSpacing(1);
    scaleSlider.setPaintTicks(true);
    scaleSlider.setOpaque(false);
    scaleSlider.setForeground(Color.WHITE);
    scaleSlider.setSnapToTicks(true);
    Dimension sliderDimTop = scaleSlider.getPreferredSize();
    scaleSlider.setPreferredSize(new Dimension((int) sliderDimTop.getWidth() + 50, (int) sliderDimTop.getHeight()));
    springLayout.putConstraint(SpringLayout.WEST, scaleSlider, 100, SpringLayout.WEST, this);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, scaleSlider, 0, SpringLayout.VERTICAL_CENTER, this);
    add(scaleSlider);
    // Slider label
    JLabel sliderLabelScale = new JLabel("Adjust the scale of the planets (Visual change only)");
    sliderLabelScale.setFont(new Font("Dialog", Font.BOLD, 15));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, sliderLabelScale, 0, SpringLayout.HORIZONTAL_CENTER,
        scaleSlider);
    springLayout.putConstraint(SpringLayout.SOUTH, sliderLabelScale, -4, SpringLayout.NORTH, scaleSlider);
    sliderLabelScale.setForeground(Color.WHITE);
    add(sliderLabelScale);
    // Current scale multiplier
    scaleLabel = new JLabel(scaleMultLabelPrefix + curS);
    scaleLabel.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, scaleLabel, 0, SpringLayout.HORIZONTAL_CENTER,
        scaleSlider);
    springLayout.putConstraint(SpringLayout.NORTH, scaleLabel, 4, SpringLayout.SOUTH, scaleSlider);
    scaleLabel.setForeground(Color.WHITE);
    add(scaleLabel);

    // Setup delta time slider
    int maxT = 20;
    double curDT = simP.getDt();
    // Convert dt value into a slider value (assumes it is possitive)
    int valT = (int) Math.round(Math.log(curDT + 1) / Math.log(2));
    timeSlider = new JSlider(-maxT, maxT, valT);
    timeSlider.setMajorTickSpacing(5);
    timeSlider.setMinorTickSpacing(1);
    timeSlider.setPaintTicks(true);
    // timeSlider.setPaintLabels(true);
    timeSlider.setOpaque(false);
    timeSlider.setForeground(Color.WHITE);
    timeSlider.setSnapToTicks(true);
    Dimension sliderDimTime = timeSlider.getPreferredSize();
    timeSlider.setPreferredSize(new Dimension((int) sliderDimTime.getWidth() + 100, (int) sliderDimTime.getHeight()));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, timeSlider, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, timeSlider, 0, SpringLayout.VERTICAL_CENTER, this);
    add(timeSlider);
    // Slider label
    JLabel sliderLabelTime = new JLabel("Adjust the speed of the simulation");
    sliderLabelTime.setFont(new Font("Dialog", Font.BOLD, 15));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, sliderLabelTime, 0, SpringLayout.HORIZONTAL_CENTER,
        timeSlider);
    springLayout.putConstraint(SpringLayout.SOUTH, sliderLabelTime, -4, SpringLayout.NORTH, timeSlider);
    sliderLabelTime.setForeground(Color.WHITE);
    add(sliderLabelTime);
    // Current speed multiplier
    multLabel = new JLabel(timeMultLabelPrefix + curDT);
    multLabel.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, multLabel, 0, SpringLayout.HORIZONTAL_CENTER,
        timeSlider);
    springLayout.putConstraint(SpringLayout.NORTH, multLabel, 4, SpringLayout.SOUTH, timeSlider);
    multLabel.setForeground(Color.WHITE);
    add(multLabel);
    // Button to pause simulation
    timePauseB = new JButton(pauseString);
    timePauseB.setFocusable(false);
    timePauseB.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.WEST, timePauseB, 20, SpringLayout.EAST, timeSlider);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, timePauseB, -20, SpringLayout.VERTICAL_CENTER, this);
    add(timePauseB);
    // Button to reset simulation
    String resetString = "⭯  Reset";
    JButton resetB = new JButton(resetString);
    resetB.setFocusable(false);
    resetB.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.WEST, resetB, 20, SpringLayout.EAST, timeSlider);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, resetB, 20, SpringLayout.VERTICAL_CENTER, this);
    add(resetB);
    // SETUP LISTENERS

    // Time slider listener
    timeSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider slider = ((JSlider) e.getSource());
        int value = slider.getValue();
        double newDt = getDt(value);
        simP.setDt(newDt);
        multLabel.setText(timeMultLabelPrefix + (int) newDt);
      }
    });
    // Scale slider listener
    scaleSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider slider = ((JSlider) e.getSource());
        int value = slider.getValue();
        double newS = getScale(value);
        simP.getCamera().setScale((int) newS);
        scaleLabel.setText(scaleMultLabelPrefix + (int) newS);
      }
    });
    // Pause button listener
    timePauseB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        if (timePaused) {
          resume();
        } else {
          stop();
        }
      }
    });
    // Reset button listener
    resetB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        reset();
      }
    });
  }

  /**
   * Resumes the simulation
   */
  private void resume() {
    timePauseB.setText(pauseString);
    int value = timeSlider.getValue();
    double newDt = getDt(value);
    simP.setDt(newDt);
    multLabel.setText(timeMultLabelPrefix + (int) newDt);
    timePaused = false;
  }

  /**
   * Stops the simulation
   */
  private void stop() {
    timePauseB.setText(resumeString);
    int value = 0;
    double newDt = getDt(value);
    simP.setDt(newDt);
    multLabel.setText(timeMultLabelPrefix + (int) newDt);
    timePaused = true;
  }

  /**
   * Resets the simulation
   */
  private void reset() {
    simP.reset();
  }

  /**
   * Takes a value from the time slider and transforms it into a small change in
   * time
   *
   * @param val The slider value
   * @return the dt value
   */
  private double getDt(int val) {
    if (val > 0) {
      return Math.pow(2, val) - 1;
    }
    return -Math.pow(2, -val) + 1;

  }

  /**
   * Takes a value from the scale slider and transforms it into a scale value for
   * the camera
   *
   * @param val The slider value
   * @return the scale value
   */
  private double getScale(int val) {
    return Math.pow(1.5, val);
  }

}
