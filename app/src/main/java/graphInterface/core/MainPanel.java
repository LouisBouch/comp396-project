package graphInterface.core;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.LayoutManager;
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
  final String HELP_NAME = "Help";
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
    HelpP helpP = new HelpP();

    add(landingP, LANDING_NAME);
    add(applicationP, APP_NAME);
    add(helpP, HELP_NAME);

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
    btnSwitchFromLanding.setOpaque(true);
    btnSwitchFromLanding.setBackground(Color.decode("#454569"));
    btnSwitchFromLanding.setForeground(Color.white);
    btnSwitchFromLanding.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, APP_NAME);
        thisP.revalidate();
        applicationP.getSimPanel().getSolarSystem().setSsytem((Systems) systemCB.getSelectedItem());
        applicationP.getSimPanel().reset();
      }
    });

    // Button for switching card to help page
    JButton btnSwitchToHelp = new JButton("Help");
    btnSwitchToHelp.setFont(smallBoldFont);
    btnSwitchToHelp.setFocusable(false);
    btnSwitchToHelp.setBackground(Color.decode("#454569"));
    btnSwitchToHelp.setForeground(Color.white);
    btnSwitchToHelp.setOpaque(true);
    btnSwitchToHelp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, HELP_NAME);
        thisP.revalidate();
        applicationP.getSimPanel().reset();
        applicationP.getSimPanel().stop();
      }
    });

    // Button for switching card back to homepage from help page
    JButton btnSwitchFromHelp = new JButton("←");
    btnSwitchFromHelp.setFont(smallBoldFont);
    btnSwitchFromHelp.setFocusable(false);
    btnSwitchFromHelp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        cardLayout.show(thisP, LANDING_NAME);
        thisP.revalidate();
      }
    });

    // Place switch button in application panel
    applicationP.getTopP().add(btnSwitchFromApp);
    SpringLayout simLayout = (SpringLayout) applicationP.getTopP().getLayout();
    simLayout.putConstraint(SpringLayout.EAST, btnSwitchFromApp, -6, SpringLayout.EAST, applicationP.getTopP());
    simLayout.putConstraint(SpringLayout.NORTH, btnSwitchFromApp, 6, SpringLayout.NORTH, applicationP.getTopP());

    // Place switch button in help panel
    SpringLayout helpLayout = (SpringLayout) helpP.getLayout();
    helpP.add(btnSwitchFromHelp);
    helpLayout.putConstraint(SpringLayout.EAST, btnSwitchFromHelp, -6, SpringLayout.EAST, helpP);
    helpLayout.putConstraint(SpringLayout.NORTH, btnSwitchFromHelp, 6, SpringLayout.NORTH, helpP);

    // Place switch button in landing panel
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
