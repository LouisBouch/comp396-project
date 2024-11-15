package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

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
  private String bodyName;

  /**
   * Create new body
   */
  public Body(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName) {
    // Sets default texture if not specified
    if (texture == null) {
      texture = Texture.Moon;
    }

    this.radius = radius;
    this.mass = mass;
    this.position = position;
    this.velocity = velocity;
    this.north = new Vector3D(0, 0, 1);
    this.equator = new Vector3D(1, 0, 0);
    this.texture = texture;
    this.bodyName = bodyName;
  }

  /**
   * Copy body
   */
  public Body(Body body) {
    radius = body.radius;
    mass = body.mass;
    position = new Vector3D(body.position);
    velocity = new Vector3D(body.velocity);
    north = new Vector3D(body.north);
    equator = new Vector3D(body.equator);
    texture = body.texture;
  }

  /**
   * Copy method to allow us to make copies of any body
   */
  public abstract Body copy();

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

  public Texture getTexture() {return texture;}

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

  public void setVel(Vector3D vel) {
    velocity = vel;
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
   * TODO: Temperature for rocky planets (atmosphere) + others
   */
  public abstract double getTemp();

  public static Body bodyCombine(ArrayList<Body> crashed){
    double mass = 0;
    Vector3D pos = new Vector3D();
    Vector3D mom = new Vector3D();
    double vol = 0;
    String name = "";
    Random rand = new Random();

    for (Body planet : crashed){
      vol += Math.pow(planet.getRadius(), 3) * 4 /3.0 * Math.PI;
      mass += planet.getMass();
      pos = Vector3D.add(pos, planet.getPos().copy().scalarMult(planet.getMass()));
      mom = Vector3D.add(mom, Vector3D.scalarMult(planet.getVel(), planet.getMass()));
      name += planet.getBodyName().substring(0, rand.nextInt(planet.getBodyName().length())+1);

    }
    name += "💥";
    double newRad = Math.pow(3*vol/4.0/Math.PI, 1/3.0);
    Body newBod = new RockyPlanet(newRad, mass, pos.scalarDiv(mass), mom.scalarDiv(mass), Texture.Crashed, name, null);
    return newBod;
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
   * Obtains the name of the body
   *
   * @return The body name
   */
  public String getBodyName() {
    return bodyName;
  }
  /**
   * Obtains the name of the body
   *
   * @param bodyName The new body name
   */
  public void setBodyName(String bodyName) {
    this.bodyName = bodyName;
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
