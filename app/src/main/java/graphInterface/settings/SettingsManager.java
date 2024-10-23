package graphInterface.settings;

import java.awt.Dimension;
import java.awt.Point;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * This class will manage a JDialog with settings
 */
public class SettingsManager {
  JFrame owner;
  Point pos;
  Dimension size;
  JDialog setD;
  JScrollPane scrollPane;
  SettingsContainerP settingsContainer;

  /**
   * Constructs a JDialog manager to handle the simulation settings
   *
   * @param owner Jframe that hosts the settings window. Can be null
   * @param pos   Position relative to the owner. Use null for center of owner
   * @param size  Size of the JDialog
   * @param name  Name of the JDialog window
   */
  public SettingsManager(JFrame owner, Point pos, Dimension size, String name) {
    this.owner = owner;
    this.pos = pos;
    this.size = size;
    setD = new JDialog(owner, name);
    setD.setSize(size);
    setD.setLocationRelativeTo(owner);
    if (pos != null) {
      setD.setLocation(pos);
    }
    setD.setResizable(false);
    // Panel that contains settings
    settingsContainer = new SettingsContainerP();
    // Component to ensure the setting container can grow infinitely. A scrollbar
    // will appear if necessary.
    scrollPane = new JScrollPane(settingsContainer);
    //Make scrolling faster
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    // Add the scrollable component to the JDialog
    setD.add(scrollPane);
  }

  /**
   * Makes settings JDialog either visible or invisible
   *
   * @param v Whether the settings JDialog is visible or not
   */
  public void setVisible(boolean v) {
    setD.setVisible(v);
  }

  /**
   * Adds a setting to the JDialog
   */
  public void addSetting(SettingRowP setR) {
    settingsContainer.addSetting(setR);
  }
}
