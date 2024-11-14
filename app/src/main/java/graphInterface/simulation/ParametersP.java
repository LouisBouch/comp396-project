package graphInterface.simulation;

import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JLabel;
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
    setBorder(new LineBorder(Color.BLACK, 2));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    JLabel parametersLabel = new JLabel("Your Solar System ♥");
    parametersLabel.setForeground(Color.WHITE);
    parametersLabel.setFont(new Font("Dialog", Font.BOLD, 28));
    layout.putConstraint(SpringLayout.NORTH, parametersLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, parametersLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
    add(parametersLabel);

    // Scrollpane where parameters for each body will be
    BodiesP bodiesP = new BodiesP(this.bodies);
    UIManager.put("ScrollBar.width", 10);
    UIManager.put("ScrollBar.height", 10);
    JScrollPane bodiesSPane = new JScrollPane(bodiesP);
    bodiesSPane.setBorder(new LineBorder(Color.BLACK, 2));
    bodiesSPane.getVerticalScrollBar().setUnitIncrement(16);
    this.add(bodiesSPane);

    layout.putConstraint(SpringLayout.SOUTH, bodiesSPane, -2, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.NORTH, bodiesSPane, 150, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.WEST, bodiesSPane, 2, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, bodiesSPane, -2, SpringLayout.EAST, this);

    // Add body panel for each body
    for (Body b : bodies) {
      bodiesP.addBodyP(new BodyP(b));
    }
  }
}
