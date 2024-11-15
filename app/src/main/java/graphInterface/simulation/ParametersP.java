package graphInterface.simulation;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
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
public class ParametersP extends JPanel implements Runnable {
  private SimulationP simP;
  private ArrayList<Body> bodies;
  private ArrayList<Body> prevBodies;
  private JButton addBodyB;
  private JComboBox<String> bodyTypeBox;
  private Thread paraThread;
  private int checkDelay = 500;
  private BodiesP bodiesP;

  /**
   * Constructor for JPanel
   *
   * @param solarSystem Solar system to take information from
   */
  public ParametersP(SimulationP simP) {
    this.simP = simP;
    this.bodies = this.simP.getSolarSystem().getBodies();
    this.prevBodies = new ArrayList<>(this.bodies);
    setBackground(Color.decode("#1f1f38"));
    setBorder(new LineBorder(Color.BLACK, 2));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // Main label
    JLabel parametersLabel = new JLabel("Your Solar System ♥");
    parametersLabel.setForeground(Color.WHITE);
    parametersLabel.setFont(new Font("Dialog", Font.BOLD, 28));
    layout.putConstraint(SpringLayout.NORTH, parametersLabel, 5, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, parametersLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
    add(parametersLabel);

    // JComboBox to decide which type of body to add
    String[] bodyTypes = { "Rocky Planet", "Gas Planet", "Star" };
    bodyTypeBox = new JComboBox<>(bodyTypes);
    bodyTypeBox.setFont(new Font("Dialog", Font.BOLD, 18));
    layout.putConstraint(SpringLayout.NORTH, bodyTypeBox, 50, SpringLayout.NORTH, parametersLabel);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, bodyTypeBox, 2, SpringLayout.HORIZONTAL_CENTER, this);
    bodyTypeBox.setFocusable(false);
    add(bodyTypeBox);

    // Button to add planet
    addBodyB = new JButton("Add body");
    addBodyB.setFocusable(false);
    layout.putConstraint(SpringLayout.NORTH, addBodyB, 10, SpringLayout.SOUTH, bodyTypeBox);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, addBodyB, 0, SpringLayout.HORIZONTAL_CENTER, bodyTypeBox);
    add(addBodyB);

    // Button listener
    addBodyB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int index = bodyTypeBox.getSelectedIndex();
        String body = bodyTypes[index];
        switch (body) {
          case "Rocky Planet":
            System.out.println(1);
            break;

          case "Gas Planet":
            System.out.println(2);
            break;

          case "Star":
            System.out.println(3);
            break;
        }
      }
    });

    // Scrollpane where parameters for each body will be
    bodiesP = new BodiesP(this.bodies);
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
    bodiesP.populatePanel(bodies, simP.getCamera());
    // Starts the thread
    start();
  }

  /**
   * Starts the listener
   */
  public void start() {
    if (paraThread == null) {
      paraThread = new Thread(this);
      paraThread.start();
    }
  }

  /**
   * Listens for changes in the bodies list
   */
  @Override
  public void run() {
    while (true) {
      listenBodies();
      repaint();
      try {
        Thread.sleep(checkDelay);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Checks if the bodies lsit has changed. If so, redraw the list of parameters
   * toreflect new bodies
   */
  public void listenBodies() {
    boolean changed = false;
    if (bodies.size() != prevBodies.size()) {
      changed = true;
    } else {
      for (int i = 0; i < bodies.size(); i++) {
        if (!bodies.get(i).getBodyName().equals(prevBodies.get(i).getBodyName())) {
          changed = true;
        }
      }
    }
    // If the list of bodies has changed since the last check, redraw the panel
    if (changed) {
      prevBodies = new ArrayList<>(bodies);
      bodiesP.updatePanel(bodies, simP.getCamera());
    }
  }
}
