package environment;

import lib.Vector3D;

import java.awt.*;

/**
 * Asteroid
 */
public class Asteroid extends Body  {

    public Asteroid(int radius, int mass, Vector3D position, Vector3D velocity) {
        super(radius, mass, position, velocity);
    }

    @Override
    public void paintThis(Graphics2D g2d) {
        g2d.setColor(Color.GRAY);
        g2d.fillOval((int) this.getX(), (int) this.getY(), (int) this.getRadius() * 2, (int) this.getRadius() * 2);
    }
}
