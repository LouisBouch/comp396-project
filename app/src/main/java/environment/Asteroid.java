package environment;

import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Asteroid
 */
public class Asteroid extends Body {

  public Asteroid(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture) {
    super(radius, mass, position, velocity, texture);
    setColor(Color.gray);
  }
  /**
   * Copy constructor
   */
  public Asteroid(Asteroid planet) {
    super(planet);
    setColor(Color.gray);
  }
  /**
   * Creates a copy of the Asteroid
   */
  public Asteroid copy() {
    return new Asteroid(this);
  }

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2, this.getRadius() * 2);
    g2d.fill(shape);
  }
}
