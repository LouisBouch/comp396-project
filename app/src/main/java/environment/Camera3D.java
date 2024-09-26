package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.AffineTransform;

import lib.Paintable;
import lib.Quaternion;
import lib.Vector3D;

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
  double screenWidthP;
  double screenHeightP;

  Quaternion rotQ;

  double DEGPERRAD = 180 / Math.PI;
  double RADPERDEG = 1 / DEGPERRAD;

  // During computation, scale everything down to alleviate numerical errors
  double scaleDown;

  /**
   * Creates basic camera looking upwards (z)
   *
   * @param positionInSpaceM The position of the camera in space in meters
   * @param solarSystem      The solar system in which the camera belongs
   */
  public Camera3D(Vector3D positionInSpaceM, SolarSystem solarSystem, double hFOV, double screenRatio) {
    this.solarSystem = solarSystem;
    this.positionInSpaceM = positionInSpaceM;
    orientation = new Vector3D(0, 0, 1);
    xAxisDirection = new Vector3D(1, 0, 0);
    yAxisDirection = new Vector3D(0, 1, 0);
    rotQ = new Quaternion();
    setHFOV(hFOV);
    setScreenRatioW(screenRatio);
  }

  /**
   * Go over every Body in the solarSystem and paint them on the screen
   *
   * @param g2d The graphics component
   */
  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(Color.yellow);
    Vector3D bodyPos = new Vector3D();// Position of the body
    Vector3D cameraToBodyVec = new Vector3D();// Vector from camera position to body
    Vector3D projectionToCenter = new Vector3D();// Projection onto the camera view diretion
    Vector3D projectionToScreen = new Vector3D();// Projection to plane perpendicular to camera view
    Vector3D projectionToX = new Vector3D();// Projection on x axis as seen by camera
    Vector3D projectionToY = new Vector3D();// Projection on y axis as seen by camera

    double ellipseRotAngleR;// Rotation of ellipse to align it towards the center of the screen
    double bodyRad;// Radius of the body used

    double screenWidthM;// Width of plane intersecting the body
    double screenHeightM;// Height of plane intersecting the body
    double projectionWidthM;// Projection of cameraToBodyVec (onto plane intersecting with body) width in meters as seen by
    double angularPositionR;// Angle between camera axis and cameraToBodyVec
    double angularDiameterDeg;// Angular diameter of body
    // pinhole camera
    double projectionHeightM;// Projection height in meters ass seen by pinhole camera

    double xDistM;// Length of projectionToX, negative if in the negative x direction
    double yDistM;

    double distToCentOfProj;// The distance to the center of the projected sphere starting from the center
                            // of the image (in meters)
    double distToCentOfProjX;
    double distToCentOfProjY;
    Ellipse2D bodyProjection = new Ellipse2D.Double();
    AffineTransform originalTransform = g2d.getTransform();
    double omega;// Used to compute the minor axis
    for (Body body : solarSystem.getBodies()) {
      bodyPos.setComponents(body.getPos());
      cameraToBodyVec.setComponents(bodyPos).sub(positionInSpaceM);

      // Scale everything down by the distance from the body to the camera
      scaleDown = cameraToBodyVec.len();
      cameraToBodyVec.scalarDiv(scaleDown);
      bodyRad = body.getRadius()/scaleDown;


      // Projections
      projectionToCenter.setComponents(cameraToBodyVec).project(orientation);
      projectionToScreen.setComponents(cameraToBodyVec).sub(projectionToCenter);

      projectionToX.setComponents(cameraToBodyVec).project(xAxisDirection);
      projectionToY.setComponents(cameraToBodyVec).project(yAxisDirection);

      // TODO: Only used to get rotation sign, find a way to remove.
      xDistM = projectionToX.len() * Math.signum(projectionToX.dot(xAxisDirection));
      yDistM = projectionToY.len() * Math.signum(projectionToY.dot(yAxisDirection));

      // Finding angles
      angularPositionR = projectionToCenter.separationAngle(cameraToBodyVec);
      ellipseRotAngleR = projectionToScreen.separationAngle(projectionToX);
      ellipseRotAngleR *= Math.signum(yDistM * xDistM);// Turns clockwise if in 2nd or 4th quadrant

      angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len()) * DEGPERRAD;

      // Relevant positions of projection
      distToCentOfProj = 0.5 * (Math.tan(angularPositionR + angularDiameterDeg/2.0 * RADPERDEG)
          + Math.tan(angularPositionR - angularDiameterDeg/2.0 * RADPERDEG))*projectionToCenter.len();
      distToCentOfProjX = Math.cos(ellipseRotAngleR) * distToCentOfProj;
      distToCentOfProjY = Math.sin(ellipseRotAngleR) * distToCentOfProj;

      // Width and height of projected ellipse
      projectionWidthM = (Math.tan(angularPositionR + angularDiameterDeg/2.0 * RADPERDEG)
          - Math.tan(angularPositionR - angularDiameterDeg/2.0 * RADPERDEG))*projectionToCenter.len();
      omega = distToCentOfProjY * distToCentOfProjY + distToCentOfProjX * distToCentOfProjX + 1
          - projectionWidthM * projectionWidthM / 4.0;
      projectionHeightM = 2 * Math.sqrt((-omega + Math.sqrt(omega * omega + projectionWidthM * projectionWidthM)) / 2.0);

      // Converts from space to image
      screenWidthM = Math.tan(hFOV / 2.0 * RADPERDEG) * projectionToCenter.len() * 2;
      screenHeightM = screenWidthM / screenRatio;
      bodyProjection.setFrame(
          screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * projectionWidthM) / screenWidthM),
          screenHeightP * (0.5 + (distToCentOfProjY - 0.5 * projectionHeightM) / screenHeightM),
          screenWidthP * projectionWidthM / screenWidthM,
          screenHeightP * projectionHeightM / screenHeightM);
      // Rotate the ellipse to align it to the center of the screen and draw
      g2d.rotate(ellipseRotAngleR);
      g2d.draw(bodyProjection);
      g2d.setTransform(originalTransform);
    }
  }

  /**
   * Updates the vertical FOV and ratio of the screen after change to related
   * values
   */
  public void updateVFOVAndRatio() {
    screenRatio = screenWidthP / screenHeightP;
    vFOV = 2 * Math.atan(Math.tan(hFOV * RADPERDEG / 2.0) / screenRatio) * DEGPERRAD;
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

  /**
   * Sets the screen ratio of the camera (width/height)
   * while keeping the width constant
   *
   * @param screenRatio The new screen ration
   */
  public void setScreenRatioW(double screenRatio) {
    this.screenRatio = screenRatio;
    screenHeightP = screenWidthP / screenRatio;
    updateVFOVAndRatio();
  }

  /**
   * Sets the horizontal FOV of the camera in degrees
   *
   * @param hFOV The new horizontal FOV
   */
  public void setHFOV(double hFOV) {
    this.hFOV = hFOV;
    updateVFOVAndRatio();
  }

  /**
   * Sets the screen width in pixels
   *
   * @param width The new screen width
   */
  public void setScreenHeight(double screenHeightP) {
    this.screenHeightP = screenHeightP;
    updateVFOVAndRatio();
  }

  /**
   * Sets the screen width
   *
   * @param width The new screen width
   */
  public void setScreenWidth(double screenWidthP) {
    this.screenWidthP = screenWidthP;
    updateVFOVAndRatio();
  }
}
