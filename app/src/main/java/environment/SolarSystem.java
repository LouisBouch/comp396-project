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
    bodies.add(new Star(6.96340e8, 1000, new Vector3D(0, 0, 100), new Vector3D()));
    bodies.add(new Star(6.96340e8, 1000, new Vector3D(2e11, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(2.4397e6, 1000, new Vector3D(53.686e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(6.0518e6, 1000, new Vector3D(108.59e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(6.371e6, 1000, new Vector3D(150.08e9, 0, 100), new Vector3D()));
    bodies.add(new RockyPlanet(3.3895e6, 1000, new Vector3D(224.75e9, 0, 100), new Vector3D()));
    //bodies.add(new Asteroid(150e9, 1000, new Vector3D(405e9, 100, 0), new Vector3D()));
    bodies.add(new GassyPlanet(69.911e6, 1000, new Vector3D(755.31e9, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(58.232e6, 1000, new Vector3D(1.4442e12, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(25.362e6, 1000, new Vector3D(2.9264e12, 0, 100), new Vector3D()));
    bodies.add(new GassyPlanet(24.622e6, 1000, new Vector3D(4.4717e12, 0, 100), new Vector3D()));
  }

  public void paintThis(Graphics2D g2d) {
    for (Body body : bodies) {
      body.paintThis(g2d);
    }
  }
}
