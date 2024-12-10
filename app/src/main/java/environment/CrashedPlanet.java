package environment;

import lib.Vector3D;

public class CrashedPlanet extends Body {

  /**
   * Constructor for a crashed planet
   * @param radius in meters
   * @param mass in kilograms
   * @param position as 3D vector in meters
   * @param velocity as 3D vector in meters/second
   * @param bodyName as string
   */
  public CrashedPlanet(double radius, double mass, Vector3D position, Vector3D velocity, String bodyName) {
    super(radius, mass, position, velocity, Texture.Crashed, bodyName);
  }

  /**
   * Copy constructor for crashed planet
   * @param planet to be copied
   */
  public CrashedPlanet(CrashedPlanet planet) {
    super(planet);
  }

  /**
   * Method to copy crashed planet
   * @return copy of crashed planet
   */
  @Override
  public Body copy() {
    return new CrashedPlanet(this);
  }

}
