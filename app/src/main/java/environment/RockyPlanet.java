package environment;

import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * RockyPlanet
 */
public class RockyPlanet extends Body {


    public RockyPlanet(double radius, double mass, Vector3D position, Vector3D velocity) {
        super(radius, mass, position, velocity);
    }

    @Override
    public void paintThis(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        Ellipse2D.Double shape = new Ellipse2D.Double(this.getX()-this.getRadius(), this.getY()-this.getRadius(), this.getRadius() * 2, this.getRadius() * 2);
        g2d.fill(shape);
    }
}
