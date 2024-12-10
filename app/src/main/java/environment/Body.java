package environment;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import environment.habitablity.StarType;
import lib.Vector3D;

/**
 * Body
 */
public abstract class Body {

  private double radius; // m
  private double mass; // kg

  private Vector3D north; // Body's north-pointing planetary axis vector
  private Vector3D equator; // Body's equator vector (perpendicular to planetary axis)

  private Vector3D position; // Position relative to (0, 0, 0), in m

  private Vector3D velocity; // 3D Velocity in m/s

  private Color color; // Colour in simulation (2D view)
  private Texture texture; // Texture in simulation (3D view)
  private String bodyName; // Body's name

  /**
   * Constructor for body
   * @param radius in meters
   * @param mass in kilograms
   * @param position as 3D vector in meters
   * @param velocity as 3D vector in meters per second
   * @param texture
   * @param bodyName as string
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
   * Copy constructor for a body
   * @param body to be copied
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
   * Abstract copy method for a body
   */
  public abstract Body copy();

  /**
   * Getter for body's radius
   * @return radius in meters
   */
  public double getRadius() {
    return radius;
  }
  /**
   * Setter for body's radius
   * @params newRad, new radius in meters
   */
  public void setRadius(double newRad) {
    radius = newRad;
  }

  /**
   * Getter for body's colour
   * @return colour of body (2D view)
   */

  public Color getColor() {
    return color;
  }

  /**
   * Setter for body's colour
   * @param color
   */
  public void setColor(Color color) {
    this.color = color;
  }

  /**
   * Getter for mass of body
   * @return mass in kg
   */
  public double getMass() {
    return mass;
  }


  /**
   * Getter for body's texture
   * @return texture (3D view)
   */
  public Texture getTexture() {return texture;}

  /**
   * Getter for position of body
   * @return position as a Vector3D in meters
   */
  public Vector3D getPos() {
    return position;
  }

  /**
   * Setter for position of body
   * @param pos as a Vector3D in meters
   */
  public void setPos(Vector3D pos) {
    position = pos;
  }

  /**
   * Getter for velocity of body
   * @return Value of velocity as a Vector3D in meters/second
   */
  public Vector3D getVel() {
    return velocity;
  }

  /**
   * Setter for velocity of body
   * @param vel as a Vector3D in meters/second
   */
  public void setVel(Vector3D vel) {
    velocity = vel;
  }

  /**
   * Obtains x coordinate of body
   * @return x coord in meters
   */
  public double getX() {
    return position.getX();
  }

  /**
   * Obtains y coordinate of body
   * @return y coord in meters
   */
  public double getY() {
    return position.getY();
  }

  /**
   * Obtains z coordinate of body
   * @return z coord in meters
   */
  public double getZ() {
    return position.getZ();
  }

  /**
   * Getter for the vector pointing north of a body
   * @return 3D vector pointing in the north direction
   */
  public Vector3D getNorth() {
    return north;
  }

  /**
   * Getter for the vector pointing to the equator of a body
   * @return 3D vector pointing in the equator's direction
   */
  public Vector3D getEquator() {
    return equator;
  }

  /**
   * Setter for the vector pointing north of a body
   * @param north 3D vector pointing in the north direction
   */
  public void setNorth(Vector3D north) {
    this.north = north;
  }

  /**
   * Setter for the vector pointing to the equator of a body
   * @param equator 3D vector pointing in the equator's direction
   */
  public void setEquator(Vector3D equator) {
    this.equator = equator;
  }

  /**
   * Getter for the texture of the body
   * @return Texture of body (As a UV map)
   */
  public BufferedImage getUVMap() {
    return texture.getUVMap();
  }

  /**
   * Setter for the texture of a body
   * @param t new texture
   */
  public void setTexture(Texture t) {
    texture = t;
  }

  /**
   * Getter for the name of the body
   * @return The body name as a string
   */
  public String getBodyName() {
    return bodyName;
  }

  /**
   * Setter for the name of the body
   * @param bodyName new body name as a string
   */
  public void setBodyName(String bodyName) {
    this.bodyName = bodyName;
  }

  /**
   * Function to combine bodies when they crash into each other
   * The crashed bodies are deleted and replaced with a new body
   *
   * The new body has a radius and mass corresponding to the sum of the crashed bodies' volume and mass
   * Its position is set to the center of mass of the crashed bodies, and it's velocity is
   * modelled as an inelastic collision
   * The new body's name is a concatenation of random substrings of the crashed bodies
   *
   * If there is a star in the collision, the new body is a Star. If not, it is a CrashedPlanet
   *
   * @param crashed list of crashed bodies
   * @return new body resulting from crash
   */
  public static Body bodyCombine(ArrayList<Body> crashed){
    double mass = 0;
    Vector3D pos = new Vector3D();
    Vector3D mom = new Vector3D();
    double vol = 0;
    String name = "";
    Random rand = new Random();
    boolean star = false;
    Body newBod;

    for (Body planet : crashed){
      vol += Math.pow(planet.getRadius(), 3) * 4 /3.0 * Math.PI;
      mass += planet.getMass();
      pos = Vector3D.add(pos, planet.getPos().copy().scalarMult(planet.getMass()));
      mom = Vector3D.add(mom, Vector3D.scalarMult(planet.getVel(), planet.getMass()));
      name += planet.getBodyName().substring(0, rand.nextInt(planet.getBodyName().length())+1);
      if (planet instanceof Star){
        star = true;
      }
    }
    name += "💥";
    double newRad = Math.pow(3*vol/4.0/Math.PI, 1/3.0);

    if (star == true){
      newBod = new Star(newRad, mass, pos.scalarDiv(mass), mom.scalarDiv(mass), name);
    }
    else {
      newBod = new CrashedPlanet(newRad, mass, pos.scalarDiv(mass), mom.scalarDiv(mass), name);
    }
    return newBod;
  }

}
