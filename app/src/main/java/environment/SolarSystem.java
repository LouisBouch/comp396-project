package environment;

import java.awt.Graphics2D;
import java.util.ArrayList;

import lib.Paintable;
import lib.Vector3D;

/**
 * SolarSystem
 */
public class SolarSystem implements Paintable {

  ArrayList<Body> bodies = new ArrayList<Body>();

  private double time = 0;

  double G = 6.67430e24; // Gravitational constant in m^3 kg^-1 s^-2, 6.67430e-11
  boolean crash = false;
  ArrayList<Body> crashed = new ArrayList<Body>();

  /**
   * Constructor for the Solar System
   */
  public SolarSystem() {
    bodies.add(new Star(6.96340e8, 100, new Vector3D(0, 0, 100), new Vector3D(0, 0, 0)));
    bodies.add(new RockyPlanet(3e8, 1, new Vector3D(1.5e10, 0, 100), new Vector3D(0, 20e7, 0), Texture.Earth));
    bodies.add(new RockyPlanet(3e8, 1, new Vector3D(3e10, 0, 100), new Vector3D(0, 10e7, 0), Texture.Mars));
    // bodies.add(new RockyPlanet(4e9, 1000, new Vector3D(2e11, 0, 100), new
    // Vector3D(), Texture.Pink));
    // bodies.add(new GassyPlanet(7e9, 1000, new Vector3D(4e11, 0, 100), new
    // Vector3D()));
    // bodies.add(new RockyPlanet(2.4397e6, 1000, new Vector3D(53.686e9, 0, 100),
    // new Vector3D()));
    // bodies.add(new RockyPlanet(6.0518e6, 1000, new Vector3D(108.59e9, 0, 100),
    // new Vector3D()));
    // bodies.add(new RockyPlanet(6.371e6, 1000, new Vector3D(150.08e9, 0, 100), new
    // Vector3D()));
    // bodies.add(new RockyPlanet(3.3895e6, 1000, new Vector3D(224.75e9, 0, 100),
    // new Vector3D()));
    // bodies.add(new Asteroid(150e9, 1000, new Vector3D(405e9, 100, 0), new
    // Vector3D()));
    // bodies.add(new GassyPlanet(69.911e6, 1000, new Vector3D(755.31e9, 0, 100),
    // new Vector3D()));
    // bodies.add(new GassyPlanet(58.232e6, 1000, new Vector3D(1.4442e12, 0, 100),
    // new Vector3D()));
    // bodies.add(new GassyPlanet(25.362e6, 1000, new Vector3D(2.9264e12, 0, 100),
    // new Vector3D()));
    // bodies.add(new GassyPlanet(24.622e6, 1000, new Vector3D(4.4717e12, 0, 100),
    // new Vector3D()));
  }

  public void paintThis(Graphics2D g2d) {
    for (Body body : bodies) {
      body.paintThis(g2d);
    }
  }

  /**
   * Getter for the bodies ArrayList
   *
   * @return The list of bodies (Each of class Body)
   */
  public ArrayList<Body> getBodies() {
    return bodies;
  }

  /**
   * Obtain time of the simulation
   *
   * @return Time in seconds inside the simulation
   */
  public double getTime() {
    return time;
  }

  /**
   * Sets time of the simulation
   *
   * @param t New time in seconds inside the simulation
   */
  public void setTime(double t) {
    time = t;
  }

  public void step(double dt) {
    // Pointless to step if time does not change
    if (dt == 0)
      return;
    time += dt;
    // System.out.println(time);
    bodies = getBodies();
    move(bodies, dt);
  }

  // TODO: Make solarSystem.reset()
  public void reset() {
  }

  public void move(ArrayList<Body> bodies, double dt) {

    ArrayList<Vector3D> forces = new ArrayList<Vector3D>();

    // Iterate over each body to calculate forces from other bodies
    for (Body body : bodies) {
      // Reset net force for this body
      Vector3D netForce = new Vector3D(0, 0, 0);

      // Compute gravitational force from every other body
      for (Body other : bodies) {
        if (body != other) { // Avoid self-interaction
          Vector3D force = calculateGravitationalForce(body, other);
          netForce = netForce.add(force);
        }
      }
      forces.add(netForce);
    }

    for (Body body : bodies) {
      int i = bodies.indexOf(body);
      Vector3D force = forces.get(i);
      // Update velocity of the body
      Vector3D acceleration = force.scale(1.0 / body.getMass());
      Vector3D newVelocity = Vector3D.add(body.getVel(), acceleration.scalarMult(dt));
      body.setVel(newVelocity);

      // Update position of the body
      Vector3D pos = new Vector3D(body.getPos());
      Vector3D newPosition = Vector3D.add(pos, Vector3D.scalarMult(newVelocity, dt));
      body.setPos(newPosition);
    }

    if (crash) {
      Body newBody = Body.starCombine(crashed);
      int i = bodies.indexOf(crashed.get(0));
      bodies.set(i, newBody);
      for (Body planet : crashed) {
        bodies.remove(planet);
      }
      crashed = new ArrayList<Body>();
      crash = false;
    }
  }

  // Helper method to calculate gravitational force between two bodies
  private Vector3D calculateGravitationalForce(Body body1, Body body2) {
    Vector3D pos2 = body2.getPos().copy();
    Vector3D pos = body1.getPos().copy();
    Vector3D r = pos2.sub(pos);
    double distance = r.len();
    double forceMagnitude = G * body1.getMass() * body2.getMass() / (distance * distance);
    if (distance < body1.getRadius()) {
      forceMagnitude = 0;
      crash = true;
      if (!crashed.contains(body1)) {
        crashed.add(body1);
      }
      if (!crashed.contains(body2)) {
        crashed.add(body2);
      }
    }
    return r.normalize().scale(forceMagnitude);
  }
}
