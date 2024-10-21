package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import lib.Vector3D;

/**
 * Star
 */
public class Star extends Body {

  public Star(double radius, double mass, Vector3D position, Vector3D velocity) {
    super(radius, mass, position, velocity, Texture.Sun);
    setColor(Color.yellow);
    //this.setTexture(textureFromFile("Sun.jpg"));
  }

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2, this.getRadius() * 2);
    g2d.fill(shape);

  }
}
