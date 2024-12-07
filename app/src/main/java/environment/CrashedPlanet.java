package environment;

import lib.Vector3D;

public class CrashedPlanet extends Body {

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
    return new CrashedPlanet(this);
  }

  @Override
  public double getTemp() {
    return 0;
  }

}
