package graphInterface.core;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import graphInterface.simulation.SimulationP;

public class MainPanel extends JPanel {
  boolean simRunning = false;

  private static final long serialVersionUID = 6992977983635887677L;

  /**
   * Create the panel.
   */
  public MainPanel(Rectangle dimensions) {
    this.setBounds(new Rectangle(dimensions.width, dimensions.height));
    this.setPreferredSize(new Dimension((int) dimensions.getWidth(), (int) dimensions.getHeight()));

    SpringLayout springLayout = new SpringLayout();
    this.setLayout(springLayout);

    JLabel lblNewLabel = new JLabel("Hi, God");
    lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 30));
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, lblNewLabel, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.NORTH, lblNewLabel, 75, SpringLayout.NORTH, this);
    this.add(lblNewLabel);




    JPanel thisP = this;
    JButton btnNewButton = new JButton("Start Simulation");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!simRunning) {
          SimulationP simPanel = new SimulationP(new Rectangle((int) thisP.getBounds().getWidth() / 4, (int) thisP.getBounds().getHeight() / 4));
          springLayout.putConstraint(SpringLayout.VERTICAL_CENTER, simPanel, 0, SpringLayout.VERTICAL_CENTER, thisP);
          springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, simPanel, 0, SpringLayout.HORIZONTAL_CENTER, thisP);
          thisP.add(simPanel);

          thisP.revalidate();

          simRunning = true;
        }

      }
    });
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnNewButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 30, SpringLayout.NORTH, this);
    this.add(btnNewButton);

  }
}
