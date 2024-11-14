package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import environment.Body;

/**
 * BodiesP
 */
public class BodyP extends JPanel {
  private int preferredHeight = 150;
  private Body body;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodyP(Body body) {
    this.body = body;
    setBorder(new LineBorder(Color.BLACK, 2));
    setBackground(Color.decode("#1f1f38"));
    Dimension prefSize = getPreferredSize();
    setPreferredSize(new Dimension((int) prefSize.getWidth(), preferredHeight));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    JLabel label = new JLabel(body.getBodyName());
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Dialog", Font.BOLD, 18));
    label.setForeground(Color.WHITE);
    add(label);
  }
}
