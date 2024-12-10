package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import environment.habitablity.StarType;
import lib.Vector3D;

public class Star extends Body {

  private StarType starType;

  /**
   * Constructor for a star
   * @param radius in meters
   * @param mass in kilograms
   * @param position as 3D vector in meters
   * @param velocity as 3D vector in meters/second
   * @param bodyName as string
   */
  public Star(double radius, double mass, Vector3D position, Vector3D velocity, String bodyName) {
    super(radius, mass, position, velocity, StarType.getStarType(mass).getTexture(), bodyName);
    this.starType = StarType.getStarType(mass);
  }
  /**
   * Copy constructor for a star
   * @param star to be copied
   */
  public Star(Star star) {
    super(star);
    this.starType = StarType.getStarType(getMass());
  }
  /**
   * Method to copy star
   * @return copy of star
   */
  public Star copy() {
    return new Star(this);
  }

  /**
   * Getter for the temperature of a tar based off it's type
   * @return temperature of star in Kelvin
   */
  public double getTemp(){
    return starType.getTemperature();
  }

}
