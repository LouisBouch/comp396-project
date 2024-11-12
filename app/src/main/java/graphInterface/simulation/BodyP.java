package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import environment.Body;

/**
 * BodiesP
 */
public class BodyP extends JPanel {
  private int preferredHeight = 70;
  private Body body;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodyP(Body body) {
    this.body = body;
    setBackground(Color.decode("#1f1f38"));
    Dimension prefSize = getPreferredSize();
    setPreferredSize(new Dimension((int) prefSize.getWidth(), preferredHeight));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    JLabel label = new JLabel("I be parameter");
    label.setForeground(Color.WHITE);
    add(label);
  }
}
