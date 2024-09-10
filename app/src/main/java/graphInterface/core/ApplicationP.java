package graphInterface.core;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import graphInterface.simulation.SimulationP;

/**
 * ApplicationP
 */
public class ApplicationP extends JPanel {

  final int PARAMETER_PANEL_WIDTH = 325;
  final int TIME_PANEL_HEIGHT = 75;
  private static final long serialVersionUID = 7892977983635887677L;

  public ApplicationP() {

    SpringLayout springLayout = new SpringLayout();
    this.setLayout(springLayout);

    SimulationP simPanel = new SimulationP();
    springLayout.putConstraint(SpringLayout.SOUTH, simPanel, -4, SpringLayout.SOUTH, this);
    springLayout.putConstraint(SpringLayout.EAST, simPanel, -4, SpringLayout.EAST, this);
    springLayout.putConstraint(SpringLayout.NORTH, simPanel, TIME_PANEL_HEIGHT, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.WEST, simPanel, PARAMETER_PANEL_WIDTH, SpringLayout.WEST, this);
    this.add(simPanel);
  }
}
