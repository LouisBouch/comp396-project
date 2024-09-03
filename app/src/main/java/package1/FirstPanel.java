package package1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class FirstPanel extends JPanel {

  private static final long serialVersionUID = 4223433857831514467L;

  /**
   * Create the panel.
   */
  public FirstPanel(Rectangle dimensions) {
    this.setBounds(new Rectangle((int) dimensions.getWidth() / 3, (int) dimensions.getHeight() / 3));
    this.setPreferredSize(new Dimension((int) dimensions.getWidth() / 3, (int) dimensions.getHeight() / 3));

    SpringLayout springLayout = new SpringLayout();
    this.setLayout(springLayout);

    JButton btnNewButton = new JButton("Flood everybody");
    btnNewButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Gloo gloo");
      }
    });
    springLayout.putConstraint(SpringLayout.HORIZONTAL_CENTER, btnNewButton, 0, SpringLayout.HORIZONTAL_CENTER, this);
    springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 30, SpringLayout.NORTH, this);
    this.add(btnNewButton);

    this.setBackground(Color.DARK_GRAY);
  }

}
