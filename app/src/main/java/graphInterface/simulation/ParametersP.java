package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.function.Consumer;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import environment.Body;
import graphInterface.simulation.addBody.AddBodyManager;
import lib.keyBinds;

/**
 * ParaetersP
 */
public class ParametersP extends JPanel implements Runnable {
  private SimulationP simP;
  private ArrayList<Body> bodies;
  private JButton addBodyB;
  private JComboBox<String> bodyTypeBox;
  private Thread paraThread;
  private int checkDelay = 500;
  private BodiesP bodiesP;
  private boolean editMode = false;
  private AddBodyManager newBodyD;

  /**
   * Constructor for JPanel
   *
   * @param solarSystem Solar system to take information from
   */
  public ParametersP(SimulationP simP) {
    this.simP = simP;
    this.bodies = this.simP.getSolarSystem().getBodies();
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
    setupAddBodyFunctionality();

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
    // Sets up keybindings
    setupKeyBindings();
  }

  /**
   * Sets up keybiding for the parameters panel
   */
  public void setupKeyBindings() {
    // Binding to monitor enter keypress
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_ENTER, "Pressed Enter",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("Enter");
          }
        });
    // Binding to monitor escape keypress
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_ESCAPE, "Pressed Escape",
        new AbstractAction() {
          @Override
          public void actionPerformed(ActionEvent e) {
            System.out.println("Escape");
          }
        });
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
   * Redraws the panel of bodies
   */
  public void listenBodies() {
    bodiesP.updatePanel(bodies, simP.getCamera());
  }

  /**
   * Sets up the logic behind add body
   */
  public void setupAddBodyFunctionality() {
    String[] bodyTypes = { "Rocky Planet", "Gas Planet", "Star" };
    JPanel thisP = this;
    // Button listener
    addBodyB.addActionListener(new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent e) {
        int index = bodyTypeBox.getSelectedIndex();
        String body = bodyTypes[index];
        editMode = true;
        int width = 500;
        int height = 475;
        Consumer<Body> addBody = b -> simP.getSolarSystem().getBodies().add(b);
        switch (body) {
          case "Rocky Planet":
            newBodyD = new AddBodyManager(null, new Dimension(width, height), AddBodyManager.ROCKY, addBody, simP.getCamera());
            break;

          case "Gas Planet":
            newBodyD = new AddBodyManager(null, new Dimension(width, height), AddBodyManager.GASSY, addBody, simP.getCamera());
            break;

          case "Star":
            newBodyD = new AddBodyManager(null, new Dimension(width, height), AddBodyManager.SUNNY, addBody, simP.getCamera());
            break;
        }
        // This is the window frame
        JFrame ownerFrame = (JFrame) SwingUtilities.getWindowAncestor(thisP);
        newBodyD.initializeJDialog(ownerFrame);
        newBodyD.setVisible(true);
      }
    });
  }
}
