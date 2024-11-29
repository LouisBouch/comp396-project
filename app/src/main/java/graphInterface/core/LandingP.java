package graphInterface.core;

import java.awt.Font;

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
    this.setLayout(springLayout);

    // Welcome label
    JLabel welcomeLabel = new JLabel("Live laugh love");
    welcomeLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, welcomeLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.NORTH, welcomeLabel, 30, SpringLayout.NORTH, this);
    this.add(welcomeLabel);

  }
}
