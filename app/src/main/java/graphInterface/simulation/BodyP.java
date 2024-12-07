package graphInterface.simulation;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.LineBorder;

import environment.Body;
import environment.Camera3D;
import environment.GassyPlanet;
import environment.RockyPlanet;
import environment.Star;
import environment.habitablity.Gas;
import lib.Vector3D;

/**
 * BodiesP
 */
public class BodyP extends JPanel {
  private int preferredHeight = 150;
  private Body body;
  private Camera3D camera;
  private JLabel pressureL;
  private JLabel temperatureL;
  private JLabel habitabilityL;
  private JLabel massL;
  private JLabel radiusL;
  private JLabel typeL;

  /**
   * Constructor for the bodies panel
   *
   * @param bodies List of bodies in the solar system
   */
  public BodyP(Body body, Camera3D camera, Runnable deleteAction) {
    this.camera = camera;
    this.body = body;
    setBorder(new LineBorder(Color.BLACK, 2));
    setBackground(Color.decode("#1f1f38"));

    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // Planet name
    JLabel bodyNameL = new JLabel(body.getBodyName());
    bodyNameL.setForeground(Color.WHITE);
    bodyNameL.setFont(new Font("Dialog", Font.BOLD, 18));
    bodyNameL.setForeground(Color.WHITE);
    add(bodyNameL);

    // Goto button that moves camera in front of planet
    JButton gotoB = new JButton("Go to");
    gotoB.setFocusable(false);
    layout.putConstraint(SpringLayout.NORTH, gotoB, 50, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.EAST, gotoB, -10, SpringLayout.EAST, this);
    add(gotoB);

    // Look button that makes camera look at body
    JButton lookB = new JButton("Look");
    lookB.setFocusable(false);
    layout.putConstraint(SpringLayout.NORTH, lookB, 10, SpringLayout.SOUTH, gotoB);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lookB, 0, SpringLayout.HORIZONTAL_CENTER, gotoB);
    add(lookB);

    // Delete button that deletes the body
    JButton deleteB = new JButton("X");
    deleteB.setFocusable(false);
    deleteB.setBorderPainted(false);
    deleteB.setContentAreaFilled(false);
    deleteB.setFont(new Font("Dialog", Font.BOLD, 18));
    deleteB.setForeground(Color.WHITE);
    deleteB.setCursor(new Cursor(Cursor.HAND_CURSOR));
    layout.putConstraint(SpringLayout.NORTH, deleteB, 0, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.EAST, deleteB, 0, SpringLayout.EAST, this);
    add(deleteB);

