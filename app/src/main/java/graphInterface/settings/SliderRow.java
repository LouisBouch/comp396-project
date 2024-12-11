package graphInterface.settings;

import java.awt.Dimension;
import java.awt.Font;
import java.util.function.Consumer;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SliderRow extends SettingRowP {
  JSlider slider;
  double min;
  double max;
  double start;
  // Initial percentage on the slider
  int iniPerc;

  /**
   * Create a row for a single setting
   */
  public SliderRow(String label, double min, double max, double start, Consumer<Double> applySetting) {
    super(30, applySetting, start, label);
    this.min = min;
    this.max = max;
    this.start = start;
    setPreferredSize(new Dimension(0, this.getRowH()));
    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // The label
    JLabel l = new JLabel(label + ":");
    l.setFont(new Font("Tahoma", Font.PLAIN, 22));
    l.setHorizontalAlignment(JLabel.LEFT);
    add(l);

    // The slider
    iniPerc = (int) Math.round(100.0 * (start - min) / ((max - min) * 1.0));
    slider = new JSlider(0, 100, iniPerc);
    add(slider);

    // Constraints
    layout.putConstraint(SpringLayout.WEST, l, 40, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, l, 0, SpringLayout.VERTICAL_CENTER, this);

    layout.putConstraint(SpringLayout.EAST, slider, -20, SpringLayout.EAST, this);
    layout.putConstraint(SpringLayout.WEST, slider, -(this.getSettingW()), SpringLayout.EAST, this);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, slider, 0, SpringLayout.VERTICAL_CENTER, this);

    // Slider listener
    SliderRow thisP = this;
    slider.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        JSlider source = (JSlider) e.getSource();
        thisP.setCurV(source.getValue() / 100.0 * (max - min) + min);
      }
    });
  }

  @Override
  public void resetValue() {
    super.resetValue();
    slider.setValue(iniPerc);
  }
  @Override
  public void setCurV(double v) {
    super.setCurV(v);
    int perc = (int) Math.round(100.0 * (v - min) / ((max - min) * 1.0));
    slider.setValue(perc);
  }
}
