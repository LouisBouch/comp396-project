package graphInterface.core;

import java.awt.Color;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * landingP
 */
public class LandingP extends JPanel {

  private static final long serialVersionUID = 7992977983635887677L;

  public LandingP() {
    SpringLayout springLayout = new SpringLayout();
    setLayout(springLayout);
    setBackground(Color.decode("#1f1f38"));

    // Welcome label
    JLabel welcomeLabel = new JLabel(
        "<html><div style='text-align: center'>Solar system simulator, <br/> By <br/> Maddy Walkington & Louis Bouchard<div/></html>");
    welcomeLabel.setFont(new Font("Dialog", Font.BOLD, 30));
    welcomeLabel.setForeground(Color.white);
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.NORTH, welcomeLabel, 30, SpringLayout.NORTH, this);
    this.add(welcomeLabel);
    // Frontpage image
    try {
      ImageIcon imageEarth = new ImageIcon(ImageIO.read(getClass().getResource("/" + "EarthFrontPage.png")));
      ImageIcon imageSun = new ImageIcon(ImageIO.read(getClass().getResource("/" + "SunFrontPage.png")));
      JLabel sunL = new JLabel(imageSun);
      add(sunL);
      JLabel earthL = new JLabel(imageEarth);
      add(earthL);

      // Constraints
      springLayout.putConstraint(SpringLayout.EAST, sunL, 0, SpringLayout.HORIZONTAL_CENTER, this);
      springLayout.putConstraint(SpringLayout.NORTH, sunL, 10, SpringLayout.SOUTH, welcomeLabel);
      springLayout.putConstraint(SpringLayout.WEST, earthL, 0, SpringLayout.HORIZONTAL_CENTER, this);
      springLayout.putConstraint(SpringLayout.NORTH, earthL, 10, SpringLayout.SOUTH, welcomeLabel);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
