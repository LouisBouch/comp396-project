package environment;

import environment.*;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import lib.Paintable;
import lib.Vector3D;

/**
 * SolarSystem
 */
public class SolarSystem implements Paintable {

  ArrayList<Body> bodies = new ArrayList<Body>();
  double timeElapsedSeconds = 0;

  /**
   * Constructor for the Solar System
   */
  public SolarSystem() {
    bodies.add(new Star(696340000, 1000, new Vector3D(9e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(2439, 1000, new Vector3D(8.948e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(6051, 1000, new Vector3D(8.892e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(6371, 1000, new Vector3D(8.85e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(3389, 1000, new Vector3D(8.776e9, 0, 100), new Vector3D()));
    bodies.add(new Asteroid(150000000, 1000, new Vector3D(8.671e9, 100, 0), new Vector3D()));
    bodies.add(new GassyPlanet(69911, 1000, new Vector3D(8.245e9, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(60268, 1000, new Vector3D(7.5e9, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(25362, 1000, new Vector3D(6.1e9, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(24622, 1000, new Vector3D(4.6e9, 0, 100), new Vector3D()));
  }

  public void paintThis(Graphics2D g2d) {
    for (Body body : bodies) {
      body.paintThis(g2d);
    }
  }
}
