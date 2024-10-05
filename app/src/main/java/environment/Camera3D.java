package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import lib.Paintable;
import lib.Quaternion;
import lib.Vector3D;

public class Camera3D implements Paintable {
  public static final int FORWARDS = 1;
  public static final int BACKWARDS = -1;
  public static final int RIGHT = 1;
  public static final int LEFT = -1;
  public static final int DOWN = 1;
  public static final int UP = -1;

  private Vector3D positionInSpaceM;
  private boolean unstable = false;
  /**
   * Converts pixel displacement to degrees
   */
  private double sensitivity = 0.1;
  /**
   * Number of meters to move by when the camera is displaced
   */
  private double stepDistanceM = 200000000;
  /**
   * Number of times we want to boost the step distance
   */
  private double nbBoosts = 0;
  /**
   * Multiplier for a single boost to the step distance
   */
  private double boostValue = 1.5;

  // These value never change, they represent the non rotated orientation values
  private final Vector3D orientation;
  private final Vector3D xAxisDirection;
  private final Vector3D yAxisDirection;
  // These are the current orientations after rotation
  Vector3D curOrientation;
  Vector3D curXAxis;
  Vector3D curYAxis;

  private SolarSystem solarSystem;

  /**
   * Equal to width/height
   */
  private double screenRatio;
  /**
   * Horizontal FOV in degrees
   */
  private double hFOV;
  /**
   * Vertical FOV in degrees
   */
  private double vFOV;
  private double screenWidthP;
  private double screenHeightP;

  private Quaternion rotQ;

  private final double DEGPERRAD = 180 / Math.PI;
  private final double RADPERDEG = 1 / DEGPERRAD;

  // During computation, scale everything down to alleviate numerical errors
  private double scaleDown;

