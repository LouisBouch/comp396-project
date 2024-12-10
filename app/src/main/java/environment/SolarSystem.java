package environment;

import java.util.ArrayList;

import environment.habitablity.Gas;
import lib.Vector3D;


public class SolarSystem {
  ArrayList<Body> bodies = new ArrayList<Body>();
  private double time = 0; // time elapsed in the simulation in seconds
  double gravity = 6.67430e-11; // Gravitational constant in m^3 kg^-1 s^-2
  Systems system;

  /**
   * Constructor for system
   * @param system default system to populate with
   */
  public SolarSystem(Systems system) {
    this.system = system;
    createSystem();
  }

  /**
   * Creates the bodies and adds them to the bodies list
   */
  public void createSystem() {
    for (Body body: system.getBodies()) {
      bodies.add(body.copy());
    }
  }


  /**
   * Getter for the bodies ArrayList
   * @return The list of bodies (Each of class Body)
   */
  public ArrayList<Body> getBodies() {
    return bodies;
  }

  /**
   * Getter for the stars of the system
   * @return list of stars in system
   */
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
   * Getter for time elapsed in the simulation
   * @return time elapsed in seconds
   */
  public double getTime() {
    return time;
  }

  /**
   * Setter for time elapsed in the simulation
   * @param t New time elapsed in seconds
   */
  public void setTime(double t) {
    time = t;
  }

  /**
   * Step the simulation by moving the bodies and updating the habitability conditions
   * @param dt time step in seconds
   */
  public void step(double dt) {
    if (dt == 0)
      return;
    time += dt;
    bodies = getBodies();
    move(bodies, dt);
    habitability(bodies, dt);
  }

  /**
   * Resets the simulation by removing the bodies and creating them again, and setting the time elapsed to 0
   */
  public void reset() {
    bodies.clear();
    time = 0;
    createSystem();
  }

  /**
   * Setter for the system field
   * @param system New solar system type
   */
  public void setSystem(Systems system) {
    this.system = system;
  }

  /**
   * Move the bodies by calculating the new gravitational force on each body from all the others
   * and updating their position and velocity accordingly.
   *
   * Also, detect crashes of bodies and combine them into new bodies accordingly
   *
   * @param bodies list of all bodies in system
   * @param dt time-step in seconds
   */
  public void move(ArrayList<Body> bodies, double dt) {

    ArrayList<Vector3D> forces = new ArrayList<Vector3D>();

    // Iterate over each body to compute the net gravitational force from every other body
    for (Body body : bodies) {
      Vector3D netForce = new Vector3D(0, 0, 0);
      for (Body other : bodies) {
        if (body != other) { // Avoid self-interaction
          Vector3D force = calculateGravitationalForce(body, other);
          netForce = netForce.add(force);
        }
      }
      forces.add(netForce);
    }
    // Update velocity and position of each body according to the net force
    for (Body body : bodies) {
      int i = bodies.indexOf(body);
      Vector3D force = forces.get(i);

      Vector3D acceleration = force.scale(1.0 / body.getMass());
      Vector3D newVelocity = Vector3D.add(body.getVel(), acceleration.scalarMult(dt));
      body.setVel(newVelocity);

      Vector3D pos = new Vector3D(body.getPos());
      Vector3D newPosition = Vector3D.add(pos, Vector3D.scalarMult(newVelocity, dt));
      body.setPos(newPosition);
    }

    // Detect crashes, deleted crashed bodies, and insert combined body accordingly
    ArrayList<Body> crashed = findCrashes(bodies);
    if (crashed.size() != 0) {
      Body newBody = Body.bodyCombine(crashed);
      int i = bodies.indexOf(crashed.get(0));
      bodies.set(i, newBody);
      for (Body planet : crashed) {
        bodies.remove(planet);
      }
    }
  }

  /**
   * Detect crashes if the center of one body enters within the radius of another
   * @param bodies list of all bodies in system
   * @return list of bodies involved in the crash
   */
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

  /**
   * Calculate the gravitational force between 2 bodies
   * @param body1 body "on which" the force is acting
   * @param body2 body enacting the force
   * @return 3D force vector between the 2 bodies directed towards the second body
   */
  private Vector3D calculateGravitationalForce(Body body1, Body body2) {
    Vector3D pos2 = body2.getPos().copy();
    Vector3D pos = body1.getPos().copy();
    Vector3D r = pos2.sub(pos);
    double distance = r.len();
    double forceMagnitude = gravity * body1.getMass() * body2.getMass() / (distance * distance);

    return r.normalize().scale(forceMagnitude);
  }

  /**
   * Create list of all the stars in the system and
   * update the habitability of rocky planets accordingly
   *
   * @param bodies list of all bodies in the system
   * @param dt time-step in seconds
   */

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
