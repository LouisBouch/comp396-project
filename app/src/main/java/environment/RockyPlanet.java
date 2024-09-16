package environment;

import lib.Vector3D;

import java.awt.*;

/**
 * RockyPlanet
 */
public class RockyPlanet extends Body {


    public RockyPlanet(int radius, int mass, Vector3D position, Vector3D velocity) {
        super(radius, mass, position, velocity);
    }

    @Override
    public void paintThis(Graphics2D g2d) {
        g2d.setColor(Color.BLUE);
        g2d.fillOval(10, 10, 2*radius, radius);
    }
}
