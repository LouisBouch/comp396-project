package graphInterface.settings;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Label;
import java.util.ArrayList;

import javax.swing.JPanel;

public class SettingsContainerP extends JPanel {
  private GridBagLayout gbLayout;
  private GridBagConstraints gbc;
  private int nbSettings = 0;
  private LabelRow emptyR;
  private ArrayList<SettingRowP> settings = new ArrayList<>();

  /**
   * Will contain all the rows of settings
   */
  public SettingsContainerP() {
    // No rows for now, and a single column
    gbLayout = new GridBagLayout();
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.weightx = 1;
    gbc.gridx = 0;// Allways align to the left
    gbc.insets = new Insets(2, 2, 2, 2);// Margin
    gbc.fill = GridBagConstraints.BOTH;// Takes all the space it can
    this.setLayout(gbLayout);
    // Empty label at the end to take up the remaining space
    emptyR = new  LabelRow("");
    gbc.weighty = 1;
    gbc.gridy = nbSettings;
    this.add(emptyR, gbc);
  }

  /**
   * Adds setting row to the panel
   *
   * @param setR The setting to add
   */
  public void addSetting(SettingRowP setR) {
    // Add to list of settings
    settings.add(setR);

    // Remove empty label
    remove(emptyR);

    // Add setting row
    gbc.weighty = 0;
    gbc.gridy = nbSettings;
    this.add(setR, gbc);
    nbSettings++;

    // Add empty label back
    gbc.weighty = 1;
    gbc.gridy = nbSettings;
    this.add(emptyR, gbc);
  }

  /**
   * Getter for settings
   *
   * @return List of settings
   */
  public ArrayList<SettingRowP> getSettings() {
    return settings;
  }
}
