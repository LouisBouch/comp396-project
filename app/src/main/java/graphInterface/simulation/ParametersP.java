package graphInterface.simulation;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

import environment.Body;

/**
 * ParaetersP
 */
public class ParametersP extends JPanel {
  private SimulationP simP;
  private ArrayList<Body> bodies;

  /**
   * Constructor for JPanel
   *
   * @param solarSystem Solar system to take information from
   */
  public ParametersP(SimulationP simP) {
    this.simP = simP;
    this.bodies = simP.getSolarSystem().getBodies();
    setBackground(Color.decode("#1f1f38"));

    SpringLayout layout = new SpringLayout();
    // Scrollpane where bodies will be
    BodiesP bodiesP = new BodiesP(this.bodies);
    JScrollPane bodiesSPane = new JScrollPane();
  }
}
