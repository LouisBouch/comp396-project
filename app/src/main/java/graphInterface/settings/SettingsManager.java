package graphInterface.settings;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

/**
 * This class will manage a JDialog with settings
 */
public class SettingsManager {
  private GridBagLayout gbLayout;
  private GridBagConstraints gbc;

  private boolean initialized = false;

  private JFrame owner;
  private Point pos;
  private Dimension size;
  private String name;
  private JDialog setD;
  private JScrollPane scrollPane;
  private SettingsContainerP settingsContainer;
  private HashMap<String, SettingRowP> settings = new HashMap<>();

  private Path PATH;

  private boolean pathLoadFail = false;

  /**
   * Constructs a JDialog manager to handle the simulation settings
   *
   * @param pos  Position relative to the owner. Use null for center of owner
   * @param size Size of the JDialog
   * @param name Name of the JDialog window
   */
  public SettingsManager(Point pos, Dimension size, String name) {
    this.pos = pos;
    this.size = size;
    this.name = name;
    // Path to config file
    PATH = Paths.get(System.getProperty("user.dir"), "config", "settings.properties");

    // Panel that contains settings
    settingsContainer = new SettingsContainerP();
    // Component to ensure the setting container can grow infinitely. A scrollbar
    // will appear if necessary.
    scrollPane = new JScrollPane(settingsContainer);
    // Make scrolling faster
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
  }

  /**
   * Lazily initialize JDialog
   * This gives time for the JFrame owner to be initialized
   *
   * @param owner Jframe that hosts the settings window. Can be null
   */
  public void initializeJDialog(JFrame owner) {
    // Only initialize JDialog once
    if (initialized)
      return;
    initialized = true;
    this.owner = owner;
    setD = new JDialog(this.owner, name);
    setD.setSize(size);
    setD.setLocationRelativeTo(owner);
    if (pos != null) {
      setD.setLocation(pos);
    }
    setD.setResizable(false);
    // Layout
    gbLayout = new GridBagLayout();
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.weightx = 1;
    gbc.weighty = 1;
    gbc.gridx = 0;// Allways align to the left
    gbc.gridy = 0;
    gbc.fill = GridBagConstraints.BOTH;
    setD.setLayout(gbLayout);

    // Add the scrollable component to the JDialog
    setD.add(scrollPane, gbc);
    gbc.weighty = 0;
    gbc.gridy = 1;
    gbc.insets = new Insets(5, 5, 5, 5);// Margin
    // Add the row of buttons to the JDialog
    ArrayList<SettingRowP> settingsList = settingsContainer.getSettings();
    ButtonsRow butR = new ButtonsRow(settingsList, this);
    setD.add(butR, gbc);

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
    settings.put(setR.getLabel(), setR);
    settingsContainer.addSetting(setR);
  }

  /**
   * Saves the current configuration of settings
   */
  public void saveSettings() {
    Properties props = new Properties();
    for (String key : settings.keySet()) {
      props.setProperty(settings.get(key).getLabel(), String.valueOf(settings.get(key).getCurV()));
    }
    try (OutputStream output = Files.newOutputStream(PATH)) {
      props.store(output, "Simulation settings");
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load settings previously saved
   */
  public void loadSettings() {
    Properties props = new Properties();
    // Get properties
    try (InputStream input = Files.newInputStream(PATH)) {
      props.load(input);
    } catch (IOException e) {
      // Does nothing if no file exists
      if (pathLoadFail == false){
        PATH = Paths.get(System.getProperty("user.dir"),"app", "config", "settings.properties");
        pathLoadFail = true;
        loadSettings();
      }
      else{
        System.out.println("Could not load settings, using default settings.");
      }
      return;
    }
    // Load the settings
    for (String key : props.stringPropertyNames()) {
      double value = Double.valueOf(props.getProperty(key));
      SettingRowP set = settings.get(key);
      set.setCurV(value);
      set.applyRowSetting();
    }
  }
}
