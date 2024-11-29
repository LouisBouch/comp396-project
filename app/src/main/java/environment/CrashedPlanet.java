package environment;

import environment.habitablity.Atmosphere;
import environment.habitablity.Gas;
import lib.Vector3D;

import java.awt.*;

public class CrashedPlanet extends Body{

    public CrashedPlanet(double radius, double mass, Vector3D position, Vector3D velocity, String bodyName) {
        super(radius, mass, position, velocity, Texture.Crashed, bodyName);
    }
    /**
     * Copy constructor
     */
    public CrashedPlanet(CrashedPlanet planet) {
        super(planet);
    }

    @Override
    public Body copy() {
        return null;
    }

    @Override
    public double getTemp() {
        return 0;
    }

}
