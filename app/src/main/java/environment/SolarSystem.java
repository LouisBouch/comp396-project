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

  public SolarSystem() {
    bodies.add(new Asteroid(1000, 1000, new Vector3D(100, 100, 100), new Vector3D()));
  }

  public void paintThis(Graphics2D g2d) {
    for (Body body : bodies) {
      body.paintThis(g2d);
    }
  }
}