  /**
   * Creates basic camera looking upwards (z)
   *
   * @param positionInSpaceM The position of the camera in space in meters
   * @param solarSystem      The solar system in which the camera belongs
   */
  public Camera3D(Vector3D positionInSpaceM, SolarSystem solarSystem, double hFOV, double screenRatio) {
    this.solarSystem = solarSystem;
    this.positionInSpaceM = positionInSpaceM;
    // These won't change
    orientation = new Vector3D(0, 0, 1);
    xAxisDirection = new Vector3D(1, 0, 0);
    yAxisDirection = new Vector3D(0, 1, 0);
    // These represent the current orientations
    curOrientation = new Vector3D(orientation);
    curXAxis = new Vector3D(xAxisDirection);
    curYAxis = new Vector3D(yAxisDirection);
    // Initialize 0 rotation quaternion
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
    // Setts up the necessary values before computing them
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
    double projectionWidthM;// Projection of cameraToBodyVec (onto plane intersecting with body) width in
                            // meters as seen by
    double angularPositionR;// Angle between camera axis and cameraToBodyVec
    double angularDiameterDeg;// Angular diameter of body
    // pinhole camera
    double projectionHeightM;// Projection height in meters ass seen by pinhole camera

    double xSign;// Length of projectionToX, negative if in the negative x direction
    double ySign;

    double furthestDist;// Distance from center of screen to furthest point of projection
    double closestDist;// Distance from center of screen to closest point on major axis of projection
    double distToCentOfProj;// The distance to the center of the projected sphere starting from the center
                            // of the image (in meters)
    double distToCentOfProjX;
    double distToCentOfProjY;
    Ellipse2D bodyProjection = new Ellipse2D.Double();
    Rectangle2D majorAxis = new Rectangle2D.Double();// TODO: for debugging only
    AffineTransform originalTransform = g2d.getTransform();
    double omega;// Used to compute the minor axis
    // Works, but breaks something somehow TODO: why?
    // ArrayList<Body> orderedBodies = solarSystem.getBodies();
    // orderedBodies.sort((a,b)->{
    // return Double.compare(Vector3D.sub(b.getPos(), positionInSpaceM).len(),
    // Vector3D.sub(a.getPos(), positionInSpaceM).len());
    // });
    if (unstable) {
      for (Body body : solarSystem.getBodies()) {
        g2d.setColor(body.getColor());
        bodyPos.setComponents(body.getPos());
        cameraToBodyVec.setComponents(bodyPos).sub(positionInSpaceM);

        // Scale everything down by the distance from the body to the camera
        scaleDown = cameraToBodyVec.len() / 100;// TODO: changing this value should not change the rendering, and yet it
                                                // does.
        cameraToBodyVec.scalarDiv(scaleDown);
        bodyRad = body.getRadius() / scaleDown;

        // Skip if inside body
        if (bodyRad > cameraToBodyVec.len())
          continue;

        // Projections
        projectionToCenter.setComponents(cameraToBodyVec).project(curOrientation);
        projectionToScreen.setComponents(cameraToBodyVec).sub(projectionToCenter);

        projectionToX.setComponents(cameraToBodyVec).project(curXAxis);
        projectionToY.setComponents(cameraToBodyVec).project(curYAxis);

        // TODO: Only used to get rotation sign, find a way to remove.
        xSign = projectionToX.len() * Math.signum(projectionToX.dot(curXAxis));
        ySign = projectionToY.len() * Math.signum(projectionToY.dot(curYAxis));

        // Finding angles
        angularPositionR = curOrientation.separationAngle(cameraToBodyVec);
        ellipseRotAngleR = projectionToScreen.separationAngle(projectionToX);
        ellipseRotAngleR *= Math.signum(ySign * xSign);// Turns clockwise if in 2nd or 4th quadrant

        angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len()) * DEGPERRAD;

        // Relevant distances of projection
        closestDist = Math.tan(angularPositionR - angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();
        // If furthest point of body cannot be projected accurately,
        if (angularPositionR * DEGPERRAD + angularDiameterDeg / 2.0 > 85) {
          double adjustedAngPosR = (85 - angularDiameterDeg / 2.0) / DEGPERRAD;
          double adjustedProjCenter = cameraToBodyVec.len() * Math.cos((85 - angularDiameterDeg / 2.0) * RADPERDEG);
          furthestDist = Math.tan(85 * RADPERDEG) * adjustedProjCenter;
          furthestDist += closestDist
              - Math.tan(adjustedAngPosR - angularDiameterDeg / 2.0 * RADPERDEG) * adjustedProjCenter;
        } else {
          furthestDist = Math.tan(angularPositionR + angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();
        }
        distToCentOfProj = 0.5 * (furthestDist + closestDist);
        distToCentOfProjX = Math.cos(ellipseRotAngleR) * distToCentOfProj * Math.signum(xSign);
        distToCentOfProjY = Math.sin(Math.abs(ellipseRotAngleR)) * distToCentOfProj * Math.signum(ySign);

        // Width and height of projected ellipse
        projectionWidthM = (furthestDist - closestDist);
        omega = distToCentOfProjY * distToCentOfProjY + distToCentOfProjX * distToCentOfProjX + 1
            - projectionWidthM * projectionWidthM / 4.0;
        projectionHeightM = 2
            * Math.sqrt((-omega + Math.sqrt(omega * omega + projectionWidthM * projectionWidthM)) / 2.0);

        // Don't load body if not visible on screen
        screenWidthM = Math.tan(hFOV / 2.0 * RADPERDEG) * projectionToCenter.len() * 2;
        screenHeightM = screenWidthM / screenRatio;
        // System.out.println("width: " + projectionWidthM / screenWidthM);
        // System.out.println("height: " + projectionHeightM / screenHeightM);
        // Don't load body if outside of hFOV (ellipse relaxed to biggest sphere
        // containing it)
        if (Math.abs(distToCentOfProjX) - projectionWidthM / 2.0 > screenWidthM / 2.0) {
          continue;
        }
        // Don't load body if outside of vFOV (ellipse relaxed to biggest sphere
        // containing it)
        if (Math.abs(distToCentOfProjY) - projectionWidthM / 2.0 > screenHeightM / 2.0) {
          continue;
        }
        // Don't load body if behind camera
        if (cameraToBodyVec.separationAngle(curOrientation) * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
          continue;
        }
        if (cameraToBodyVec.separationAngle(curOrientation) * DEGPERRAD > 90) {
          System.out.println("BEHIND");
        }
        // Converts from space to image
        bodyProjection.setFrame(
            screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * projectionWidthM) / screenWidthM),
            screenHeightP * (0.5 + (distToCentOfProjY - 0.5 * projectionHeightM) / screenHeightM),
            screenWidthP * projectionWidthM / screenWidthM,
            screenHeightP * projectionHeightM / screenHeightM);
        // majorAxis.setFrame(
        // screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * 10 * projectionWidthM) /
        // screenWidthM),
        // screenHeightP * (0.5 + (distToCentOfProjY) / screenHeightM),
        // screenWidthP * projectionWidthM * 10 / screenWidthM,
        // 1);
        // Rotate the ellipse to align it to the center of the screen and draw
        g2d.translate(bodyProjection.getX() + bodyProjection.getWidth() / 2,
            bodyProjection.getY() + bodyProjection.getHeight() / 2);
        g2d.rotate(ellipseRotAngleR);
        g2d.translate(-bodyProjection.getX() - bodyProjection.getWidth() / 2,
            -bodyProjection.getY() - bodyProjection.getHeight() / 2);

        g2d.fill(bodyProjection);
        g2d.setColor(Color.RED);
        g2d.fill(majorAxis);
        g2d.setTransform(originalTransform);
      }
    } else {
      for (Body body : solarSystem.getBodies()) {
        double screenDistM = 10000000.0;
        g2d.setColor(body.getColor());
        bodyPos.setComponents(body.getPos());
        cameraToBodyVec.setComponents(bodyPos).sub(positionInSpaceM);

        // Scale everything down by the distance from the body to the camera
        scaleDown = cameraToBodyVec.len();
        screenDistM /= scaleDown;
        cameraToBodyVec.scalarDiv(scaleDown);
        bodyRad = body.getRadius() / scaleDown;

        // Skip if inside body
        if (bodyRad > cameraToBodyVec.len())
          continue;

        // Projections
        // projectionToCenter.setComponents(cameraToBodyVec).project(curOrientation);//CHANGED
        projectionToCenter.setComponents(curOrientation).normalize().scalarMult(screenDistM);// DELETE
        // projectionToScreen.setComponents(cameraToBodyVec).sub(projectionToCenter);//CHANGED
        double straightToBodyAngleR = projectionToCenter.separationAngle(cameraToBodyVec);// DELETE
        double lengthToScreen = screenDistM / Math.abs(Math.cos(straightToBodyAngleR));// DELETE
        cameraToBodyVec.normalize().scalarMult(lengthToScreen);// DELETE
        projectionToScreen.setComponents(cameraToBodyVec).sub(projectionToCenter);// DELETE

        // Screen info
        screenWidthM = Math.tan(hFOV / 2.0 * RADPERDEG) * projectionToCenter.len() * 2;
        screenHeightM = screenWidthM / screenRatio;

        projectionToX.setComponents(cameraToBodyVec).project(curXAxis);
        projectionToY.setComponents(cameraToBodyVec).project(curYAxis);

        xSign = Math.signum(projectionToX.dot(curXAxis));
        ySign = Math.signum(projectionToY.dot(curYAxis));

        // Finding angles
        angularPositionR = curOrientation.separationAngle(cameraToBodyVec);
        ellipseRotAngleR = projectionToScreen.separationAngle(projectionToX);
        ellipseRotAngleR *= Math.signum(ySign * xSign);// Turns clockwise if in 2nd or 4th quadrant

        // angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len()) *
        // DEGPERRAD;//CHANGED
        angularDiameterDeg = 2 * Math.asin(body.getRadius() / (Vector3D.sub(body.getPos(), positionInSpaceM)).len())
            * DEGPERRAD;// DELETE

        // Relevant distances of projection
        closestDist = Math.tan(angularPositionR - angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();
        // If furthest point of body cannot be projected accurately,
        if (angularPositionR * DEGPERRAD + angularDiameterDeg / 2.0 > 85) {
          double adjustedAngPosR = (85 - angularDiameterDeg / 2.0) / DEGPERRAD;
          // double adjustedProjCenter = cameraToBodyVec.len() * Math.cos((85 -
          // angularDiameterDeg / 2.0) * RADPERDEG);//CHANGED
          // furthestDist = Math.tan(85 * RADPERDEG) * adjustedProjCenter;//CHANGED
          furthestDist = Math.tan(85 * RADPERDEG) * projectionToCenter.len();// DELETE
          // furthestDist += closestDist - Math.tan(adjustedAngPosR - angularDiameterDeg /
          // 2.0 * RADPERDEG) * adjustedProjCenter;//CHANGED
          furthestDist += closestDist
              - Math.tan(adjustedAngPosR - angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();// DELETE
        } else {
          furthestDist = Math.tan(angularPositionR + angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();
        }
        distToCentOfProj = 0.5 * (furthestDist + closestDist);
        distToCentOfProjX = Math.cos(ellipseRotAngleR) * distToCentOfProj * Math.signum(xSign);
        distToCentOfProjY = Math.sin(Math.abs(ellipseRotAngleR)) * distToCentOfProj * Math.signum(ySign);

        // Width and height of projected ellipse
        projectionWidthM = (furthestDist - closestDist);
        // This is not working
        // omega = distToCentOfProjY * distToCentOfProjY + distToCentOfProjX *
        // distToCentOfProjX + 1
        // - projectionWidthM * projectionWidthM / 4.0;
        // projectionHeightM = 2
        // * Math.sqrt((-omega + Math.sqrt(omega * omega + projectionWidthM *
        // projectionWidthM)) / 2.0);

        // This is only an approximation, this formula is not physically accurate. I
        // pulled it out of my ***. It's 4am and I just want to go to bed :(
        // If you figure out how to get the minor axis length, message me at
        // louisbouchard@mail.com (yes, @mail.com)
        double ellEccentricty = Math.abs(distToCentOfProj - projectionToScreen.len()) / projectionWidthM;
        if (angularPositionR * DEGPERRAD + angularDiameterDeg / 2.0 > 85) {
          double adjustedAngPosR = (85 - angularDiameterDeg / 2.0) / DEGPERRAD;
           double change = closestDist - Math.tan(adjustedAngPosR - angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();// DELETE
           double adjustedProjScreen = projectionToCenter.len()*Math.tan(85*DEGPERRAD);
          ellEccentricty = Math.abs(distToCentOfProj - change - adjustedProjScreen) / projectionWidthM;
        }
        //TODO: fix issue of weird rotation when body is behind camera
        //Don't change eccentricity after the 85deg threshold
        projectionHeightM = projectionWidthM * Math.sqrt(1 - ellEccentricty * ellEccentricty);

        // Don't load body if not visible on screen
        // Don't load body if outside of hFOV (ellipse relaxed to biggest sphere
        // containing it)
        if (Math.abs(distToCentOfProjX) - projectionWidthM / 2.0 > screenWidthM / 2.0) {
          //System.out.println("OUTSIDE HORI");
          continue;
        }
        // Don't load body if outside of vFOV (ellipse relaxed to biggest sphere
        // containing it)
        if (Math.abs(distToCentOfProjY) - projectionWidthM / 2.0 > screenHeightM / 2.0) {
          //System.out.println("OUTSIDE VERTI");
          continue;
        }
        // Don't load body if behind camera
        if (cameraToBodyVec.separationAngle(curOrientation) * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
          //System.out.println("FULLY BEHIND");
          continue;
        }
        if (cameraToBodyVec.separationAngle(curOrientation) * DEGPERRAD > 90) {
          //System.out.println("BEHIND");
        }
        // Converts from space to image
        bodyProjection.setFrame(
            screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * projectionWidthM) / screenWidthM),
            screenHeightP * (0.5 + (distToCentOfProjY - 0.5 * projectionHeightM) / screenHeightM),
            screenWidthP * projectionWidthM / screenWidthM,
            screenHeightP * projectionHeightM / screenHeightM);
        // majorAxis.setFrame(
        // screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * 10 * projectionWidthM) /
        // screenWidthM),
        // screenHeightP * (0.5 + (distToCentOfProjY) / screenHeightM),
        // screenWidthP * projectionWidthM * 10 / screenWidthM,
        // 1);
        // Rotate the ellipse to align it to the center of the screen and draw
        g2d.translate(bodyProjection.getX() + bodyProjection.getWidth() / 2,
            bodyProjection.getY() + bodyProjection.getHeight() / 2);
        g2d.rotate(ellipseRotAngleR);
        g2d.translate(-bodyProjection.getX() - bodyProjection.getWidth() / 2,
            -bodyProjection.getY() - bodyProjection.getHeight() / 2);

        g2d.fill(bodyProjection);
        g2d.setColor(Color.RED);
        g2d.fill(majorAxis);
        g2d.setTransform(originalTransform);
      }
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
   * @param screenHeightP The new screen width
   */
  public void setScreenHeight(double screenHeightP) {
    this.screenHeightP = screenHeightP;
    updateVFOVAndRatio();
  }

  /**
   * Sets the screen width
   *
   * @param screenWidthP The new screen width
   */
  public void setScreenWidth(double screenWidthP) {
    this.screenWidthP = screenWidthP;
    updateVFOVAndRatio();
  }

  /**
   * Rotates the camera given a mouse motion
   *
   * @param pixDis The mouse displacement in pixel to take into account for the
   *               rotation (+ means right, - means left)
   * @param roll   If true, will rotate aroudn the acmera direction axis (+ means
   *               counterclockwise, - means clockwise)
   */
  public void rotateCamera(Point pixDis, boolean roll) {
    // Does roll if roll is true
    if (roll) {
      Quaternion r = Quaternion.fromAxisAngle(pixDis.getX() * sensitivity * RADPERDEG, curOrientation);
      rotQ.mulQuaternionReverse(r);
    } else {
      Quaternion yaw = Quaternion.fromAxisAngle(pixDis.getX() * sensitivity * RADPERDEG, curYAxis);
      Quaternion pitch = Quaternion.fromAxisAngle(-pixDis.getY() * sensitivity * RADPERDEG, curXAxis);
      rotQ.mulQuaternionReverse(yaw);
      rotQ.mulQuaternionReverse(pitch);
    }
    // Update orientations
    curOrientation = Vector3D.qatRot(orientation, rotQ);
    curXAxis = Vector3D.qatRot(xAxisDirection, rotQ);
    curYAxis = Vector3D.qatRot(yAxisDirection, rotQ);
    // System.out.println("orientation: " + curOrientation.getX() + " " +
    // curOrientation.getY() + " " + curOrientation.getZ());
  }

  /**
   * Moves the camera in space
   *
   * @param v The displacement of the camera in meters moving along the
   *          direction
   *          the camera is pointing towards. (Camera3D.FORWARDS,
   *          Camera3D.BACKWARDS)
   */
  public void moveAlongView(int v) {
    Vector3D direction = Vector3D.normalize(curOrientation).scalarMult(v);
    positionInSpaceM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
    // System.out.println(positionInSpaceM.getX() + " " + positionInSpaceM.getY() +
    // " " + positionInSpaceM.getZ());
  }

  /**
   * Strafes left or right
   *
   * @param v The displacement of the camera in meters from side to side
   *          (Camera3D.LEFT, Camera3D.RIGHT)
   */
  public void moveSideways(int v) {
    Vector3D direction = Vector3D.normalize(curXAxis).scalarMult(v);
    positionInSpaceM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
    // System.out.println(positionInSpaceM.getX() + " " + positionInSpaceM.getY() +
    // " " + positionInSpaceM.getZ());
  }

  /**
   * Moves the camera vertically
   *
   * @param v The displacement of the camera in meters in the vertical direction
   *          (Camera3D.UP, Camera3D.DOWN)
   */
  public void moveVertical(int v) {
    Vector3D direction = Vector3D.normalize(curYAxis).scalarMult(v);
    positionInSpaceM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
    // System.out.println(positionInSpaceM.getX() + " " + positionInSpaceM.getY() +
    // " " + positionInSpaceM.getZ());
  }

  /**
   * Adds a number of boosts
   *
   * @param v Number of boosts to add
   */
  public void addBoost(int v) {
    nbBoosts += v;
  }

  /**
   * Setter for boostValue
   *
   * @param boostValue New boost value
   */
  public void setBoostValue(double boostValue) {
    this.boostValue = boostValue;
  }
}
