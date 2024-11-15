package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import environment.Body;
import environment.Camera3D;
import lib.Vector3D;

/**
 * BodiesP
 */
public class BodyP extends JPanel {
  private int preferredHeight = 150;
  private Body body;
  private Camera3D camera;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodyP(Body body, Camera3D camera) {
    this.camera = camera;
    this.body = body;
    setBorder(new LineBorder(Color.BLACK, 2));
    setBackground(Color.decode("#1f1f38"));
    Dimension prefSize = getPreferredSize();
    setPreferredSize(new Dimension((int) prefSize.getWidth(), preferredHeight));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // Planet name
    JLabel label = new JLabel(body.getBodyName());
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Dialog", Font.BOLD, 18));
    label.setForeground(Color.WHITE);
    add(label);

    // Goto button that moves camera in front of planet
    JButton gotoB = new JButton("Go to");
    gotoB.setFocusable(false);
    layout.putConstraint(SpringLayout.NORTH, gotoB, 10, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.EAST, gotoB, -10, SpringLayout.EAST, this);
    add(gotoB);

    // Listener for button. Places the camera near the planet
    gotoB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector3D pos = body.getPos();
        Vector3D ori = camera.getCurOrientation();
        double rad = body.getRadius();
        Vector3D exitCenter = Vector3D.scalarMult(ori, -2*rad);
        Vector3D newPos = Vector3D.add(pos, exitCenter);
        camera.setCurPosM(newPos);
      }
    });
  }
}
