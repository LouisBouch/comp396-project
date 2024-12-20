package graphInterface.simulation.addBody;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.function.Consumer;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import environment.Body;
import environment.Camera3D;
import environment.GassyPlanet;
import environment.RockyPlanet;
import environment.Star;
import environment.Texture;
import environment.habitablity.Gas;
import environment.habitablity.StarType;
import lib.Vector3D;

public class AddBodyContainerP extends JPanel {
  private JLabel titleL;
  private JButton addB;

  private JComboBox<Gas> atmC;
  private JSlider tempS;
  private JComboBox<Texture> texC;
  private JTextField massExpT;
  private JTextField massManT;
  private JTextField radExpT;
  private JTextField radManT;
  private JTextField velExpT;
  private JTextField velManT;
  private JTextField nameT;
  private JLabel velL;
  private JLabel atmL;
  private JLabel tempL;
  private JLabel currentTempL;
  private JLabel texL;
  private JLabel massL;
  private JLabel radL;
  private JLabel nameL;
  private JLabel expMassL = new JLabel("×10^", SwingConstants.CENTER);
  private JLabel expRadL = new JLabel("×10^", SwingConstants.CENTER);
  private JLabel expVelL = new JLabel("×10^", SwingConstants.CENTER);

  private String[] bodyTypes = { "Rocky Planet", "Gas Planet", "Star" };
  private int type;
  private Texture[] textures = { Texture.Mercury, Texture.Venus, Texture.Earth, Texture.Mars, Texture.Jupiter,
      Texture.Saturn, Texture.Uranus, Texture.Neptune, Texture.Moon, Texture.Haumea, Texture.Ceres, Texture.Eris,
      Texture.Pink };
  private Gas[] gasses = { Gas.Earthlike, Gas.Marslike, Gas.Venuslike, Gas.Titanlike, Gas.Vacuum };
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
    hideComponents();
  }

  // Places panels
  public void setUpComps() {
    int fontSize = 20;
    // Temperature values for the slider
    int minTemp = 0;
    int maxTemp = 5000;
    int iniTemp = 0;
    // Labels
    nameL = new JLabel("Name: ");
    nameL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(nameL);
    radL = new JLabel("Radius: ");
    radL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(radL);
    velL = new JLabel("Velocity: ");
    velL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(velL);
    massL = new JLabel("Mass: ");
    massL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(massL);
    texL = new JLabel("Texture: ");
    texL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(texL);
    tempL = new JLabel("Temperature: ");
    tempL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(tempL);
    currentTempL = new JLabel("0°K");
    currentTempL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(currentTempL);
    atmL = new JLabel("Atmosphere: ");
    atmL.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(atmL);
    expRadL.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(expRadL);
    expMassL.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(expMassL);
    expVelL.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(expVelL);

    // Text
    nameT = new JTextField();
    nameT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(nameT);
    radManT = new JTextField();
    radManT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(radManT);
    radExpT = new JTextField();
    radExpT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(radExpT);
    velManT = new JTextField("0");
    velManT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(velManT);
    velExpT = new JTextField("1");
    velExpT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(velExpT);
    massManT = new JTextField();
    massManT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(massManT);
    massExpT = new JTextField();
    massExpT.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(massExpT);
    texC = new JComboBox<Texture>(textures);
    texC.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(texC);
    tempS = new JSlider(minTemp, maxTemp, iniTemp);
    tempS.setMajorTickSpacing(500);
    tempS.setMinorTickSpacing(100);
    tempS.setPaintTicks(true);
    tempS.setOpaque(false);
    tempS.setFont(new Font("Dialog", Font.BOLD, fontSize));
    add(tempS);
    atmC = new JComboBox<Gas>(gasses);
    atmC.setFont(new Font("Dialog", Font.PLAIN, fontSize));
    add(atmC);

    // Title
    titleL = new JLabel("Add " + bodyTypes[type]);
    titleL.setFont(new Font("Comic Sans MS", Font.BOLD, fontSize + 2));
    add(titleL);

    // Button
    addB = new JButton("Add");
    addB.setFont(new Font("Dialog", Font.BOLD, fontSize));
    addB.setFocusable(false);
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
    layout.putConstraint(SpringLayout.WEST, velL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, texL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, tempL, 15, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, atmL, 15, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.NORTH, nameL, spaceBetweenLabels + 50, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.NORTH, radL, spaceBetweenLabels, SpringLayout.SOUTH, nameL);
    layout.putConstraint(SpringLayout.NORTH, massL, spaceBetweenLabels, SpringLayout.SOUTH, radL);
    layout.putConstraint(SpringLayout.NORTH, velL, spaceBetweenLabels, SpringLayout.SOUTH, massL);
    layout.putConstraint(SpringLayout.NORTH, texL, spaceBetweenLabels, SpringLayout.SOUTH, velL);
    layout.putConstraint(SpringLayout.NORTH, tempL, spaceBetweenLabels, SpringLayout.SOUTH, texL);
    layout.putConstraint(SpringLayout.NORTH, atmL, spaceBetweenLabels, SpringLayout.SOUTH, tempL);

    // Property text
    int start = 180;
    int end = 350;
    layout.putConstraint(SpringLayout.WEST, nameT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, radManT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, radExpT, end - 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, velManT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, velExpT, end - 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massManT, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, massExpT, end - 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, texC, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, tempS, start, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.WEST, atmC, start, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.EAST, nameT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, radManT, start + 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, radExpT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, velManT, start + 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, velExpT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, massManT, start + 50, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, massExpT, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, texC, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, tempS, end, SpringLayout.WEST, this);
    layout.putConstraint(SpringLayout.EAST, atmC, end, SpringLayout.WEST, this);

    layout.putConstraint(SpringLayout.VERTICAL_CENTER, nameT, 0, SpringLayout.VERTICAL_CENTER, nameL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, radManT, 0, SpringLayout.VERTICAL_CENTER, radL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, radExpT, 0, SpringLayout.VERTICAL_CENTER, radL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, velManT, 0, SpringLayout.VERTICAL_CENTER, velL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, velExpT, 0, SpringLayout.VERTICAL_CENTER, velL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, massManT, 0, SpringLayout.VERTICAL_CENTER, massL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, massExpT, 0, SpringLayout.VERTICAL_CENTER, massL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, texC, 0, SpringLayout.VERTICAL_CENTER, texL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, tempS, 0, SpringLayout.VERTICAL_CENTER, tempL);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, atmC, 0, SpringLayout.VERTICAL_CENTER, atmL);

    // Title
    layout.putConstraint(SpringLayout.NORTH, titleL, 10, SpringLayout.NORTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, titleL, 0, SpringLayout.HORIZONTAL_CENTER, this);

    // Button
    layout.putConstraint(SpringLayout.SOUTH, addB, -20, SpringLayout.SOUTH, this);
    layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, addB, 0, SpringLayout.HORIZONTAL_CENTER, this);

    // Exponents
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, expMassL, 0, SpringLayout.VERTICAL_CENTER, massL);
    layout.putConstraint(SpringLayout.WEST, expMassL, 0, SpringLayout.EAST, massManT);
    layout.putConstraint(SpringLayout.EAST, expMassL, 0, SpringLayout.WEST, massExpT);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, expRadL, 0, SpringLayout.VERTICAL_CENTER, radL);
    layout.putConstraint(SpringLayout.WEST, expRadL, 0, SpringLayout.EAST, radManT);
    layout.putConstraint(SpringLayout.EAST, expRadL, 0, SpringLayout.WEST, radExpT);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, expVelL, 0, SpringLayout.VERTICAL_CENTER, velL);
    layout.putConstraint(SpringLayout.WEST, expVelL, 0, SpringLayout.EAST, velManT);
    layout.putConstraint(SpringLayout.EAST, expVelL, 0, SpringLayout.WEST, velExpT);

    // Live labels
    layout.putConstraint(SpringLayout.WEST, currentTempL, 15, SpringLayout.EAST, tempS);
    layout.putConstraint(SpringLayout.VERTICAL_CENTER, currentTempL, 0, SpringLayout.VERTICAL_CENTER, tempL);

  }

  /**
   * Sets up listeners
   */
  public void listeners() {
    // Add button listener
    addB.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        try {
          Texture texV;
          Gas atmV;
          double tempV;
          double massExpV = Double.parseDouble(massExpT.getText());
          double massManV = Double.parseDouble(massManT.getText());
          double radExpV = Double.parseDouble(radExpT.getText());
          double radManV = Double.parseDouble(radManT.getText());
          double velExpV = Double.parseDouble(velExpT.getText());
          double velManV = Double.parseDouble(velManT.getText());
          String nameV = nameT.getText();

          double rad = radManV * Math.pow(10, radExpV);
          Vector3D vel = new Vector3D(cam.getCurOrientation()).scalarMult(velManV * Math.pow(10, velExpV));
          Vector3D pos = cam.getCurPosM().copy().add(cam.getCurOrientation().copy().scalarMult(2 * rad));
          double mass = massManV * Math.pow(10, massExpV);
          Body newBody;
          switch (bodyTypes[type]) {
            case "Rocky Planet":
              atmV = (Gas) atmC.getSelectedItem();
              texV = (Texture) texC.getSelectedItem();
              tempV = tempS.getValue();
              newBody = new RockyPlanet(rad, mass, pos, vel, texV, nameV, atmV, tempV);
              break;

            case "Gas Planet":
              texV = (Texture) texC.getSelectedItem();
              newBody = new GassyPlanet(rad, mass, pos, vel, texV, nameV);
              break;

            case "Star":
              newBody = new Star(rad, mass, pos, vel, nameV);
              break;
            default:
              newBody = new GassyPlanet(rad, mass,
                  cam.getCurPosM().copy().add(cam.getCurOrientation().copy().scalarMult(2 * rad)),
                  new Vector3D(0, 0, 0),
                  Texture.Jupiter, nameV);

          }
          add.accept(newBody);
        } catch (Exception er) {
          System.out.println("Invalid value(s)");
        }
      }
    });
    // Temperature slider listener
    tempS.addChangeListener(new ChangeListener() {
      @Override
      public void stateChanged(ChangeEvent e) {
        currentTempL.setText(((JSlider) e.getSource()).getValue() + "°K");
      }
    });
  }

  /**
   * Hides components based on the type of body that is added
   */
  public void hideComponents() {
    switch (bodyTypes[type]) {
      case "Star":
        texL.setVisible(false);
        texC.setVisible(false);
      case "Gas Planet":
        atmC.setVisible(false);
        atmL.setVisible(false);
        tempL.setVisible(false);
        tempS.setVisible(false);
        currentTempL.setVisible(false);
        break;
    }
  }
}
