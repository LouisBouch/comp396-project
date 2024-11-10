package graphInterface.core;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import environment.SolarSystem;
import graphInterface.simulation.ParametersP;
import graphInterface.simulation.SimulationP;
import graphInterface.simulation.TimeP;

/**
 * ApplicationP
 */
public class ApplicationP extends JPanel {

  final int PARAMETER_PANEL_WIDTH = 325;
  final int TIME_PANEL_HEIGHT = 125;
  private static final long serialVersionUID = 7892977983635887677L;

  private SimulationP simPanel;
  private TimeP timePanel;
  private ParametersP paraPanel;
  public ApplicationP() {

    SpringLayout springLayout = new SpringLayout();
    this.setLayout(springLayout);

    // Contains the 3D sim
    simPanel = new SimulationP();
    springLayout.putConstraint(SpringLayout.SOUTH, simPanel, -4, SpringLayout.SOUTH, this);
    springLayout.putConstraint(SpringLayout.EAST, simPanel, -4, SpringLayout.EAST, this);
    springLayout.putConstraint(SpringLayout.NORTH, simPanel, TIME_PANEL_HEIGHT, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.WEST, simPanel, PARAMETER_PANEL_WIDTH, SpringLayout.WEST, this);
    this.add(simPanel);
    // Contains time info
    timePanel = new TimeP(simPanel);
    springLayout.putConstraint(SpringLayout.SOUTH, timePanel, TIME_PANEL_HEIGHT, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.EAST, timePanel, -4, SpringLayout.EAST, this);
    springLayout.putConstraint(SpringLayout.NORTH, timePanel, 4, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.WEST, timePanel, PARAMETER_PANEL_WIDTH, SpringLayout.WEST, this);
    this.add(timePanel);
    // Contains parameters
    paraPanel = new ParametersP(simPanel);
    springLayout.putConstraint(SpringLayout.SOUTH, paraPanel, -4, SpringLayout.SOUTH, this);
    springLayout.putConstraint(SpringLayout.EAST, paraPanel, PARAMETER_PANEL_WIDTH, SpringLayout.WEST, this);
    springLayout.putConstraint(SpringLayout.NORTH, paraPanel, 4, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.WEST, paraPanel, 4, SpringLayout.WEST, this);
    this.add(paraPanel);

  }

  /**
   * Getter for the simulation panel
   * @return an instance of the simulation
   */
  public SimulationP getSimPanel() {
    return simPanel;
  }
}


