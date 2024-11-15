package environment;

import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * RockyPlanet
 */
public class RockyPlanet extends Body {
  public RockyPlanet(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName) {
    super(radius, mass, position, velocity, texture, bodyName);
    setColor(Color.blue);
  }
  /**
   * Copy constructor
   */
  public RockyPlanet(RockyPlanet planet) {
    super(planet);
    setColor(Color.blue);
  }
  /**
   * Creates a copy of the planet
   */
  public RockyPlanet copy() {
    return new RockyPlanet(this);
  }

  /**
   * TODO: getTemp() for rocky planets
   */
  public double getTemp(){
    return 0;
  }

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2, this.getRadius() * 2);
    g2d.fill(shape);
  }
}
