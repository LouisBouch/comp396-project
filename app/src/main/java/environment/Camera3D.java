package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

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

  private int mode = 4;
  private ArrayList<Vector3D> stars;
  private ArrayList<Integer> starWidths;
  private Vector3D positionInSpaceM;
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
    populateGalaxy(80);
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
   * Creates stars at random positions
   */
  private void populateGalaxy(int nbStars) {
    int maxSize = 3;
    int minSize = 1;
    stars = new ArrayList<Vector3D>(nbStars);
    starWidths = new ArrayList<>(nbStars);
    for (int i = 0; i < nbStars; i++) {
      Vector3D star = new Vector3D(Math.random(), Math.random(), Math.random()).ScalarAdd(-0.5);
      double width = minSize + Math.random() * (1 + maxSize - minSize);
      if (star.len() == 0) {
        continue;
      }
      star.normalize();
      starWidths.add((int) width);
      stars.add(star);
    }
  }

  /**
   * Paints the stars in the background
   *
   * @param g2d The graphics component
   */
  private void paintStars(Graphics2D g2d) {
    for (int i = 0; i < stars.size(); i++) {
      Vector3D star = new Vector3D(stars.get(i));
      double screenDistM = 1;
      double screenWidthM = Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM * 2;
      double pixelPerMeter = screenWidthP / screenWidthM;
      // Get angles projected onto X and Y axis and check if the dots are out of
      // bounds
      double separationAngleXD = Vector3D.sub(star, Vector3D.project(star, curYAxis))
          .separationAngle(curOrientation) * DEGPERRAD;
      double separationAngleYD = Vector3D.sub(star, Vector3D.project(star, curXAxis))
          .separationAngle(curOrientation) * DEGPERRAD;
      if (separationAngleYD > vFOV / 2.0 || separationAngleXD > hFOV / 2.0) {
        continue;
      }

      double starWidhtP = starWidths.get(i);
      Point2D screenMidP = new Point2D.Double(screenWidthP / 2.0, screenHeightP / 2.0);
      // Obtain the quadrant of the star
      Point2D signs = new Point2D.Double(Math.signum(star.dot(curXAxis)), Math.signum(star.dot(curYAxis)));

      // Obtain the distance from the center in meters
      Point2D distFromCenterM = new Point2D.Double(
          Math.tan(separationAngleXD * RADPERDEG) * screenDistM,
          Math.tan(separationAngleYD * RADPERDEG) * screenDistM);

      // Convert the distance in meters to a distance in pixels
      // This can be done because the pinhole camera does not distort images
      Point2D posP = new Point2D.Double(
          screenMidP.getX() + distFromCenterM.getX() * pixelPerMeter * signs.getX(),
          screenMidP.getY() + distFromCenterM.getY() * pixelPerMeter * signs.getY());

      Ellipse2D el = new Ellipse2D.Double(posP.getX() - starWidhtP / 2.0, posP.getY() - starWidhtP / 2.0, starWidhtP,
          starWidhtP);
      g2d.setColor(Color.white);
      g2d.fill(el);
    }
  }

  /**
   * Go over every Body in the solarSystem and paint them on the screen
   *
   * @param g2d The graphics component
   */
  @Override
  public void paintThis(Graphics2D g2d) {
    // Setts up the necessary values before computing them
    AffineTransform originalTransform = g2d.getTransform();
    // Works, but breaks something somehow TODO: why?
    // ArrayList<Body> orderedBodies = solarSystem.getBodies();
    // orderedBodies.sort((a,b)->{
    // return Double.compare(Vector3D.sub(b.getPos(), positionInSpaceM).len(),
    // Vector3D.sub(a.getPos(), positionInSpaceM).len());
    // });
    // Paint the stars
    paintStars(g2d);
    // Paint each body
    for (Body body : solarSystem.getBodies()) {
      // Method using no approximation
      int contourDots = 60;
      int innerDots = 15;
      double start = System.nanoTime();
      // Shows bodies with respect to a prticular mode

      // Least computationally expensive mode
      if (mode == 0) {
        approximationView(g2d, body);
      }
      // Nicest mode for now
      if (mode == 1) {
        objectiveView(g2d, body, contourDots, innerDots);
      }
      // Will be useful for the UV map texture
      if (mode == 2) {
        objectiveViewEqualDist(g2d, body, contourDots, innerDots);
      }
      // Looks weird, but it only shows the visible side of the sphere
      if (mode == 3) {
        subjectiveView(g2d, body, contourDots, innerDots);
      }
      // Tried to apply texure
      if (mode == 4) {
        BufferedImage texture = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        try {
          texture = ImageIO.read(getClass().getResource("/earth.jpg"));
        } catch (IOException e) {
          e.printStackTrace();
        }
        // System.out.println("red " + (texture.getRGB(0, 100) >> 16 & 0xFF));
        // System.out.println("green " + (texture.getRGB(0, 100) >> 8 & 0xFF));
        // System.out.println("blue " + (texture.getRGB(0, 100) & 0xFF));
        textureView(g2d, body, contourDots, innerDots, texture);
      }
      g2d.setTransform(originalTransform);

      double end = System.nanoTime();
      double timeSpent = (end - start) / 1000000.0;
      // System.out.println("Time spent: " + timeSpent);
    }
  }

  /**
   * Takes a vector going from the camera to somehwere in space and return the
   * position at the end of the vector as it would appear on screen
   *
   * @param edge Vector3D going from the camera to some point in space
   *
   * @return The position in pixel of the point as seen by the camera
   */
  private Point2D.Double spaceToScreen(Vector3D edge) {
    double screenDistM = edge.len();
    if (screenDistM == 0) {
      return null;
    }
    double screenWidthM = 2 * Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM;
    double pixelPerMeter = screenWidthP / screenWidthM;
    // Separation angle in the vertical and horizontal direciton
    double separationAngleXD = Vector3D.projectOnPlane(edge, curYAxis).separationAngle(curOrientation) * DEGPERRAD;
    double separationAngleYD = Vector3D.projectOnPlane(edge, curXAxis).separationAngle(curOrientation) * DEGPERRAD;
    // Don't draw if point is out of bounds
    if (separationAngleYD > vFOV / 2.0 || separationAngleXD > hFOV / 2.0) {
      return null;
    }

    Point2D screenMidP = new Point2D.Double(screenWidthP / 2.0, screenHeightP / 2.0);
    Point2D signs = new Point2D.Double(Math.signum(edge.dot(curXAxis)), Math.signum(edge.dot(curYAxis)));

    // Obtain the distance from the center of image plane in meters
    Point2D screenProjM = new Point2D.Double(
        Math.tan(separationAngleXD * RADPERDEG) * screenDistM,
        Math.tan(separationAngleYD * RADPERDEG) * screenDistM);
    // Convert the distance to pixels on the screen
    Point2D.Double posP = new Point2D.Double(
        screenMidP.getX() + screenProjM.getX() * pixelPerMeter * signs.getX(),
        screenMidP.getY() + screenProjM.getY() * pixelPerMeter * signs.getY());
    return posP;
  }

  /**
   * Takes a vector going from the camera to somehwere in space and return the UV
   * position for a sphere
   *
   * @param edge Vector3D going from the camera to some point in space
   *
   * @return The UV position on the sphere
   */
  private Point2D.Double spaceToUVSphere(Vector3D edge) {
    return new Point2D.Double();
  }

  /**
   * Projects sphere on plane with textures
   */
  private void textureView(Graphics2D g2d, Body body, int contourDots, int innerDots, BufferedImage texture) {
    int imageWidth = texture.getWidth();
    int imageHeight = texture.getHeight();

    // Texture related elements
    BufferedImage texProj = new BufferedImage((int) screenWidthP, (int) screenHeightP, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2t = texProj.createGraphics();
    WritableRaster raster = texProj.getRaster();
    int[] pixelColor = new int[] { 0, 0, 255, 255 };

    // Circle to iterate over
    Point imCenter = new Point(600, 500);
    int xc = (int) imCenter.getX();
    int yc = (int) imCenter.getY();
    double radImage = 450;

    double start = System.nanoTime();
    // Fill in the circle
    for (int h = (int) -radImage; h <= 0; h++) {
      for (int w = (int) -radImage; w <= 0; w++) {
        // Check if point is in circle
        if (!(h * h + w * w <= radImage * radImage))
          continue;
        // Draw two horizontal lines in the circle
        for (int x = w; x <= -w; x++) {
          raster.setPixel(x + xc, h + yc, pixelColor);
          raster.setPixel(x + xc, -h + yc, pixelColor);
        }
        break;
      }
    }
    //// Fill in the circle (optimized... or is it?)
    //// bresenhams-circle-algorithm
    // int x = 0;
    // int y = (int) radImage;
    //// drawCircleSeg(xc, yc, x, y, raster, pixelColor);
    // int d = 3 - 2 * (int) radImage;
    // while (y >= x) {
    // // d = 2*(x+1)*(x+1)+y*y+(y-1)*(y-1)-2*(int)(radImage*radImage);
    // drawHLine(-y + xc, x + yc, y + xc, raster, pixelColor);
    // drawHLine(-y + xc, -x + yc, y + xc, raster, pixelColor);
    // if (d <= 0) {
    // d = d + (4 * x) + 6;
    // } else {
    // drawHLine(-x + xc, y + yc, x + xc, raster, pixelColor);
    // drawHLine(-x + xc, -y + yc, x + xc, raster, pixelColor);
    // d = d + 4 * (x - y) + 10;
    // y--;
    // }
    // x++;
    // // drawCircleSeg(xc, yc, x, y, raster, pixelColor);
    // }
    double end = System.nanoTime();
    double timeSpent = (end - start) / 1000000.0;
    System.out.println(timeSpent);
    g2d.drawImage(texProj, 0, 0, (int) screenWidthP, (int) screenHeightP, null);
  }

  /**
   * Draw a line
   */
  private void drawHLine(int x, int y, int xe, WritableRaster raster, int[] pixelColor) {
    for (int i = x; i <= xe; i++) {
      raster.setPixel(i, y, pixelColor);
    }
  }

  /*
   * Fill 4 points on the circle as given by the Bresenhams circle algorithm
   */
  private void drawCircleSeg(int xc, int yc, int x, int y, WritableRaster raster, int[] pixelColor) {
    raster.setPixel((x + xc), (y + yc), pixelColor);// uur
    raster.setPixel((-x + xc), (y + yc), pixelColor);// uul
    raster.setPixel((x + xc), (-y + yc), pixelColor);// ddr
    raster.setPixel((-x + xc), (-y + yc), pixelColor);// ddl
    raster.setPixel((y + xc), (x + yc), pixelColor);// ur
    raster.setPixel((-y + xc), (x + yc), pixelColor);// ul
    raster.setPixel((y + xc), (-x + yc), pixelColor);// dr
    raster.setPixel((-y + xc), (-x + yc), pixelColor);// dl
  }

  /**
   * Easy to compute but has wrong minor axis size
   */
  private void approximationView(Graphics2D g2d, Body body) {
    // Setts up the necessary values before computing them
    Vector3D cameraToBodyVec = new Vector3D();// Vector from camera position to body
    Vector3D projectionToCenter = new Vector3D();// Projection onto the camera view diretion
    Vector3D projectionToScreen = new Vector3D();// Projection to plane perpendicular to camera view
    Vector3D projectionToX = new Vector3D();// Projection on x axis as seen by camera
    Vector3D projectionToY = new Vector3D();// Projection on y axis as seen by camera

    double furthestAngleThreshold = 88.5;// If the furthest point on the ellipse forms an angle with the orientation
                                         // axis
    // greater than this value, approximate the furthest point as the position of
    // the point at this angle plus the shift of the closest point on the ellipse.
    double ellipseRotAngleR;// Rotation of ellipse to align it towards the center of the screen
    double bodyRad;// Radius of the body used
    double screenDistM;// Distance from tthe camera to the projection plane

    double screenWidthM;// Width of plane intersecting the body
    double screenHeightM;// Height of plane intersecting the body
    double projectionMajorM; // Major axis of the ellipse projected onto the image plane
    double projectionMinorM;// Minor axis of the ellipse projected onto the image plane
    double angularPositionR;// Angle between camera axis and cameraToBodyVec
    double angularDiameterDeg;// Angular diameter of body
    double ellEccentricty;// Approximated eccentricity of the Ellipse
    // pinhole camera
    double lengthToScreenM; // Length of vector that goes from the camera to the body and is intersected by
                            // the image plane

    double xSign;// Length of projectionToX, negative if in the negative x direction
    double ySign;
    double centerShiftM;// When the approximation is in effect, the body needs to be shifted by this
                        // amount along the projectionToScreen vector.

    double furthestDist;// Distance from center of screen to furthest point of projection
    double closestDist;// Distance from center of screen to closest point on major axis of projection
    double distToCentOfProj;// The distance to the center of the projected sphere starting from the center
                            // of the image (in meters)
    double distToCentOfProjX;// Value is in meters (and scaled down, like all other values in meters)
    double distToCentOfProjY;
    Ellipse2D bodyProjection = new Ellipse2D.Double();
    g2d.setColor(body.getColor());
    cameraToBodyVec.setComponents(body.getPos()).sub(positionInSpaceM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    scaleDown = cameraToBodyVec.len();
    cameraToBodyVec.scalarDiv(scaleDown);
    bodyRad = body.getRadius() / scaleDown;

    // Skip if inside body
    if (bodyRad > cameraToBodyVec.len())
      return;

    angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len())
        * DEGPERRAD;
    angularPositionR = curOrientation.separationAngle(cameraToBodyVec);

    // Don't load body if behind camera
    if (angularPositionR * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
      return;
    }

    // Take screen dist as projection of cameraToBodyVec on the orientation axis
    screenDistM = Math.cos(angularPositionR) * cameraToBodyVec.len();

    centerShiftM = 0;
    // If the body is almost behind the camera, just draw as if it were closer and
    // then shift
    if (angularPositionR * DEGPERRAD + angularDiameterDeg / 2.0 > furthestAngleThreshold) {
      // Angle that makes sure the body does not cross the angle threshold
      double adjustedAngPosR = (furthestAngleThreshold - angularDiameterDeg / 2.0) / DEGPERRAD;
      screenDistM = Math.cos(adjustedAngPosR) * cameraToBodyVec.len();
      double closestDistBeforeM = Math.tan(angularPositionR - angularDiameterDeg * RADPERDEG / 2.0) * screenDistM;
      double closestDistAfterM = Math.tan(adjustedAngPosR - angularDiameterDeg * RADPERDEG / 2.0)
          * screenDistM;

      centerShiftM = closestDistBeforeM - closestDistAfterM;
      // Adjust body to make it look like it is still positioned before the camera
      // plane
      cameraToBodyVec.qatRot(Quaternion.fromAxisAngle(angularPositionR - adjustedAngPosR,
          Vector3D.cross(cameraToBodyVec, curOrientation)));
      // New angular position that assumes the body is positioned before the camera
      // plane
      angularPositionR = adjustedAngPosR;
    }
    lengthToScreenM = screenDistM / Math.abs(Math.cos(angularPositionR));
    cameraToBodyVec.normalize().scalarMult(lengthToScreenM);
    projectionToCenter.setComponents(curOrientation).normalize().scalarMult(screenDistM);
    projectionToScreen.setComponents(cameraToBodyVec).sub(projectionToCenter);
    // Relevant distances of projection
    closestDist = Math.tan(angularPositionR - angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();
    furthestDist = Math.tan(angularPositionR + angularDiameterDeg / 2.0 * RADPERDEG) * projectionToCenter.len();

    // Screen info
    screenWidthM = Math.tan(hFOV / 2.0 * RADPERDEG) * projectionToCenter.len() * 2;
    screenHeightM = screenWidthM / screenRatio;

    // Body position info
    projectionToX.setComponents(cameraToBodyVec).project(curXAxis);
    projectionToY.setComponents(cameraToBodyVec).project(curYAxis);

    xSign = Math.signum(projectionToX.dot(curXAxis));
    ySign = Math.signum(projectionToY.dot(curYAxis));
    xSign = xSign == 0 ? 1 : xSign;
    ySign = ySign == 0 ? 1 : ySign;

    // Finding angles
    ellipseRotAngleR = projectionToScreen.separationAngle(projectionToX);
    ellipseRotAngleR *= Math.signum(ySign * xSign);// Turns clockwise if in 2nd or 4th quadrant

    // If furthest point of body cannot be projected accurately
    distToCentOfProj = 0.5 * (furthestDist + closestDist);

    // Width and height of projected ellipse
    projectionMajorM = (furthestDist - closestDist);

    // This is only an approximation, this formula is not physically accurate. I
    // pulled it out of my ***. It's 4am and I just want to go to bed :(
    // If you figure out how to get the minor axis length, message me at
    // louisbouchard@mail.com (it is indeed @mail.com)
    ellEccentricty = 2.0 * Math.abs(distToCentOfProj - projectionToScreen.len()) / projectionMajorM;
    projectionMinorM = projectionMajorM * Math.sqrt(1 - ellEccentricty * ellEccentricty);
    // double omega = projectionToX.len()*projectionToX.len() +
    // projectionToY.len()*projectionToY.len() + 1 -
    // (projectionMajorM/2.0)*(projectionMajorM/2.0);
    // projectionMinorM = 2*Math.sqrt((-omega +
    // Math.sqrt(omega*omega+projectionMajorM*projectionMajorM))/2);

    // Get projections
    distToCentOfProj += centerShiftM;// Add shift from approximation
    distToCentOfProjX = Math.cos(ellipseRotAngleR) * distToCentOfProj * Math.signum(xSign);
    distToCentOfProjY = Math.sin(Math.abs(ellipseRotAngleR)) * distToCentOfProj * Math.signum(ySign);

    // Don't load body if outside of hFOV (ellipse relaxed to biggest sphere
    // containing it)
    if (Math.abs(distToCentOfProjX) - projectionMajorM / 2.0 > screenWidthM / 2.0) {
      return;
    }
    // Don't load body if outside of vFOV (ellipse relaxed to biggest sphere
    // containing it)
    if (Math.abs(distToCentOfProjY) - projectionMajorM / 2.0 > screenHeightM / 2.0) {
      return;
    }
    // Converts from space to image
    bodyProjection.setFrame(
        screenWidthP * (0.5 + (distToCentOfProjX - 0.5 * projectionMajorM) / screenWidthM),
        screenHeightP * (0.5 + (distToCentOfProjY - 0.5 * projectionMinorM) / screenHeightM),
        screenWidthP * projectionMajorM / screenWidthM,
        screenHeightP * projectionMinorM / screenHeightM);
    // Rotate the ellipse to align it to the center of the screen and draw
    g2d.translate(bodyProjection.getX() + bodyProjection.getWidth() / 2,
        bodyProjection.getY() + bodyProjection.getHeight() / 2);
    g2d.rotate(ellipseRotAngleR);
    g2d.translate(-bodyProjection.getX() - bodyProjection.getWidth() / 2,
        -bodyProjection.getY() - bodyProjection.getHeight() / 2);

    g2d.fill(bodyProjection);
  }

  /**
   * Shows only part of the body visible by the camera
   */
  private void subjectiveView(Graphics2D g2d, Body body, int contourDots, int innerDots) {
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), positionInSpaceM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    double scaleDown = cameraToBodyVec.len();
    cameraToBodyVec.scalarDiv(scaleDown);
    double bodyRad = body.getRadius() / scaleDown;

    // Skip if inside body
    if (bodyRad > cameraToBodyVec.len())
      return;

    double angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len())
        * DEGPERRAD;
    double angularPositionR = curOrientation.separationAngle(cameraToBodyVec);

    // Don't load body if behind camera
    if (angularPositionR * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
      return;
    }

    // Take screen dist as projection of cameraToBodyVec on the orientation axis
    double screenDistM = cameraToBodyVec.len();
    double screenWidthM = 2 * Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM;
    double screenHeightM = screenWidthM / screenRatio;
    for (int c = 0; c < contourDots; c++) {
      for (int i = 0; i <= innerDots; i++) {
        Vector3D posInSpaceM = new Vector3D(positionInSpaceM).scalarDiv(scaleDown);
        Vector3D bodyPosInSpaceM = new Vector3D(posInSpaceM).add(cameraToBodyVec);

        // Uncomment this to only show visible part of sphere (subjective view)
        Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c *
            RADPERDEG, cameraToBodyVec);
        Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(curXAxis, midQuat),
            cameraToBodyVec);
        // Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c *
        // RADPERDEG, orientation);
        // Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(xAxisDirection, midQuat),
        // orientation);
        if (rotAxis.len() == 0) {
          rotAxis.setComponents(curYAxis);
        }
        // Uncomment this to only show visible part of sphere (subjective view)
        Quaternion quat = Quaternion.fromAxisAngle((90 - angularDiameterDeg / 2.0) /
            innerDots * i * RADPERDEG,
            rotAxis);
        Vector3D centerToSurfM = new Vector3D(cameraToBodyVec).normalize().scalarMult(-bodyRad).qatRot(quat);

        // Uncomment this one INSTEAD to get objective view
        // Quaternion quat = Quaternion.fromAxisAngle(180 / ((double) innerDots) * i *
        // RADPERDEG,
        // rotAxis);

        // This view leaves equal space between points if looking from origin
        // double dist = bodyRad * 0.5 * i / (double) innerDots;
        // double angR = Math.atan(dist / (cameraToBodyVec.len() - bodyRad));
        // double l = cameraToBodyVec.len();
        // double cr = Math.cos(angR);
        // double k = cr * l - Math.sqrt(l * l * (cr * cr - 1) + bodyRad * bodyRad);
        // Quaternion quat = Quaternion.fromAxisAngle(Math.asin(Math.sin(angR) * k /
        // bodyRad), rotAxis);
        // Vector3D centerToSurfM = new
        // Vector3D(orientation).normalize().scalarMult(-bodyRad).qatRot(quat);

        // Gets final position and project onto image plane
        Vector3D surfP = Vector3D.add(centerToSurfM, bodyPosInSpaceM);
        Vector3D edge = new Vector3D(surfP).sub(posInSpaceM);
        double separationAngleXD = Vector3D.sub(edge, Vector3D.project(edge, curYAxis))
            .separationAngle(curOrientation) * DEGPERRAD;
        double separationAngleYD = Vector3D.sub(edge, Vector3D.project(edge, curXAxis))
            .separationAngle(curOrientation) * DEGPERRAD;
        if (separationAngleYD > vFOV / 2.0 || separationAngleXD > hFOV / 2.0) {
          continue;
        }

        // Distance from camera to surface point
        double lengthToScreenM = screenDistM / Math.abs(Math.cos(edge.separationAngle(curOrientation)));
        edge.normalize().scalarMult(lengthToScreenM);

        Vector3D projToScreenNew = Vector3D.sub(edge, Vector3D.project(edge, curOrientation));
        double projDistM = projToScreenNew.len();
        double angleWithXAxisD = projToScreenNew.separationAngle(curXAxis) * DEGPERRAD;

        Point2D screenProj2DM = new Point2D.Double(Math.abs(Math.cos(angleWithXAxisD * RADPERDEG)) * projDistM,
            Math.abs(Math.sin(angleWithXAxisD * RADPERDEG)) * projDistM);
        Point2D endP = new Point2D.Double(
            screenWidthP
                * (0.5 + (screenProj2DM.getX() * Math.signum(projToScreenNew.dot(curXAxis))) / screenWidthM),
            screenHeightP
                * (0.5
                    + (screenProj2DM.getY() * Math.signum(projToScreenNew.dot(curYAxis))) / screenHeightM));
        // Colors
        Vector3D RGBValue = new Vector3D(Math.abs(surfP.getX() - bodyPosInSpaceM.getX()),
            Math.abs(surfP.getY() - bodyPosInSpaceM.getY()),
            Math.abs(surfP.getZ() - bodyPosInSpaceM.getZ()))
            .scalarDiv(bodyRad).scalarMult(255);
        g2d.setColor(new Color((int) RGBValue.getX(), (int) RGBValue.getY(), (int) RGBValue.getZ()));
        // g2d.draw(new Line2D.Double(startP, endP));
        g2d.fill(new Ellipse2D.Double(endP.getX() - 2, endP.getY() - 2, 4, 4));
        // System.out.println("Dist to body: " + Vector3D.pointToLine(cameraToBodyVec,
        // new Vector3D(), edge).len() + ", Radius: " + bodyRad);
        // System.out.println();
      }
    }
  }

  /**
   * Shows body in space with fixed points, with rings equally far away when seen
   * from the origin
   */
  private void objectiveViewEqualDist(Graphics2D g2d, Body body, int contourDots, int innerDots) {
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), positionInSpaceM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    double scaleDown = cameraToBodyVec.len();
    cameraToBodyVec.scalarDiv(scaleDown);
    double bodyRad = body.getRadius() / scaleDown;

    // Skip if inside body
    if (bodyRad > cameraToBodyVec.len())
      return;

    double angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len())
        * DEGPERRAD;
    double angularPositionR = curOrientation.separationAngle(cameraToBodyVec);

    // Don't load body if behind camera
    if (angularPositionR * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
      return;
    }

    // Take screen dist as projection of cameraToBodyVec on the orientation axis
    double screenDistM = cameraToBodyVec.len();
    double screenWidthM = 2 * Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM;
    double screenHeightM = screenWidthM / screenRatio;
    for (int c = 0; c < contourDots; c++) {
      for (int i = 0; i <= innerDots; i++) {
        Vector3D posInSpaceM = new Vector3D(positionInSpaceM).scalarDiv(scaleDown);
        Vector3D bodyPosInSpaceM = new Vector3D(posInSpaceM).add(cameraToBodyVec);

        // Uncomment this to only show visible part of sphere (subjective view)
        // Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c *
        // RADPERDEG, cameraToBodyVec);
        // Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(curXAxis, midQuat),
        // cameraToBodyVec);
        Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c * RADPERDEG, orientation);
        Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(xAxisDirection, midQuat), orientation);
        if (rotAxis.len() == 0) {
          rotAxis.setComponents(curYAxis);
        }
        // Uncomment this to only show visible part of sphere (subjective view)
        // Quaternion quat = Quaternion.fromAxisAngle((90 - angularDiameterDeg / 2.0) /
        // innerDots * i * RADPERDEG,
        // rotAxis);
        // Vector3D centerToSurfM = new
        // Vector3D(cameraToBodyVec).normalize().scalarMult(-bodyRad).qatRot(quat);

        // Uncomment this one INSTEAD to get objective view
        // Quaternion quat = Quaternion.fromAxisAngle(180 / ((double) innerDots) * i *
        // RADPERDEG,
        // rotAxis);

        // This view leaves equal space between points if looking from origin
        double dist = bodyRad * 0.5 * i / (double) innerDots;
        double angR = Math.atan(dist / (cameraToBodyVec.len() - bodyRad));
        double l = cameraToBodyVec.len();
        double cr = Math.cos(angR);
        double k = cr * l - Math.sqrt(l * l * (cr * cr - 1) + bodyRad * bodyRad);
        Quaternion quat = Quaternion.fromAxisAngle(Math.asin(Math.sin(angR) * k /
            bodyRad), rotAxis);
        Vector3D centerToSurfM = new Vector3D(orientation).normalize().scalarMult(-bodyRad).qatRot(quat);

        // Vector3D centerToSurfM = subjectiveView(g2d);

        // Gets final position and project onto image plane
        Vector3D surfP = Vector3D.add(centerToSurfM, bodyPosInSpaceM);
        Vector3D edge = new Vector3D(surfP).sub(posInSpaceM);
        double separationAngleXD = Vector3D.sub(edge, Vector3D.project(edge, curYAxis))
            .separationAngle(curOrientation) * DEGPERRAD;
        double separationAngleYD = Vector3D.sub(edge, Vector3D.project(edge, curXAxis))
            .separationAngle(curOrientation) * DEGPERRAD;
        if (separationAngleYD > vFOV / 2.0 || separationAngleXD > hFOV / 2.0) {
          continue;
        }

        // Distance from camera to surface point
        double lengthToScreenM = screenDistM / Math.abs(Math.cos(edge.separationAngle(curOrientation)));
        edge.normalize().scalarMult(lengthToScreenM);

        Vector3D projToScreenNew = Vector3D.sub(edge, Vector3D.project(edge, curOrientation));
        double projDistM = projToScreenNew.len();
        double angleWithXAxisD = projToScreenNew.separationAngle(curXAxis) * DEGPERRAD;

        Point2D screenProj2DM = new Point2D.Double(Math.abs(Math.cos(angleWithXAxisD * RADPERDEG)) * projDistM,
            Math.abs(Math.sin(angleWithXAxisD * RADPERDEG)) * projDistM);
        Point2D endP = new Point2D.Double(
            screenWidthP
                * (0.5 + (screenProj2DM.getX() * Math.signum(projToScreenNew.dot(curXAxis))) / screenWidthM),
            screenHeightP
                * (0.5
                    + (screenProj2DM.getY() * Math.signum(projToScreenNew.dot(curYAxis))) / screenHeightM));
        // Colors
        Vector3D RGBValue = new Vector3D(Math.abs(surfP.getX() - bodyPosInSpaceM.getX()),
            Math.abs(surfP.getY() - bodyPosInSpaceM.getY()),
            Math.abs(surfP.getZ() - bodyPosInSpaceM.getZ()))
            .scalarDiv(bodyRad).scalarMult(255);
        g2d.setColor(new Color((int) RGBValue.getX(), (int) RGBValue.getY(), (int) RGBValue.getZ()));
        // g2d.draw(new Line2D.Double(startP, endP));
        g2d.fill(new Ellipse2D.Double(endP.getX() - 2, endP.getY() - 2, 4, 4));
        // System.out.println("Dist to body: " + Vector3D.pointToLine(cameraToBodyVec,
        // new Vector3D(), edge).len() + ", Radius: " + bodyRad);
        // System.out.println();
      }
    }
  }

  /**
   * Shows body as if transparent if points on its surface
   */
  private void objectiveView(Graphics2D g2d, Body body, int contourDots, int innerDots) {
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), positionInSpaceM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    double scaleDown = cameraToBodyVec.len();
    cameraToBodyVec.scalarDiv(scaleDown);
    double bodyRad = body.getRadius() / scaleDown;

    // Skip if inside body
    if (bodyRad > cameraToBodyVec.len())
      return;

    double angularDiameterDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len())
        * DEGPERRAD;
    double angularPositionR = curOrientation.separationAngle(cameraToBodyVec);

    // Don't load body if behind camera
    if (angularPositionR * DEGPERRAD - angularDiameterDeg / 2.0 > 90) {
      return;
    }

    // Take screen dist as projection of cameraToBodyVec on the orientation axis
    double screenDistM = cameraToBodyVec.len();
    double screenWidthM = 2 * Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM;

    for (int c = 0; c < contourDots; c++) {
      for (int i = 0; i <= innerDots; i++) {
        Vector3D posInSpaceM = new Vector3D(positionInSpaceM).scalarDiv(scaleDown);
        Vector3D bodyPosInSpaceM = new Vector3D(posInSpaceM).add(cameraToBodyVec);

        Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c * RADPERDEG, orientation);
        Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(xAxisDirection, midQuat), orientation);
        if (rotAxis.len() == 0) {
          rotAxis.setComponents(curYAxis);
        }
        Quaternion quat = Quaternion.fromAxisAngle(180 / ((double) innerDots) * i * RADPERDEG,
            rotAxis);

        // Vector going from the center of the sphere onto the surface
        Vector3D centerToSurfM = new Vector3D(orientation).normalize().scalarMult(-bodyRad).qatRot(quat);

        // Vector representing the position of the surface point
        Vector3D surfP = Vector3D.add(centerToSurfM, bodyPosInSpaceM);

        // Vector going from the camera to the surface point
        Vector3D edge = new Vector3D(surfP).sub(posInSpaceM);
        Point2D posP = spaceToScreen(edge);
        if (posP == null) {
          continue;
        }

        // Draw corresponding point as an ellipse
        Ellipse2D el = new Ellipse2D.Double(posP.getX() - 2, posP.getY() - 2, 4, 4);
        // Colors
        Vector3D RGBValue = new Vector3D(Math.abs(surfP.getX() - bodyPosInSpaceM.getX()),
            Math.abs(surfP.getY() - bodyPosInSpaceM.getY()),
            Math.abs(surfP.getZ() - bodyPosInSpaceM.getZ()))
            .scalarDiv(bodyRad).scalarMult(255);

        g2d.setColor(new Color((int) RGBValue.getX(), (int) RGBValue.getY(), (int) RGBValue.getZ()));
        g2d.fill(el);
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
   * Obtain a copy of the vector containing information about spacial position
   *
   * @return The position in space of the camera in meters
   */
  public Vector3D getPositionInSpaceM() {
    return new Vector3D(positionInSpaceM);
  }

  /**
   * Obtain a copy of the vector containing information about spacial orientation
   *
   * @return The orientation of the camera
   */
  public Vector3D getCurOrientation() {
    return new Vector3D(curOrientation);
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
