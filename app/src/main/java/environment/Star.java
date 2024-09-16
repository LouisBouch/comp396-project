package environment;

import lib.Vector3D;

import java.awt.*;

/**
 * Star
 */
public class Star extends Body {


    public Star(int radius, int mass, Vector3D position, Vector3D velocity) {
        super(radius, mass, position, velocity);
    }

    @Override
    public void paintThis(Graphics2D g2d) {
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(10, 10, 2*radius, radius);
    }
}
