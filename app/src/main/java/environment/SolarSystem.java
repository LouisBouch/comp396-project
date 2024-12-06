package environment;

import java.awt.Graphics2D;
import java.util.ArrayList;

import environment.habitablity.Gas;
import environment.habitablity.StarType;
import lib.Paintable;
import lib.Vector3D;

/**
 * SolarSystem
 */
public class SolarSystem {

  ArrayList<Body> bodies = new ArrayList<Body>();

  private double time = 0;

  double gravity = 6.67430e-11; // Gravitational constant in m^3 kg^-1 s^-2, 6.67430e-11

  /**
   * Constructor for the Solar System
   */
  public SolarSystem() {
    createSystem();
  }

  /**
   * Creates the bodies and adds them to the bodies list
   */
  public void createSystem() {
    bodies.add(new Star(6.9634e8*0.121, 1.989e30*0.089, new Vector3D(0, 0, 0), new Vector3D(0, 0, 0), "TRAPPIST-1"));

    bodies.add(new RockyPlanet(6.378e6*1.127, 5.9722e24*1.02, new Vector3D(0.01150*1.496e11, 0, 0), new Vector3D(0, 82806.91989, 0), Texture.Crashed, "TRAPPIST-1b", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*1.100, 5.9722e24*1.16, new Vector3D(0.01576*1.496e11, 0, 0), new Vector3D(0, 70796.91277, 0), Texture.Crashed, "TRAPPIST-1c", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*0.788, 5.9722e24*0.297, new Vector3D(0.02219*1.496e11, 0, 0), new Vector3D(0, 59607.97867, 0), Texture.Crashed, "TRAPPIST-1d", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*0.915, 5.9722e24*0.772, new Vector3D(0.02916*1.496e11, 0, 0), new Vector3D(0, 52014.39408, 0), Texture.Crashed, "TRAPPIST-1e", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*1.052, 5.9722e24*0.934, new Vector3D(0.03836*1.496e11, 0, 0), new Vector3D(0, 45334.09240, 0), Texture.Crashed, "TRAPPIST-1f", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*1.154, 5.9722e24*1.148, new Vector3D(0.0467*1.496e11, 0, 0), new Vector3D(0, 41123.52890, 0), Texture.Crashed, "TRAPPIST-1g", Gas.Vacuum, 0));
    bodies.add(new RockyPlanet(6.378e6*0.777, 5.9722e24*0.331, new Vector3D(0.0617*1.496e11, 0, 0), new Vector3D(0, 35765.63797, 0), Texture.Crashed, "TRAPPIST-1h", Gas.Vacuum, 0));
  }


  /**
   * Getter for the bodies ArrayList
   *
   * @return The list of bodies (Each of class Body)
   */
  public ArrayList<Body> getBodies() {
    return bodies;
  }

  public ArrayList<Body> getSuns() {

    ArrayList<Body> suns = new ArrayList<Body>();
    bodies = getBodies();

    for (Body body : bodies) {
      if (body instanceof Star) {
        suns.add(body);
      }
    }
    return suns;
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
    habitability(bodies, dt);
  }

  /**
   * Resets the simulation by removing the bodies and creating them again
   */
  public void reset() {
    bodies.clear();
    time = 0;
    createSystem();
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
    ArrayList<Body> crashed = findCrashes(bodies);
    if (crashed.size() != 0) {
      Body newBody = Body.bodyCombine(crashed);
      int i = bodies.indexOf(crashed.get(0));
      bodies.set(i, newBody);
      for (Body planet : crashed) {
        bodies.remove(planet);
      }
      crashed = new ArrayList<Body>();
    }
  }

  public ArrayList<Body> findCrashes(ArrayList<Body> bodies) {
    ArrayList<Body> crashed = new ArrayList<Body>();
    for (Body body1 : bodies) {
      for (Body body2 : bodies) {
        Vector3D pos2 = body2.getPos().copy();
        Vector3D pos = body1.getPos().copy();
        Vector3D r = pos2.sub(pos);
        double distance = r.len();
        if (body1 != body2) { // Avoid self-interaction
          if (distance < body1.getRadius()) {
            if (!crashed.contains(body1)) {
              crashed.add(body1);
            }
            if (!crashed.contains(body2)) {
              crashed.add(body2);
            }
          }
        }
      }
    }
    return crashed;
  }

  // Helper method to calculate gravitational force between two bodies
  private Vector3D calculateGravitationalForce(Body body1, Body body2) {
    Vector3D pos2 = body2.getPos().copy();
    Vector3D pos = body1.getPos().copy();
    Vector3D r = pos2.sub(pos);
    double distance = r.len();
    double forceMagnitude = gravity * body1.getMass() * body2.getMass() / (distance * distance);

    return r.normalize().scale(forceMagnitude);
  }

  public static void habitability(ArrayList<Body> bodies, double dt) {
    ArrayList<Star> suns = new ArrayList<Star>();
    for (Body body : bodies) {
      if (body instanceof Star) {
        suns.add((Star) body);
      }
      if (body instanceof RockyPlanet) {
        if (((RockyPlanet) body).getAtm().getGas() != Gas.Vacuum ) {
          ((RockyPlanet) body).update_habitability(suns, dt);
        }
      }
    }
  }
}
