package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import lib.Paintable;
import lib.Vector3D;

/**
 * Body
 */
public abstract class Body implements Paintable {

  private double radius;
  private double mass;

  private Vector3D north;
  private Vector3D equator;

  private Vector3D position;

  private Vector3D velocity;

  private Color color;

  private Texture texture;


  public Body(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture) {
    this.radius = radius;
    this.mass = mass;
    this.position = position;
    this.velocity = velocity;
    this.north = new Vector3D(0, 0, 1);
    this.equator = new Vector3D(1, 0, 0);
    this.texture = texture;
  }

  /**
   * Obtains radius of Body
   * 
   * @return Value of radius in meters
   */
  public double getRadius() {
    return radius;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Obtains mass of Body
   * 
   * @return Value of mass in kg
   */
  public double getMass() {
    return mass;
  }

  /**
   * Obtains position of body
   * 
   * @return Value of position as a Vector3D in meters
   */
  public Vector3D getPos() {
    return position;
  }

  public void setPos(Vector3D pos) {
    position = pos;
  }

  /**
   * Obtains velocity of body
   * 
   * @return Value of velocity as a Vector3D in meters/second
   */
  public Vector3D getVel() {
    return velocity;
  }

  /**
   * Obtains X position of Body
   * 
   * @return Value of X position in meters
   */
  public double getX() {
    return position.getX();
  }

  /**
   * Obtains Y position of Body
   * 
   * @return Value of Y position in meters
   */
  public double getY() {
    return position.getY();
  }

  /**
   * Obtains Z position of Body
   * 
   * @return Value of Z position in meters
   */
  public double getZ() {
    return position.getZ();
  }

  /**
   * Obtain the vector pointing north
   *
   * @return The VectorPointing in the north direction
   */
  public Vector3D getNorth() {
    return north;
  }

  /**
   * Obtain the vector pointing at the equator
   *
   * @return The Vector pointing towrads the equator
   */
  public Vector3D getEquator() {
    return equator;
  }

  /**
   * Obtain the vector pointing north
   *
   * @param north The VectorPointing in the north direction
   */
  public void setNorth(Vector3D north) {
    this.north = north;
  }

  /**
   * Obtain the vector pointing at the equator
   *
   * @param equator The Vector pointing towrads the equator
   */
  public void setEquator(Vector3D equator) {
    this.equator = equator;
  }

  /**
   * Obtains the texture of the body
   *
   * @return Texture of body (As a UV map)
   */
  public BufferedImage getUVMap() {
    return texture.getUVMap();
  }

  /**
   * Sets the texture of the body
   *
   * @param t The new texture
   */
  public void setTexture(Texture t) {
    texture = t;
  }

  /**
   * Abstract class implemented in the respective subclasses: Asteroid,
   * GassyPlanet, RockyPlanet and Star
   * 
   * @param g2d The paintbrush
   */
  @Override
  public abstract void paintThis(Graphics2D g2d);

}
