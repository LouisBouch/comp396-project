package environment;

import lib.Paintable;
import lib.Vector3D;
import lib.Quaternion;

import java.awt.*;

public class Camera3D implements Paintable {

  Vector3D positionInSpaceM;
  Vector3D orientation;
  Vector3D xAxisDirection;
  Vector3D yAxisDirection;

  SolarSystem solarSystem;

  /**
   * Equal to width/height
   */
  double screenRatio;
  /**
   * Horizontal FOV in degrees
   */
  double hFOV;
  /**
   * Vertical FOV in degrees
   */
  double vFOV;

  Quaternion rotQ;

  double DEGPERRAD = 180 / Math.PI;
  double RAGPERDEG = 1 / DEGPERRAD;

  /**
   * Creates basic camera looking upwards (z)
   *
   * @param positionInSpaceM The position of the camera in space in meters
   * @param solarSystem      The solar system in which the camera belongs
   */
  public Camera3D(Vector3D positionInSpaceM, SolarSystem solarSystem) {
    this.solarSystem = solarSystem;
    this.positionInSpaceM = positionInSpaceM;
    orientation = new Vector3D(0, 0, 1);
    xAxisDirection = new Vector3D(1, 0, 0);
    yAxisDirection = new Vector3D(0, 1, 0);
    rotQ = new Quaternion();
  }

  /**
   * Go over every Body in the solarSystem and paint them on the screen
   *
   * @param g2d The graphics component
   */
  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(Color.yellow);
    Vector3D bodyPos;
    Vector3D cameraToBodyVec;
    for (Body body : solarSystem.getBodies()) {
      bodyPos = body.getPos();
      cameraToBodyVec = Vector3D.sub(positionInSpaceM, bodyPos);
    }
  }

  /**
   * Sets the screen ratio of the camera (width/height)
   *
   * @param screenRatio The new screen ration
   */
  public void setScreenRatio(double screenRatio) {
    this.screenRatio = screenRatio;
    vFOV = 2 * Math.atan(Math.tan(hFOV * RAGPERDEG / 2.0) / screenRatio) * DEGPERRAD;
  }

  /**
   * Sets the horizontal FOV of the camera in degrees
   *
   * @param hFOV The new horizontal FOV
   */
  public void setHFOV(double hFOV) {
    this.hFOV = hFOV;
  }

  /**
   * Gets the horizontal FOV of the camera
   *
   * @return The horizontal FOV of the camera
   */
  public double getHFOV() {
    return hFOV;
  }

  /**
   * Gets the vertical FOV of the camera
   *
   * @return The vertical FOV of the camera
   */
  public double getVFOV() {
    return vFOV;
  }
}
