package graphInterface.settings;

import java.util.function.Consumer;

import javax.swing.JPanel;

public class SettingRowP extends JPanel {
  private int rowH;
  private Consumer<Double> applySetting;
  /**
   * Current value of setting
   */
  private double curV;
  private String label;
  /**
   * Default value of setting
   */
  private double defV;
  private int settingW = 220;
  /**
   * Create a row for a single setting
   */
  public SettingRowP(int rowH, Consumer<Double> applySetting, double value, String label) {
    this.rowH = rowH;
    this.applySetting = applySetting;
    this.curV = value;
    this.defV = curV;
    this.label = label;
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
    applySetting.accept(curV);
  }
  /**
   * Resets the value of the setting to its original value
   */
  public void resetValue() {
    this.curV = defV;
    applySetting.accept(defV);
  }
  /**
   * Setter for the value field
   *
   * @param v The new value
   */
  public void setCurV(double v) {
    curV = v;
  }
  /**
   * Getter for the value field
   *
   * @return The value of the setting
   */
  public double getCurV() {
    return curV;
  }
  /**
   * Gets the width of a setting item
   *
   * @return Width in pixel of the setting component
   */
  public int getSettingW() {
    return settingW;
  }
  /**
   * Getter for the setting label
   *
   * @return The label of the setting
   */
  public String getLabel() {
    return label;
  }
}
