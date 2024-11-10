package graphInterface.simulation;

import java.awt.Color;
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
public class TimeP extends JPanel {
  private SimulationP simP;
  private boolean timePaused = false;
  private JSlider timeSlider;
  private JLabel multLabel;
  private String timeMultLabelPrefix = "Current time multiplier: x";
  private String pauseString = "⏹ Pause";
  private String resumeString = "▶ Resume";
  private JButton timePauseB;

  /**
   * Constructor for JPanel
   *
   * @param solarSystem Solar system to take information from
   */
  public TimeP(SimulationP simP) {
    this.simP = simP;
    setBackground(Color.decode("#1f1f38"));
    SpringLayout springLayout = new SpringLayout();
    setLayout(springLayout);

    // Setup delta time slider
    int max = 10;
    double curValue = simP.getDt();
    // Convert dt value into a slider value (assumes it is possitive)
    int val = (int) Math.round(Math.log(curValue + 1) / Math.log(2));
    timeSlider = new JSlider(-max, max, val);
    timeSlider.setMajorTickSpacing(5);
    timeSlider.setMinorTickSpacing(1);
    timeSlider.setPaintTicks(true);
    // timeSlider.setPaintLabels(true);
    timeSlider.setOpaque(false);
    timeSlider.setForeground(Color.WHITE);
    timeSlider.setSnapToTicks(true);
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, timeSlider, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, timeSlider, 0, SpringLayout.VERTICAL_CENTER, this);
    add(timeSlider);
    // Slider label
    JLabel sliderLabel = new JLabel("Adjust the speed of the simulation");
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, sliderLabel, 0, SpringLayout.HORIZONTAL_CENTER,
        timeSlider);
    springLayout.putConstraint(SpringLayout.SOUTH, sliderLabel, -4, SpringLayout.NORTH, timeSlider);
    sliderLabel.setForeground(Color.WHITE);
    add(sliderLabel);
    // Current speed multiplier
    multLabel = new JLabel(timeMultLabelPrefix + timeSlider.getValue());
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, multLabel, 0, SpringLayout.HORIZONTAL_CENTER,
        timeSlider);
    springLayout.putConstraint(SpringLayout.NORTH, multLabel, 4, SpringLayout.SOUTH, timeSlider);
    multLabel.setForeground(Color.WHITE);
    add(multLabel);
    // Button to pause simulation
    timePauseB = new JButton(pauseString);
    timePauseB.setFocusable(false);
    System.out.println(timePauseB.getFont());
    timePauseB.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.WEST, timePauseB, 15, SpringLayout.EAST, timeSlider);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, timePauseB, -20, SpringLayout.VERTICAL_CENTER, this);
    add(timePauseB);
    // Button to reset simulation
    String resetString = "⭯  Reset";
    JButton resetB = new JButton(resetString);
    resetB.setFocusable(false);
    resetB.setFont(new Font("Dialog", Font.BOLD, 14));
    springLayout.putConstraint(SpringLayout.WEST, resetB, 15, SpringLayout.EAST, timeSlider);
    springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, resetB, 20, SpringLayout.VERTICAL_CENTER, this);
    add(resetB);
    // SETUP LISTENERS

    // Slider listener
    timeSlider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider slider = ((JSlider) e.getSource());
        int value = slider.getValue();
        double newDt = getDt(value);
        simP.setDt(newDt);
        multLabel.setText(timeMultLabelPrefix + (int) newDt);
        resume();
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
    resume();
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

}
