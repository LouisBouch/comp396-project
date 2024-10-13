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
  private BufferedImage texture;

  private Vector3D position;

  private Vector3D velocity;

  private Color color;

  public Body(double radius, double mass, Vector3D position, Vector3D velocity) {
    this.radius = radius;
    this.mass = mass;
    this.position = position;
    this.velocity = velocity;
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
   * Obtains the texture of the body
   *
   * @return Texture map of body
   */
  public BufferedImage getTexture() {
    return texture;
  }

  /**
   * Sets the texture of the body
   *
   * @param t The new texture
   */
  public void setTexture(BufferedImage t) {
    texture = t;
  }

  public BufferedImage textureFromFile(String resName) {
    // Get texture
    try {
      BufferedImage texture = ImageIO.read(getClass().getResource("/" + resName));
      // BufferedImage textureARGB = new
      // BufferedImage(texture.getWidth(),texture.getHeight(),
      // BufferedImage.TYPE_INT_ARGB);
      // //Convert to transparent image
      // textureARGB.getGraphics().drawImage(texture, 0, 0, null);
      return texture;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
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
