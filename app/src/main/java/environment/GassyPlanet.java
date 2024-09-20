package environment;

import lib.Vector3D;

import java.awt.*;

/**
 * GassyPlanet
 */
public class GassyPlanet extends Body {


    public GassyPlanet(double radius, double mass, Vector3D position, Vector3D velocity) {
        super(radius, mass, position, velocity);
    }

    @Override
    public void paintThis(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        g2d.fillOval((int) (this.getX()-this.getRadius()), (int) (this.getY()-this.getRadius()), (int) this.getRadius() * 2, (int) this.getRadius() * 2);
    }
}
