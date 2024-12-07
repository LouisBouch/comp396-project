package graphInterface.core;

import java.awt.Color;
import java.awt.Insets;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

/**
 * landingP
 */
public class HelpP extends JPanel {

  private static final long serialVersionUID = 7992977983635887677L;

  public HelpP() {
    SpringLayout springLayout = new SpringLayout();
    setLayout(springLayout);
    setBackground(Color.decode("#1f1f38"));
    setBorder(null);

    // Welcome label
    JEditorPane welcomePane = new JEditorPane();
    welcomePane.setOpaque(false);// Prevents borders from appearing
    welcomePane.setMargin(new Insets(0, 0, 0, 0));// No margins
    try {
      URL htmlPage = getClass().getResource("/" + "HelpPage.html");
      welcomePane.setPage(htmlPage);
    } catch (IOException e) {
      e.printStackTrace();
    }

    springLayout.putConstraint(SpringLayout.NORTH, welcomePane, 0, SpringLayout.NORTH, this);
    springLayout.putConstraint(SpringLayout.SOUTH, welcomePane, 0, SpringLayout.SOUTH, this);
    springLayout.putConstraint(SpringLayout.WEST, welcomePane, 0, SpringLayout.WEST, this);
    springLayout.putConstraint(SpringLayout.EAST, welcomePane, -100, SpringLayout.EAST, this);
    add(welcomePane);
  }
}
