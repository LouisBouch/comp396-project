package graphInterface.simulation.addBody;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import environment.Body;
import environment.Camera3D;
import environment.Star;
import environment.Texture;
import environment.habitablity.Gas;
import environment.habitablity.StarType;
import lib.Vector3D;

public class AddBodyContainerP extends JPanel {
  private JLabel titleL;
  private JButton addB;

  private JComboBox<Gas> atmT;
  private JSlider tempT;
  private JComboBox<Texture> texT;
  private JTextField massExpT;
  private JTextField massManT;
  private JSlider radT;
  private JTextField nameT;
  private JLabel atmL;
  private JLabel tempL;
  private JLabel texL;
  private JLabel massL;
  private JLabel radL;
  private JLabel nameL;
  private JLabel expMassL = new JLabel("*10^");
  private JLabel expRadL = new JLabel("*10^");

  private String[] bodyTypes = { "Rocky Planet", "Gas Planet", "Star" };
  private int type;
  private Texture[] textures = { Texture.Mercury, Texture.Venus, Texture.Earth, Texture.Mars, Texture.Jupiter,
      Texture.Saturn, Texture.Uranus, Texture.Neptune, Texture.Moon, Texture.Haumea, Texture.Ceres, Texture.Eris,
      Texture.Pink };
  private Gas[] gasses = { Gas.Earthlike, Gas.Marslike, Gas.Venuslike, Gas.Mercurylike, Gas.Titanlike, Gas.Vacuum };
  private SpringLayout layout;

  private Consumer<Body> add;
  private Camera3D cam;

  /**
   * Will contain all the options for the new body settings
   *
   * @param type The body type
   */
  public AddBodyContainerP(int type, Consumer<Body> add, Camera3D cam) {
    this.type = type;
    this.add = add;
    this.cam = cam;
    layout = new SpringLayout();
    setLayout(layout);
    setUpComps();
    listeners();
  }

  // Places panels
  public void setUpComps() {
    int fontSize = 20;
    // Labels
    nameL = new JLabel("Name: ");
    nameL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(nameL);
    radL = new JLabel("Radius: ");
    radL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(radL);
    massL = new JLabel("Mass: ");
    massL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(massL);
    texL = new JLabel("Texture: ");
    texL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(texL);
    tempL = new JLabel("Temperature: ");
    tempL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(tempL);
    atmL = new JLabel("Atmosphere: ");
    atmL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(atmL);
    expRadL.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(expRadL);
    expMassL.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(expMassL);

    // Text
    nameT = new JTextField();
    nameT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(nameT);
    radT = new JSlider();
    radT.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(radT);
    massManT = new JTextField();
    massManT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(massManT);
    massExpT = new JTextField();
    massExpT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(massExpT);
    texT = new JComboBox<Texture>(textures);
    texT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(texT);
    tempT = new JSlider();
    tempT.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(tempT);
    atmT = new JComboBox<Gas>(gasses);
    atmT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(atmT);

    // Title
    titleL = new JLabel("Add " + bodyTypes[type]);
    titleL.setFont(new Font("Comic Sans MS", Font.BOLD, fontSize + 2));
    add(titleL);

    // Button
    addB = new JButton("Add");
    addB.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(addB);

    // Constraints
    setUpConsts();
  }

  // Places constraints
  public void setUpConsts() {
    int spaceBetweenLabels = 15;
    // Property label
    layout.putConstraint(SpringLayout.WEST, nameL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, radL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, texL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, tempL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, atmL, 15, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.NORTH, nameL, spaceBetweenLabels + 50, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.NORTH, radL, spaceBetweenLabels, SpringLayout.SOUTH, nameL);
    layout.putConstraint(SpringLayout.NORTH, massL, spaceBetweenLabels, SpringLayout.SOUTH, radL);
    layout.putConstraint(SpringLayout.NORTH, texL, spaceBetweenLabels, SpringLayout.SOUTH, massL);
    layout.putConstraint(SpringLayout.NORTH, tempL, spaceBetweenLabels, SpringLayout.SOUTH, texL);
    layout.putConstraint(SpringLayout.NORTH, atmL, spaceBetweenLabels, SpringLayout.SOUTH, tempL);

    // Property text
    int start = 180;
    int end = 350;
    layout.putConstraint(SpringLayout.WEST, nameT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, radT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massManT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massExpT, start + 120, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, texT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, tempT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, atmT, start, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.EAST, nameT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, radT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, massManT, start + 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, massExpT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, texT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, tempT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, atmT, end, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.VERTICAL_CENTER, nameT, 0, SpringLayout.VERTICAL_CENTER, nameL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, radT, 0, SpringLayout.VERTICAL_CENTER, radL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, massManT, 0, SpringLayout.VERTICAL_CENTER, massL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, massExpT, 0, SpringLayout.VERTICAL_CENTER, massL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, texT, 0, SpringLayout.VERTICAL_CENTER, texL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, tempT, 0, SpringLayout.VERTICAL_CENTER, tempL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, atmT, 0, SpringLayout.VERTICAL_CENTER, atmL);

    // Title
    layout.putConstraint(SpringLayout.NORTH, titleL, 10, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, titleL, 0, SpringLayout.HORIZONTAL_CENTER, this);

    // Button
    layout.putConstraint(SpringLayout.SOUTH, addB, -20, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, addB, 0, SpringLayout.HORIZONTAL_CENTER, this);

  }

  /**
   * Sets up button listeners
   */
  public void listeners() {
    double rad = 6.9634e8;
    addB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        add.accept(
            new Star(rad, 1.989e30, cam.getCurPosM().copy().add(cam.getCurOrientation().copy().scalarMult(2 * rad)),
                new Vector3D(0, 0, 0), ":AJD:AJ:AJA:DA:AJ", StarType.G));
      }
    });
  }
}
