package graphInterface.simulation;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;

import environment.Body;

/**
 * BodiesP
 */
public class BodiesP extends JPanel {
  private GridBagLayout gbLayout;
  private GridBagConstraints gbc;
  private int nbPanels = 0;
  private JLabel emptyL;
  private ArrayList<BodyP> bodyPanels = new ArrayList<>();
  private ArrayList<Body> bodies;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodiesP(ArrayList<Body> bodies) {
    this.bodies = bodies;
    setBackground(Color.decode("#1f1f38"));

    // No rows for now, and a single column
    gbLayout = new GridBagLayout();
    gbc = new GridBagConstraints();
    gbc.anchor = GridBagConstraints.FIRST_LINE_START;
    gbc.weightx = 1;
    gbc.gridx = 0;// Allways align to the left
    gbc.insets = new Insets(2, 2, 2, 2);// Margin
    gbc.fill = GridBagConstraints.BOTH;// Takes all the space it can
    this.setLayout(gbLayout);
    // Empty label at the end to take up the remaining space
    emptyL = new JLabel("");
    emptyL.setOpaque(false);
    gbc.weighty = 1;
    gbc.gridy = nbPanels;
    this.add(emptyL, gbc);

  }

  /**
   * Addes a parameter panel for a body
   *
   * @param p The pannel holding the body parameters
   */
  public void addBodyP(BodyP p) {
    // Add to list of bodies
    bodyPanels.add(p);

    // Remove empty label
    remove(emptyL);

    // Add body parameter row
    gbc.weighty = 0;
    gbc.gridy = nbPanels;
    this.add(p, gbc);
    nbPanels++;

    // Add empty label back
    gbc.weighty = 1;
    gbc.gridy = nbPanels;
    this.add(emptyL, gbc);
  }
}
