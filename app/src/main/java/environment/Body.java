package environment;
import lib.Paintable;
import lib.Vector3D;

import java.awt.*;

/**
 * Body
 */
public abstract class Body implements Paintable {

    int radius;
    int mass;

    Vector3D position;

    Vector3D velocity;

    public Body(int radius, int mass, Vector3D position, Vector3D velocity){
        this.radius = radius;
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
    }

    public double getRadius(){
        return radius;
    }

    /**
     * Abstract class implemented in the respective subclasses: Asteroid, GassyPlanet, RockyPlanet and Star
     * @param g2d The paintbrush
     */
    @Override
    public abstract void paintThis(Graphics2D g2d);

}
