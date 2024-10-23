package graphInterface.settings;

import java.awt.Dimension;
import java.util.function.Consumer;

import javax.swing.JPanel;

public class SettingRowP extends JPanel {
  private int rowH;
  private Consumer<Double> applySetting;
  private double value;
  private int settingW = 220;
  /**
   * Create a row for a single setting
   */
  public SettingRowP(int rowH, Consumer<Double> applySetting, double value) {
    this.rowH = rowH;
    this.applySetting = applySetting;
    this.value = value;
  }
  /**
   * Getter for rowH
   *
   * @return Return the row height
   */
  public int getRowH() {
    return rowH;
  }
  /**
   * Executes the method defined for the functional interface Consumer
   * For our purposes, it should update some value of the simulation given the
   * value of the setting.
   */
  public void applyRowSetting() {
    applySetting.accept(value);
  }
  /**
   * Setter for the value field
   *
   * @param v The new value
   */
  public void setValue(double v) {
    value = v;
  }
  /**
   * Getter for the value field
   *
   * @return The value of the setting
   */
  public double getValue() {
    return value;
  }
  /**
   * Gets the width of a setting item
   *
   * @return Width in pixel of the setting component
   */
  public int getSettingW() {
    return settingW;
  }
}
