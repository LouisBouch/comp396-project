package environment;

import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * GassyPlanet
 */
public class GassyPlanet extends Body {

  public GassyPlanet(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName) {
    super(radius, mass, position, velocity, texture, bodyName);
    setColor(Color.red);
  }
  /**
   * Copy constructor
   */
  public GassyPlanet(GassyPlanet planet) {
    super(planet);
    setColor(Color.red);
  }
  /**
   * Creates a copy of the planet
   */
  public GassyPlanet copy() {
    return new GassyPlanet(this);
  }

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2, this.getRadius() * 2);
    g2d.fill(shape);
  }
}
