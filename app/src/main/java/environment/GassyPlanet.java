package environment;

import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
public class GassyPlanet extends Body {

  /**
   * Constructor for a gassy planet
   * @param radius in meters
   * @param mass in kilograms
   * @param position as 3D vector in meters
   * @param velocity as 3D vector in meters/second
   * @param texture
   * @param bodyName as string
   */

  public GassyPlanet(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName) {
    super(radius, mass, position, velocity, texture, bodyName);
  }

  /**
   * Copy constructor for gassy planets
   * @param planet to be copied
   */
  public GassyPlanet(GassyPlanet planet) {
    super(planet);
  }

  /**
   * Method to copy gassy planet
   * @return copy of gassy planet
   */
  public GassyPlanet copy() {
    return new GassyPlanet(this);
  }


}
