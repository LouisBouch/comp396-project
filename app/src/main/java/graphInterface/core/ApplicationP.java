package graphInterface.core;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import graphInterface.simulation.ParametersP;
import graphInterface.simulation.SimulationP;
import graphInterface.simulation.TopP;

/**
 * ApplicationP
 */
public class ApplicationP extends JPanel {

  final int PARAMETER_PANEL_WIDTH = 325;
  final int TIME_PANEL_HEIGHT = 125;
  private static final long serialVersionUID = 7892977983635887677L;

  private SimulationP simPanel;
  private TopP topP;
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
    topP = new TopP(simPanel);
    springLayout.putConstraint(SpringLayout.SOUTH, topP, TIME_PANEL_HEIGHT, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.EAST, topP, -4, SpringLayout.EAST, this);
    springLayout.putConstraint(SpringLayout.NORTH, topP, 4, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.WEST, topP, PARAMETER_PANEL_WIDTH, SpringLayout.WEST, this);
    this.add(topP);
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
  /**
   * Getter for the top panel
   * @return an instance of the top panel
   */
  public TopP getTopP() {
    return topP;
  }
}


