package graphInterface.simulation;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import environment.Body;

/**
 * BodiesP
 */
public class BodiesP extends JPanel {
  private ArrayList<Body> bodies;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodiesP(ArrayList<Body> bodies) {
    this.bodies = bodies;
    setBackground(Color.decode("#1f1f38"));

    SpringLayout layout = new SpringLayout();
  }
}
