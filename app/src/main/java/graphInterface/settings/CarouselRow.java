package graphInterface.settings;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class CarouselRow extends SettingRowP {
  // Button that allows to go to the previous setting value
  JButton leftB;
  // Button that allows to go to the next setting value
  JButton rightB;
  // Current setting value
  JLabel setting;
  // Current index
  int curIndex;
  // Default index
  int defIndex;
  ArrayList<Map.Entry<String, Double>> settingValues;

  /**
   * Create a row for a single setting
   */
  public CarouselRow(String label, ArrayList<Map.Entry<String, Double>> settingValues, int startIndex,
      Consumer<Double> applySetting) {
    super(30, applySetting, settingValues.get(startIndex).getValue(), label);
    this.settingValues = settingValues;
    curIndex = startIndex;
    defIndex = curIndex;
    setPreferredSize(new Dimension(0, this.getRowH()));
    SpringLayout layout = new SpringLayout();
    setLayout(layout);

    // The label
    JLabel l = new JLabel(label + ":");
    l.setFont(new Font("Tahoma", Font.PLAIN, 22));
    l.setHorizontalAlignment(JLabel.LEFT);
    add(l);

    // The inner label that contains the current setting value
    setting = new JLabel(settingValues.get(startIndex).getKey());
    setting.setFont(new Font("Tahoma", Font.PLAIN, 18));
    setting.setHorizontalAlignment(SwingConstants.CENTER);
    add(setting);

    // Going left button
    leftB = new JButton("<");
    add(leftB);

    // Going right button
    rightB = new JButton(">");
    add(rightB);

    // Constraints
    layout.putConstraint(SpringLayout.WEST, l, 40, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, l, 0, SpringLayout.VERTICAL_CENTER, this);

    layout.putConstraint(SpringLayout.EAST, rightB, -20, SpringLayout.EAST, this);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, rightB, 0, SpringLayout.VERTICAL_CENTER, this);

    layout.putConstraint(SpringLayout.EAST, setting, -5, SpringLayout.WEST, rightB);
    layout.putConstraint(SpringLayout.WEST, setting, 5, SpringLayout.EAST, leftB);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, setting, 0, SpringLayout.VERTICAL_CENTER, this);

    layout.putConstraint(SpringLayout.WEST, leftB, -this.getSettingW(), SpringLayout.EAST, this);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, leftB, 0, SpringLayout.VERTICAL_CENTER, this);

    CarouselRow thisP = this;
    // Right button listener
    rightB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        curIndex = (curIndex + 1) % settingValues.size();
        thisP.setCurV(settingValues.get(curIndex).getValue());
        setting.setText(settingValues.get(curIndex).getKey());
      }
    });
    // Left button listener
    leftB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent arg0) {
        curIndex = (curIndex - 1 + settingValues.size()) % settingValues.size();
        thisP.setCurV(settingValues.get(curIndex).getValue());
        setting.setText(settingValues.get(curIndex).getKey());
      }
    });
  }

  @Override
  public void resetValue() {
    super.resetValue();
    curIndex = defIndex;
    setting.setText(settingValues.get(curIndex).getKey());
  }

  // sets the current value of the carousel
  @Override
  public void setCurV(double v) {
    super.setCurV(v);
    // Get index from value
    for (int i = 0; i < settingValues.size(); i++) {
      Map.Entry<String, Double> tuple = settingValues.get(i);
      if (tuple.getValue() == v) {
        curIndex = i;
        setting.setText(settingValues.get(curIndex).getKey());
      }
    }
  }
}
