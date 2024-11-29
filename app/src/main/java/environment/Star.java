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

    setColor(Color.yellow);
    //this.setTexture(textureFromFile("Sun.jpg"));
  }
  /**
   * Copy constructor
   */
  public Star(Star star) {
    super(star);
    setColor(Color.yellow);
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

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2, this.getRadius() * 2);
    g2d.fill(shape);

  }
}
