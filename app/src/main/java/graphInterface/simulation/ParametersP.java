package graphInterface.simulation;

import java.awt.Color;

import javax.swing.JPanel;

/**
 * ParaetersP
 */
public class ParametersP extends JPanel {
  private SimulationP simP;

  /**
   * Constructor for JPanel
   *
   * @param solarSystem Solar system to take information from
   */
  public ParametersP(SimulationP simP) {
    this.simP = simP;
    setBackground(Color.decode("#1f1f38"));
  }
}
