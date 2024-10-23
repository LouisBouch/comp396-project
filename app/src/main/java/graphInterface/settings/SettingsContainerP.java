package graphInterface.settings;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SettingsContainerP extends JPanel {
  private GridBagLayout gbLayout;
  private GridBagConstraints gbc;
  private int nbSettings = 0;
  private JButton applyButton;
  private ArrayList<SettingRowP> settings = new ArrayList<>();

  /**
   * Will contain all the rows of settings
   */
  public SettingsContainerP() {
    applyButton = new JButton("Apply");
    applyButton.setFocusable(false);
    applyButton.setFont(new Font("Tahoma", Font.BOLD, 18));
    // No rows for now, and a single column
    gbLayout = new GridBagLayout();
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.weightx = 1;
    gbc.gridx = 0;// Allways align to the left
    this.setLayout(gbLayout);
    // Empty label at the end to take up the remaining space
    gbc.anchor = GridBagConstraints.LAST_LINE_END;
    gbc.weighty = 1;
    gbc.gridy = nbSettings;
    this.add(applyButton, gbc);

    // Button listener to apply settings
    applyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Apply changes for each setting
        for (SettingRowP setting : settings) {
          setting.applyRowSetting();
        }
      }
    });
  }

  /**
   * Adds setting row to the panel
   *
   * @param setR The setting to add
   */
  public void addSetting(SettingRowP setR) {
    // Add to list of settings
    settings.add(setR);
    // Remove button
    remove(applyButton);

    // Add setting row
    gbc.insets = new Insets(2, 2, 2, 2);// Margin
    gbc.fill = GridBagConstraints.BOTH;// Takes all the space it can
    gbc.weighty = 0;
    gbc.gridy = nbSettings;
    this.add(setR, gbc);
    nbSettings++;

    // Add button back
    gbc.insets = new Insets(15, 15, 15, 15);// Margin
    gbc.fill = GridBagConstraints.NONE;// Take no extra space
    gbc.weighty = 1;
    gbc.gridy = nbSettings;
    this.add(applyButton, gbc);
  }
}
