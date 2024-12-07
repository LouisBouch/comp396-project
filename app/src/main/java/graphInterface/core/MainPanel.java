package graphInterface.core;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import environment.Systems;

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
    setLayout(cardLayout);

    // Cards
    LandingP landingP = new LandingP();
    ApplicationP applicationP = new ApplicationP();

    add(landingP, LANDING_NAME);
    add(applicationP, APP_NAME);

    JPanel thisP = this;
    Font boldFont = new Font("Dialog", Font.BOLD, 30);
    Font smallBoldFont = new Font("Dialog", Font.BOLD, 20);

    // Button for switching card to simulation
    JButton btnSwitchFromApp = new JButton("←");
    btnSwitchFromApp.setFont(smallBoldFont);
    btnSwitchFromApp.setFocusable(false);
    btnSwitchFromApp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, LANDING_NAME);
        thisP.revalidate();
        applicationP.getSimPanel().stop();
      }
    });

    // JComboBox that determines which system to initialize
    JComboBox<Systems> systemCB = new JComboBox<>(Systems.values());
    systemCB.setFont(smallBoldFont);
    systemCB.setFocusable(false);

    // Button for switching card to simulation
    JButton btnSwitchFromLanding = new JButton("Go to sim");
    btnSwitchFromLanding.setFont(boldFont);
    btnSwitchFromLanding.setFocusable(false);
    btnSwitchFromLanding.setBackground(Color.decode("#454569"));
    btnSwitchFromLanding.setForeground(Color.white);
    btnSwitchFromLanding.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, APP_NAME);
        thisP.revalidate();
        applicationP.getSimPanel().getSolarSystem().setSsytem((Systems) systemCB.getSelectedItem());
        applicationP.getSimPanel().reset();
        applicationP.getSimPanel().start();
      }
    });

    // Button for switching card to help page
    JButton btnSwitchToHelp = new JButton("Help");
    btnSwitchToHelp.setFont(smallBoldFont);
    btnSwitchToHelp.setFocusable(false);
    btnSwitchToHelp.setBackground(Color.decode("#454569"));
    btnSwitchToHelp.setForeground(Color.white);
    btnSwitchToHelp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, APP_NAME);
        thisP.revalidate();
        applicationP.getSimPanel().reset();

        applicationP.getSimPanel().start();
      }
    });

    // Place switch button in application panel
    applicationP.getTopP().add(btnSwitchFromApp);
    SpringLayout simLayout = (SpringLayout) applicationP.getTopP().getLayout();
    simLayout.putConstraint(SpringLayout.EAST, btnSwitchFromApp, -6, SpringLayout.EAST, applicationP.getTopP());
    simLayout.putConstraint(SpringLayout.NORTH, btnSwitchFromApp, 6, SpringLayout.NORTH, applicationP.getTopP());

    // Place switch button in application panel
    landingP.add(btnSwitchFromLanding);
    landingP.add(btnSwitchToHelp);
    SpringLayout appLayout = (SpringLayout) landingP.getLayout();
    appLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnSwitchFromLanding, 0, SpringLayout.HORIZONTAL_CENTER,
        landingP);
    appLayout.putConstraint(SpringLayout.SOUTH, btnSwitchFromLanding, -100, SpringLayout.SOUTH, landingP);
    appLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnSwitchToHelp, 0, SpringLayout.HORIZONTAL_CENTER,
        landingP);
    appLayout.putConstraint(SpringLayout.NORTH, btnSwitchToHelp, 10, SpringLayout.SOUTH, btnSwitchFromLanding);

    // Place combobox
    landingP.add(systemCB);
    appLayout.putConstraint(SpringLayout.VERTICAL_CENTER, systemCB, 0, SpringLayout.VERTICAL_CENTER,
        btnSwitchFromLanding);
    appLayout.putConstraint(SpringLayout.WEST, systemCB, 10, SpringLayout.EAST, btnSwitchFromLanding);

  }
}
