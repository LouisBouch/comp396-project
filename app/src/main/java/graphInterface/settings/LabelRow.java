package graphInterface.settings;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.SpringLayout;

public class LabelRow extends SettingRowP {
  /**
   * Create a row for a single setting
   */
  public LabelRow(String label) {
    super(40, (v) -> {}, 0, label);
    setPreferredSize(new Dimension(0, this.getRowH()));
    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // The label
    JLabel l = new JLabel(label);
    l.setFont(new Font("Tahoma", Font.BOLD, 24));
    l.setHorizontalAlignment(JLabel.LEFT);
    this.add(l);

    // Label constraint
    layout.putConstraint(SpringLayout.WEST, l, 15, SpringLayout.WEST, this);
  }
}
