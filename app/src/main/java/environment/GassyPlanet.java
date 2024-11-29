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
  }
  /**
   * Copy constructor
   */
  public GassyPlanet(GassyPlanet planet) {
    super(planet);
  }
  /**
   * Creates a copy of the planet
   */
  public GassyPlanet copy() {
    return new GassyPlanet(this);
  }

  /**
   * TODO: getTemp() for Gassy planets
   */
  public double getTemp(){
    return 0;
  }

}
