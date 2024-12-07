package environment;

import java.util.ArrayList;
import java.util.Arrays;

import environment.habitablity.Gas;
import lib.Vector3D;

/**
 * Contains all possible systems
 */
public enum Systems {

  SolarSystem(new ArrayList<Body>() {
    {
      add(new Star(6.9634e8, 1.989e30, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), "Sun"));
      add(new RockyPlanet(2.44e6, 3.302e23, new Vector3D(46006000000.0, 0, 0), new Vector3D(0, 47000, 0),
          Texture.Mercury, "Mercury", Gas.Vacuum, 0));
      add(new RockyPlanet(6.0518e6, 4.867e24, new Vector3D(108.58e9, 0, 0), new Vector3D(0, 35000, 0), Texture.Venus,
          "Venus", Gas.Venuslike, 0));
      add(new RockyPlanet(6378137, 5.9722e24, new Vector3D(149597870700.0, 0, 0), new Vector3D(0, 29784.8, 0),
          Texture.Earth, "Earth", Gas.Earthlike, 293));
      add(new RockyPlanet(3389500, 6.42e23, new Vector3D(235940000000.0, 0, 0), new Vector3D(0, 24080, 0), Texture.Mars,
          "Mars", Gas.Marslike, 210));

      add(new GassyPlanet(69.911e6, 1.898e27, new Vector3D(758.73e9, 0, 0), new Vector3D(0, 13070, 0), Texture.Jupiter,
          "Jupiter"));
      add(new GassyPlanet(58.232e6, 5.683e26, new Vector3D(1.441e12, 0, 0), new Vector3D(0, 9.6391e3, 0),
          Texture.Saturn, "Saturn"));
      add(new GassyPlanet(25.362e6, 8.681e25, new Vector3D(2.9246e12, 0, 0), new Vector3D(0, 6.8e3, 0), Texture.Uranus,
          "Uranus"));
      add(new GassyPlanet(24.622e6, 1.024e26, new Vector3D(4.4715e12, 0, 0), new Vector3D(0, 5.4e3, 0), Texture.Neptune,
          "Neptune"));
    }
  }),
  Trappist(new ArrayList<Body>(){{
    add(new Star(6.9634e8*0.121, 1.989e30*0.089, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), "TRAPPIST-1"));

    add(new RockyPlanet(6.378e6*1.127, 5.9722e24*1.02, new Vector3D(0.01150*1.496e11, 0, 0), new Vector3D(0, 82806.91989, 0), Texture.Crashed, "TRAPPIST-1b", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*1.100, 5.9722e24*1.16, new Vector3D(0.01576*1.496e11, 0, 0), new Vector3D(0, 70796.91277, 0), Texture.Crashed, "TRAPPIST-1c", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*0.788, 5.9722e24*0.297, new Vector3D(0.02219*1.496e11, 0, 0), new Vector3D(0, 59607.97867, 0), Texture.Crashed, "TRAPPIST-1d", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*0.915, 5.9722e24*0.772, new Vector3D(0.02916*1.496e11, 0, 0), new Vector3D(0, 52014.39408, 0), Texture.Crashed, "TRAPPIST-1e", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*1.052, 5.9722e24*0.934, new Vector3D(0.03836*1.496e11, 0, 0), new Vector3D(0, 45334.09240, 0), Texture.Crashed, "TRAPPIST-1f", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*1.154, 5.9722e24*1.148, new Vector3D(0.0467*1.496e11, 0, 0), new Vector3D(0, 41123.52890, 0), Texture.Crashed, "TRAPPIST-1g", Gas.Vacuum, 0));
    add(new RockyPlanet(6.378e6*0.777, 5.9722e24*0.331, new Vector3D(0.0617*1.496e11, 0, 0), new Vector3D(0, 35765.63797, 0), Texture.Crashed, "TRAPPIST-1h", Gas.Vacuum, 0));
  }});

  private ArrayList<Body> bodies;

  Systems(ArrayList<Body> bodies) {
    this.bodies = bodies;
  }

  /**
   * Gets the list of enumeration as a list of strings
   *
   * @return The list of names representating the enum constants
   */
  public static String[] getEnumNamesAsArrayList() {
    return Arrays.stream(Systems.values()).map(Enum::name).toArray(String[]::new);
  }

  /**
   * A getter for the field bodies
   *
   * @return The array of bodies that make up the system
   */
  public ArrayList<Body> getBodies() {
    return bodies;
  }
}
