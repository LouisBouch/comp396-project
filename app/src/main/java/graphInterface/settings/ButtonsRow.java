package graphInterface.settings;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.SpringLayout;

public class ButtonsRow extends SettingRowP {
  private SettingsManager setM;
  /**
   * Create a row for the buttons
   * This class also handles the saving and loading of settings
   */
  public ButtonsRow(ArrayList<SettingRowP> settings, SettingsManager setM) {
    super(55, (v) -> {
    }, 0, "Buttons");
    this.setM = setM;
    setPreferredSize(new Dimension(0, this.getRowH()));
    setMinimumSize(new Dimension(0, this.getRowH()));
    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // The apply button
    JButton applyB = new JButton("Apply");
    applyB.setFocusable(false);
    applyB.setFont(new Font("Tahoma", Font.BOLD, 18));
    add(applyB);

    // The reset button
    JButton resetB = new JButton("Reset");
    resetB.setFocusable(false);
    resetB.setFont(new Font("Tahoma", Font.BOLD, 18));
    add(resetB);

    // Constraints
    layout.putConstraint(SpringLayout.EAST, applyB, -15, SpringLayout.EAST, this);
    layout.putConstraint(SpringLayout.SOUTH, applyB, -15, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.WEST, resetB, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.SOUTH, resetB, -15, SpringLayout.SOUTH, this);

    // Listeners
    applyB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Apply changes for each setting
        for (SettingRowP setting : settings) {
          setting.applyRowSetting();
        }
        setM.saveSettings();
      }
    });
    resetB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // Apply changes for each setting
        for (SettingRowP setting : settings) {
          setting.resetValue();
        }
        setM.saveSettings();
      }
    });
  }
}