    // Listener for button. Places the camera near the planet
    gotoB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector3D pos = body.getPos();
        Vector3D ori = camera.getCurOrientation();
        double rad = body.getRadius();
        Vector3D exitCenter = Vector3D.scalarMult(ori, -2 * rad * camera.getScale());
        Vector3D newPos = Vector3D.add(pos, exitCenter);
        camera.setCurPosM(newPos);
      }
    });
    lookB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        Vector3D bodyPos = body.getPos().copy();
        camera.lookAt(bodyPos);
      }
    });
    deleteB.addMouseListener(new MouseListener() {
      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mousePressed(MouseEvent e) {
        deleteAction.run();
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }
    });
    // Labels
    typeL = new JLabel("Type: ");
    typeL.setForeground(Color.WHITE);
    typeL.setFont(new Font("Dialog", Font.BOLD, 18));
    typeL.setForeground(Color.WHITE);

    radiusL = new JLabel("Radius: " + roundToSF(body.getRadius(), 3) + " m");
    radiusL.setForeground(Color.WHITE);
    radiusL.setFont(new Font("Dialog", Font.BOLD, 18));
    radiusL.setForeground(Color.WHITE);

    massL = new JLabel("Mass: " + roundToSF(body.getMass(), 3) + " kg");
    massL.setForeground(Color.WHITE);
    massL.setFont(new Font("Dialog", Font.BOLD, 18));
    massL.setForeground(Color.WHITE);

    // Switch case to bind the body class
    if (body instanceof RockyPlanet) {
      // Type
      typeL.setText(typeL.getText() + "Rocky planet");
      RockyPlanet r = (RockyPlanet) body;
      if (r.getAtm().getGas() != Gas.Vacuum) {
        preferredHeight = 300;
        // Delimiter
        JLabel delimiterL = new JLabel("----------------------");
        delimiterL.setForeground(Color.WHITE);
        delimiterL.setFont(new Font("Dialog", Font.BOLD, 18));
        delimiterL.setForeground(Color.WHITE);
        layout.putConstraint(SpringLayout.NORTH, delimiterL, 10, SpringLayout.SOUTH,
            radiusL);
        layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, delimiterL, 0, SpringLayout.HORIZONTAL_CENTER,
            this);
        add(delimiterL);
        // Pressure
        pressureL = new JLabel("Pressure: " + roundToSF(r.getAtm().getPressure() / 1000.0, 4) + " kPa");
        pressureL.setForeground(Color.WHITE);
        pressureL.setFont(new Font("Dialog", Font.BOLD, 18));
        pressureL.setForeground(Color.WHITE);
        layout.putConstraint(SpringLayout.NORTH, pressureL, 15, SpringLayout.SOUTH,
            delimiterL);
        layout.putConstraint(SpringLayout.WEST, pressureL, 0, SpringLayout.WEST,
            this);
        add(pressureL);
        // Temperature
        temperatureL = new JLabel("Temperature: " + roundToSF(r.getAtm().getTemperature(), 4) + " °K");
        temperatureL.setForeground(Color.WHITE);
        temperatureL.setFont(new Font("Dialog", Font.BOLD, 18));
        temperatureL.setForeground(Color.WHITE);
        layout.putConstraint(SpringLayout.NORTH, temperatureL, 5, SpringLayout.SOUTH,
            pressureL);
        layout.putConstraint(SpringLayout.WEST, temperatureL, 0, SpringLayout.WEST,
            this);
        add(temperatureL);
        // Habitability
        String habitabilityBool = r.getHab() ? "<span style='color: green'>true</span>"
            : "<span style='color: red'>false</span>";
        habitabilityL = new JLabel("<html>Habitable: " + habitabilityBool + "</html>");
        habitabilityL.setForeground(Color.WHITE);
        habitabilityL.setFont(new Font("Dialog", Font.BOLD, 18));
        habitabilityL.setForeground(Color.WHITE);
        layout.putConstraint(SpringLayout.NORTH, habitabilityL, 5, SpringLayout.SOUTH,
            temperatureL);
        layout.putConstraint(SpringLayout.WEST, habitabilityL, 0, SpringLayout.WEST,
            this);
        add(habitabilityL);
      }
    } else if (body instanceof GassyPlanet) {
      // Type
      typeL.setText(typeL.getText() + "Gassy planet");
    } else if (body instanceof Star) {
      // Type
      typeL.setText(typeL.getText() + "Star");
    }

    // Updat preferred size depending on body type
    Dimension prefSize = getPreferredSize();
    setPreferredSize(new Dimension((int) prefSize.getWidth(), preferredHeight));
    // Add labels
    layout.putConstraint(SpringLayout.NORTH, typeL, 20, SpringLayout.SOUTH, bodyNameL);
    layout.putConstraint(SpringLayout.WEST, typeL, 0, SpringLayout.WEST, this);
    add(typeL);
    layout.putConstraint(SpringLayout.NORTH, massL, 5, SpringLayout.SOUTH, typeL);
    layout.putConstraint(SpringLayout.WEST, massL, 0, SpringLayout.WEST, this);
    add(massL);
    layout.putConstraint(SpringLayout.NORTH, radiusL, 5, SpringLayout.SOUTH, massL);
    layout.putConstraint(SpringLayout.WEST, radiusL, 0, SpringLayout.WEST, this);
    add(radiusL);
  }

  /**
   * Rounds a value to a certain amount of significant figures
   *
   * @param v  The valie to round
   * @param sf Number of significant figures
   *
   * @return The value rounded to the required amount of significant figuers
   */
  public BigDecimal roundToSF(double v, int sf) {
    BigDecimal bv = new BigDecimal(v);
    int scale = sf - bv.precision() + bv.scale();
    bv = bv.setScale(scale, RoundingMode.HALF_UP);
    return bv;
  }
}
