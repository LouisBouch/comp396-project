package graphInterface.core;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class MainPanel extends JPanel {

  final String APP_NAME = "Application";
  final String LANDING_NAME = "Landing";
  private static final long serialVersionUID = 6992977983635887677L;

  /**
   * Create the panel.
   */
  public MainPanel(Rectangle dimensions) {
    this.setBounds(new Rectangle(dimensions.width, dimensions.height));
    this.setPreferredSize(new Dimension((int) dimensions.getWidth(), (int) dimensions.getHeight()));

    CardLayout cardLayout = new CardLayout();
    this.setLayout(cardLayout);

    // Cards
    LandingP landingP = new LandingP();
    ApplicationP applicationP = new ApplicationP();

    this.add(applicationP, APP_NAME);
    this.add(landingP, LANDING_NAME);


    JPanel thisP = this;
    Font boldFont = new Font("Tahoma", Font.BOLD, 20);

    // Button for switching card to simulation
    JButton btnSwitchFromApp = new JButton("←");
    btnSwitchFromApp.setFont(boldFont);
    btnSwitchFromApp.setFocusable(false);
    btnSwitchFromApp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, LANDING_NAME);
        thisP.revalidate();
      }
    });

    // Button for switching card to simulation
    JButton btnSwitchFromLanding = new JButton("Go to sim");
    btnSwitchFromLanding.setFont(boldFont);
    btnSwitchFromLanding.setFocusable(false);
    btnSwitchFromLanding.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, APP_NAME);
        thisP.revalidate();
      }
    });

    // Place switch button in application panel
    applicationP.add(btnSwitchFromApp);
    SpringLayout simLayout = (SpringLayout) applicationP.getLayout();
    simLayout.putConstraint(SpringLayout.EAST, btnSwitchFromApp, -6, SpringLayout.EAST, applicationP);
    simLayout.putConstraint(SpringLayout.NORTH, btnSwitchFromApp, 6, SpringLayout.NORTH, applicationP);

    // Place switch button in application panel
    landingP.add(btnSwitchFromLanding);
    SpringLayout appLayout = (SpringLayout) landingP.getLayout();
    appLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnSwitchFromLanding, 0, SpringLayout.HORIZONTAL_CENTER, landingP);
    appLayout.putConstraint(SpringLayout.SOUTH, btnSwitchFromLanding, -100, SpringLayout.SOUTH, landingP);

  }
}
