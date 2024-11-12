package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

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
    setLayout(layout);

    // Scrollpane where parameters for each body will be
    BodiesP bodiesP = new BodiesP(this.bodies);
    UIManager.put("ScrollBar.width", 10);
    UIManager.put("ScrollBar.height", 10);
    JScrollPane bodiesSPane = new JScrollPane(bodiesP);
    bodiesSPane.setBorder(new LineBorder(Color.BLACK, 2));
    bodiesSPane.getVerticalScrollBar().setUnitIncrement(16);
    this.add(bodiesSPane);

    layout.putConstraint(SpringLayout.SOUTH, bodiesSPane, -2, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.NORTH, bodiesSPane, 200, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, bodiesSPane, 2, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, bodiesSPane, -2, SpringLayout.EAST, this);

    for (int i = 0; i < 50; i++) {
      bodiesP.addBodyP(new BodyP(null));
    }
  }
}
