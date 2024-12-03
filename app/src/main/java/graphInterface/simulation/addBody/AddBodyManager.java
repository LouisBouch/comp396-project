package graphInterface.simulation.addBody;

import java.awt.Dimension;
import java.awt.Point;
import java.util.function.Consumer;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import environment.Body;
import environment.Camera3D;

/**
 * This class will manage a JDialog with settings
 */
public class AddBodyManager {
  public static final int ROCKY = 0;
  public static final int GASSY = 1;
  public static final int SUNNY = 2;
  private boolean initialized = false;

  private JFrame owner;
  private Point pos;
  private int type;
  private Dimension size;
  private JDialog setD;
  private JScrollPane scrollPane;
  private AddBodyContainerP addBodyP;

  /**
   * Constructs a JDialog manager to handle the simulation settings
   *
   * @param pos  Position relative to the owner. Use null for center of owner
   * @param size Size of the JDialog
   * @param type Type of body
   */
  public AddBodyManager(Point pos, Dimension size, int type, Consumer<Body> add, Camera3D cam) {
    this.pos = pos;
    this.size = size;
    this.type = type;
    // Panel that contains add body logic
    addBodyP = new AddBodyContainerP(type, add, cam);
    scrollPane = new JScrollPane(addBodyP);
    // Make scrolling faster
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
  }

  /**
   * Lazily initialize JDialog
   * This gives time for the JFrame owner to be initialized
   *
   * @param owner Jframe that hosts the body window. Can be null
   */
  public JDialog initializeJDialog(JFrame owner) {
    // Only initialize JDialog once
    if (initialized)
      return null;
    initialized = true;
    this.owner = owner;
    setD = new JDialog(this.owner, "New body");
    setD.setSize(size);
    setD.setLocationRelativeTo(owner);
    if (pos != null) {
      setD.setLocation(pos);
    }
    setD.setResizable(false);
    // Layout

    // Add the scrollable component to the JDialog
    setD.add(scrollPane);
    return setD;
  }

  /**
   * Makes settings JDialog either visible or invisible
   *
   * @param v Whether the settings JDialog is visible or not
   */
  public void setVisible(boolean v) {
    setD.setVisible(v);
  }
}
