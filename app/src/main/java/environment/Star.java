package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

import environment.habitablity.StarType;
import lib.Vector3D;

/**
 * Star
 */
public class Star extends Body {

  private StarType starType;
  public Star(double radius, double mass, Vector3D position, Vector3D velocity, String bodyName, StarType starType) {
    super(radius, mass, position, velocity, starType.getTexture(), bodyName);

    this.starType = starType;
  }
  /**
   * Copy constructor
   */
  public Star(Star star) {
    super(star);
  }
  /**
   * Creates a copy of the star
   */
  public Star copy() {
    return new Star(this);
  }

  public double getTemp(){
    return starType.getTemperature();
  }

}
