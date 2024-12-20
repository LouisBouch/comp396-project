package environment;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;
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

  private int mode = 4;
  private int scale = 1;

  // Backgrounds universe
  private ArrayList<Vector3D> stars;
  private ArrayList<Integer> starWidths;

  /**
   * Maximum number of pixel in the image before curPixBlockSize starts to
   * increase. This introduces dynamic resolution. Closer to the body -> Less
   * resolution
   */
  private int maxImSize = 275;
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
  // and initial camera position
  private final Vector3D iniPosM;
  private final Vector3D iniOrientation = new Vector3D(0, 0, 1);
  private final Vector3D iniXAxis = new Vector3D(1, 0, 0);
  private final Vector3D iniYAxis = new Vector3D(0, 1, 0);
  // These are the current properties of the camera
  private Vector3D curPosM;
  private Vector3D curOrientation;
  private Vector3D curXAxis;
  private Vector3D curYAxis;

  // Solar system in which the camear lives
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

  /**
   * Holds the value of the camera rotation
   */
  private Quaternion rotQ;

  // Useful constant
  private final double DEGPERRAD = 180 / Math.PI;
  private final double RADPERDEG = 1 / DEGPERRAD;

  /**
   * Creates basic camera looking upwards (z)
   *
   * @param positionInSpaceM The position of the camera in space in meters
   * @param solarSystem      The solar system in which the camera belongs
   */
  public Camera3D(Vector3D positionInSpaceM, SolarSystem solarSystem, double hFOV, double screenRatio) {
    populateGalaxy(100);
    this.solarSystem = solarSystem;
    this.curPosM = positionInSpaceM;
    this.iniPosM = new Vector3D(positionInSpaceM);
    // These represent the current orientations
    curOrientation = new Vector3D(iniOrientation);
    curXAxis = new Vector3D(iniXAxis);
    curYAxis = new Vector3D(iniYAxis);
    // Initialize 0 rotation quaternion
    rotQ = new Quaternion();
    setHFOV(hFOV);
    setScreenRatioW(screenRatio);
  }

  /**
   * Clone a Camera3D
   *
   * @param cam Camera3D to clone
   * @return cloned camera
   */
  public Camera3D(Camera3D cam) {
    // Keep same background and solarSystem
    stars = cam.stars;
    solarSystem = cam.solarSystem;
    maxImSize = cam.maxImSize;

    iniPosM = cam.iniPosM;
    curPosM = new Vector3D(cam.getCurPosM());

    // These represent the current orientations
    curOrientation = new Vector3D(cam.curOrientation);
    curXAxis = new Vector3D(cam.curXAxis);
    curYAxis = new Vector3D(cam.curYAxis);

    // Initialize copy orientation
    rotQ = new Quaternion(cam.getRotQ());
    setHFOV(cam.getHFOV());
    setScreenWidth(cam.screenWidthP);
    setScreenRatioW(cam.getScreenRatio());
  }

  /**
   * Creates stars at random positions
   *
   * @param nbStars Number of stars in the background
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
      // Get pos on screen
      Point2D posP = spaceToScreen(star);
      // Don't draw if no screen position
      if (posP == null) {
        continue;
      }
      double starWidhtP = starWidths.get(i);
      Ellipse2D el = new Ellipse2D.Double(
          posP.getX() - starWidhtP / 2.0, posP.getY() - starWidhtP / 2.0, starWidhtP, starWidhtP);
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
    ArrayList<Body> orderedBodies = new ArrayList<>(solarSystem.getBodies());
    // Sort the bodies by distance to camera
    orderedBodies.sort((a, b) -> {
      return Double.compare(
          Vector3D.sub(b.getPos(), curPosM).len(),
          Vector3D.sub(a.getPos(), curPosM).len());
    });
    // Paint the stars
    paintStars(g2d);
    // Paint each body
    for (Body body : orderedBodies) {
      // Method using no approximation
      int contourDots = 60;
      int innerDots = 15;
      double start = System.nanoTime();
      // Shows bodies with respect to a prticular mode

      // Points on surface
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
      // Texture mode
      if (mode == 4) {
        // Copy camera to make sure the change in position while rendering does not
        // affect the final picture
        Camera3D cam = new Camera3D(this);
        // Also copy body to prevent the same issue but with the body movement
        Body bodyCopy = body.copy();
        // Radius scale factor
        bodyCopy.setRadius(bodyCopy.getRadius() * scale);
        cam.textureView(g2d, bodyCopy);
      }
      // Least computationally expensive mode. Shows approximation to sphere
      // projection
      if (mode == 0) {
        approximationView(g2d, body);
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
    // Don't draw if point is out of bounds (with a bit of leniency)
    if (separationAngleYD > vFOV / 1.9 || separationAngleXD > hFOV / 1.9) {
      return null;
    }

    Point2D screenMidP = new Point2D.Double(screenWidthP / 2.0, screenHeightP / 2.0);
    // Gets quadrant of the point on screen
    Point2D signs = new Point2D.Double(Math.signum(edge.dot(curXAxis)), Math.signum(edge.dot(curYAxis)));

    // Obtain the distance from the center of image plane in meters
    Point2D screenProjM = new Point2D.Double(
        Math.tan(separationAngleXD * RADPERDEG) * screenDistM,
        Math.tan(separationAngleYD * RADPERDEG) * screenDistM);
    // Convert the distance in meters to a distance in pixels
    // This can be done because the pinhole camera does not distort images
    Point2D.Double posP = new Point2D.Double(
        screenMidP.getX() + screenProjM.getX() * pixelPerMeter * signs.getX(),
        screenMidP.getY() + screenProjM.getY() * pixelPerMeter * signs.getY());
    return posP;
  }

  /**
   * Takes a point on the screen an convert it to an outgoing edge in space
   *
   * @param x x position on screen (pixel)
   * @param y y position on screen (pixel)
   * @return An outgoing edge from the camerae equivalent to the screen position
   */
  private Vector3D screenToSpace(int x, int y) {
    double screenDist = 0.5 * screenWidthP / Math.tan(hFOV / 2.0 * RADPERDEG);
    Vector3D hor = new Vector3D(curXAxis).normalize().scalarMult(x - screenWidthP / 2.0);
    Vector3D vert = new Vector3D(curYAxis).normalize().scalarMult(y - screenHeightP / 2.0);
    Vector3D front = new Vector3D(curOrientation).normalize().scalarMult(screenDist);
    return front.add(hor).add(vert).normalize();
  }

  /**
   * Takes an edge Vector3D going from the camera to somehwere in space and return
   * the UV
   * position of the intersection of the edge ray and body. Return null if
   * there is no intersection.
   *
   * @param edge Vector3D going from the camera to some point in space
   * @return The UV position on the sphere (values range from 0 to 1)
   */
  private Point2D.Double spaceToUVSphere(Vector3D edge, Body body) {
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), curPosM);
    double r = body.getRadius();
    double sepAngleR = edge.separationAngle(cameraToBodyVec);
    double l = cameraToBodyVec.len();
    double projL = Math.cos(sepAngleR) * l;
    // Define ray as r(t) = positionInSpaceM + t*edge.normalize()
    double radicand = projL * projL - (l * l - r * r);
    if (radicand < 0) {
      return null;
    }
    double t = projL - Math.sqrt(radicand);
    // If intersection is behind
    if (t < 0) {
      return null;
    }
    // Position of the point on the surface of the sphere
    Vector3D posSurface = Vector3D.add(curPosM, Vector3D.normalize(edge).scalarMult(t));
    Vector3D centerToSurf = Vector3D.sub(posSurface, body.getPos());

    // Get longitude and latitude
    Vector3D north = body.getNorth();
    Vector3D equator = body.getEquator();
    double latitude = centerToSurf.separationAngle(north);
    double longitude = Vector3D.projectOnPlane(centerToSurf, north).separationAngle(equator);
    if (Vector3D.cross(north, equator).dot(centerToSurf) < 0) {
      longitude = -longitude + Math.PI * 2;
    }

    // Find UV coord from surface point
    return new Point2D.Double(longitude / (2 * Math.PI), latitude / Math.PI);
  }

  /**
   * Get approximated value for the position and radius of projected sphere
   *
   * @param body The sphere projection to approximate
   * @return Return the approximated radius, position in x and position in y
   *         (pixel)
   */
  private double[] getApproximateCenterAndRad(Body body) {
    // Finding circle that contains the projection
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), curPosM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    double scaleDown = cameraToBodyVec.len();
    cameraToBodyVec.scalarDiv(scaleDown);
    double bodyRad = body.getRadius() / scaleDown;

    // Skip if inside body or too close to it
    if (bodyRad * 1.1 > cameraToBodyVec.len())
      return null;

    double angDiamDeg = 2 * Math.asin(bodyRad / cameraToBodyVec.len()) * DEGPERRAD;
    double angPosR = curOrientation.separationAngle(cameraToBodyVec);

    // Don't load body if behind camera
    if (angPosR * DEGPERRAD - angDiamDeg / 2.0 > 90) {
      return null;
    }

    // Vector pointing at the point closest to the center of the screen
    Vector3D closestPoint = Vector3D.qatRot(cameraToBodyVec, Quaternion.fromAxisAngle(angDiamDeg / 2.0 * RADPERDEG,
        Vector3D.cross(cameraToBodyVec, curOrientation)));
    // Separation angle in the vertical and horizontal direciton
    double separationAngleXD = Vector3D.projectOnPlane(closestPoint, curYAxis).separationAngle(curOrientation)
        * DEGPERRAD;
    double separationAngleYD = Vector3D.projectOnPlane(closestPoint, curXAxis).separationAngle(curOrientation)
        * DEGPERRAD;
    // Don't draw if closest point is out of bounds (with a bit of leniency)
    if (separationAngleYD > vFOV / 1.9 || separationAngleXD > hFOV / 1.9) {
      // The closest point can be out of bounds while the rest of the sphre is in
      // view.
      // In this case, we don't want to quit
      if (angPosR * DEGPERRAD - angDiamDeg / 2.0 > 0) {
        return null;
      }
    }

    // Take random value for dist to screen
    double screenDistM = cameraToBodyVec.len();
    double screenWidthM = 2 * Math.tan(hFOV / 2.0 * RADPERDEG) * screenDistM;
    double pixelPerMeter = screenWidthP / screenWidthM;

    double centerShiftM = 0;
    // If the body is almost behind the camera, just draw as if it were closer and
    // then shift at the end
    double largestAngleD = 70;
    if (angPosR * DEGPERRAD + angDiamDeg / 2.0 > largestAngleD) {
      // Angle that makes sure the body does not cross the angle threshold
      double adjustedAngPosR = (largestAngleD - angDiamDeg / 2.0) / DEGPERRAD;
      double closestDistBeforeM = Math.tan(angPosR - angDiamDeg * RADPERDEG / 2.0) * screenDistM;
      double closestDistAfterM = Math.tan(adjustedAngPosR - angDiamDeg * RADPERDEG / 2.0)
          * screenDistM;

      centerShiftM = closestDistBeforeM - closestDistAfterM;
      // Adjust body to make it look like it is still positioned before the camera
      // plane
      cameraToBodyVec.qatRot(Quaternion.fromAxisAngle(angPosR - adjustedAngPosR,
          Vector3D.cross(cameraToBodyVec, curOrientation)));
      // New angular position that assumes the body is positioned before the camera
      // plane
      angPosR = adjustedAngPosR;
    }
    double lengthToScreenM = screenDistM / Math.abs(Math.cos(angPosR));
    cameraToBodyVec.normalize().scalarMult(lengthToScreenM);
    Vector3D projectionToScreen = new Vector3D(cameraToBodyVec).projectOnPlane(curOrientation);
    double xAngleR = projectionToScreen.separationAngle(curXAxis);
    // Relevant distances of projection
    double closestDist = Math.tan(angPosR - angDiamDeg / 2.0 * RADPERDEG) * screenDistM;
    double furthestDist = Math.tan(angPosR + angDiamDeg / 2.0 * RADPERDEG) * screenDistM;

    double xSign = Math.signum(cameraToBodyVec.dot(curXAxis));
    double ySign = Math.signum(cameraToBodyVec.dot(curYAxis));
    xSign = xSign == 0 ? 1 : xSign;
    ySign = ySign == 0 ? 1 : ySign;

    // Distance to center of the projection on screen
    double distToCentOfProj = 0.5 * (furthestDist + closestDist);

    // Width and height of projected ellipse
    double radM = (furthestDist - closestDist) / 2.0;

    // Get projections
    distToCentOfProj += centerShiftM;// Add shift from approximation
    double[] app = new double[] { radM * pixelPerMeter,
        xSign * distToCentOfProj * Math.abs(Math.cos(xAngleR)) * pixelPerMeter,
        ySign * distToCentOfProj * Math.abs(Math.sin(xAngleR)) * pixelPerMeter };
    return app;
  }

  /**
   * Projects sphere on plane with textures
   */
  private void textureView(Graphics2D g2d, Body body) {
    // Get approximated value for radius and position of projection
    // Use this to better approximate ray paths
    double[] approx = getApproximateCenterAndRad(body);
    if (approx == null) {
      return;
    }

    double approxRad = approx[0];
    double approxCenterX = approx[1];
    double approxCenterY = approx[2];
    // Circle to iterate over
    int rp = (int) Math.ceil(approxRad);
    Point2D imCenter = new Point.Double(approxCenterX + screenWidthP / 2.0, approxCenterY + screenHeightP / 2.0);
    int xc = (int) imCenter.getX();
    int yc = (int) imCenter.getY();

    // Decide pixBlockSize based on approximation radius
    int pixBlock = 1;
    int diam = (rp * 2) / pixBlock + 1;
    if (diam <= 0) {
      return;
    }
    // Doubles the curPixBlockSize when the imagesize >= maxImSize. Then triples at
    // maxImSize*base, quadruples at maxImSize*base² and so on...
    double base = 2.3;
    int ratioLog = (int) Math.ceil(Math.log(diam / (double) maxImSize) / Math.log(base)) + 1;
    // Size of pixel blocks in the image. 1 = full quality
    int curPixBlock = pixBlock;
    if (maxImSize > 0 && ratioLog > 1) {
      curPixBlock = ratioLog * pixBlock;
    }
    // Texture related elements
    // Expand final picture by pixBlockSize, so divide its initial size by it
    BufferedImage image = new BufferedImage((rp * 2) / curPixBlock + 1, (rp * 2) / curPixBlock + 1,
        BufferedImage.TYPE_INT_ARGB);
    // Graphics2D g2t = texProj.createGraphics();
    WritableRaster raster = image.getRaster();

    // Fill in the circle (optimized... or is it?)
    // bresenhams-circle-algorithm modified to fill in the circle

    // Reduced radius
    int rr = rp / curPixBlock;

    // Fill first octant horizontally + symmetry
    int y = rr;
    // Make sure the y component does not start out of bounds
    // (not necessary, remove if bugged)
    if (yc > screenHeightP / 2.0 && yc - rp < 0) {
      y = yc / curPixBlock;
    } else if (yc < screenHeightP / 2.0 && yc + rp > screenHeightP) {
      y = ((int) screenHeightP - yc) / curPixBlock;
    }
    int x = (int) Math.sqrt(rr * rr - y * y);
    int d = 2 * (x + 1) * (x + 1) + y * y + (y - 1) * (y - 1) - 2 * (rr * rr);
    // Iterate as if it were a smaller circle, cause we're iterating over the image
    // circle, which is smaller.
    while (y >= x) {
      if (d <= 0) {
        d = d + (4 * x) + 6;
      } else {
        // TODO: separate getting uv texture and drawing
        drawTLine(-x * curPixBlock, y * curPixBlock, x * curPixBlock, raster, imCenter, rp, body, curPixBlock);
        drawTLine(-x * curPixBlock, -y * curPixBlock, x * curPixBlock, raster, imCenter, rp, body, curPixBlock);
        d = d + 4 * (x - y) + 10;
        y--;
      }
      x++;
    }
    // Fill second octant horizontally + symmetry
    y = 0;
    // Make sure the y component does not start out of bounds
    // (not necessary, remove if bugged)
    if (yc > screenHeightP) {
      y = (yc - (int) screenHeightP) / curPixBlock;
    } else if (yc < 0) {
      y = -yc / curPixBlock;
    }
    x = (int) Math.sqrt(rr * rr - y * y);
    d = 2 * (y + 1) * (y + 1) + x * x + (x - 1) * (x - 1) - 2 * (rr * rr);
    while (y < x) {
      drawTLine(-x * curPixBlock, y * curPixBlock, x * curPixBlock, raster, imCenter, rp, body, curPixBlock);
      if (y != 0)
        drawTLine(-x * curPixBlock, -y * curPixBlock, x * curPixBlock, raster, imCenter, rp, body, curPixBlock);
      if (d <= 0) {
        d = d + (4 * y) + 6;
      } else {
        d = d + 4 * (y - x) + 10;
        x--;
      }
      y++;
    }
    g2d.drawImage(image, xc - rp, yc - rp, image.getWidth() * curPixBlock, image.getHeight() * curPixBlock,
        null);
    // Shows where the rays are being sent
    // g2d.setColor(new Color(255, 0, 0, 100));
    // g2d.fill(new Ellipse2D.Double(screenWidthP / 2 + approxCenterX - approxRad,
    // screenHeightP / 2 + approxCenterY - approxRad, 2 * approxRad, 2 *
    // approxRad));
  }

  /**
   * Draw a textured line on the imageBuffer where the sphere intersects with the
   * screen
   *
   * @param x        Starting x position of the line to be drawn (pixels)
   * @param y        y position of the line to be drawn (pixels)
   * @param x        Final x position of the line to be drawn (pixels)
   * @param imRaster Pixel raster for the imageBuffer that will be drawn
   * @param imCenter Center of the circle and imageBuffer
   * @param rp       Radius of circle within imageBuffer (pixel)
   * @param body     Body which will be painted on the imageBuffer
   */
  private void drawTLine(int x, int y, int xe, WritableRaster imRaster, Point2D imCenter, int rp, Body body,
      int curPixBlockSize) {
    int xc = (int) imCenter.getX();
    int yc = (int) imCenter.getY();
    int absY = y + yc;
    int absX = x + xc;
    int absXe = xe + xc;
    // Ensure the line is not drawn fully outside of the screen
    if (absY < 0 || absY > screenHeightP || absXe < 0 || absX > screenWidthP) {
      return;
    }
    // Adjust bounds to not draw pixels outside of screen
    if (absX < 0) {
      x = -xc;
    }
    if (absXe > screenWidthP) {
      xe = (int) screenWidthP - xc;
    }
    // Get image pixels from image buffer
    int[] pixIm = ((DataBufferInt) imRaster.getDataBuffer()).getData();
    int imWidth = imRaster.getWidth();

    // Get texture info
    WritableRaster texRaster = body.getUVMap().getRaster();
    int texWidth = texRaster.getWidth();
    int texHeight = texRaster.getHeight();
    byte[] pixTex = ((DataBufferByte) texRaster.getDataBuffer()).getData();
    // nb of colors per pixel
    int bands = 3;

    for (int i = x; i <= xe; i += curPixBlockSize) {
      // Check if sphere is in the path of the pixel
      Point2D UV = spaceToUVSphere(screenToSpace(i + xc, y + yc), body);
      if (UV == null) {
        continue;
      }
      // Makes sure value is less than 1, else we have an out of bounds
      if (UV.getX() == 1) {
        UV.setLocation((texWidth - 1) / (double) texWidth, UV.getY());
      }
      if (UV.getY() == 1) {
        UV.setLocation(UV.getX(), (texHeight - 1) / (double) texHeight);
      }
      // (we will have false-false-...-false-true-true-...-true-false-break)
      // This is because after missing the sphere the second time, it will never hit
      // the sphere again, so we break TODO: implement this check
      int indexTex = ((int) (UV.getY() * texHeight) * texWidth + (int) (UV.getX() * texWidth)) * bands;
      int indexIm = (y + rp) / curPixBlockSize * imWidth + (i + rp) / curPixBlockSize;
      // Apply texture color, stored as ARGB
      pixIm[indexIm] = (0xFF << 24) |
          (pixTex[indexTex] & 0xFF) |
          ((pixTex[indexTex + 1] & 0xFF) << 8) |
          ((pixTex[indexTex + 2] & 0xFF) << 16);
    }
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
    cameraToBodyVec.setComponents(body.getPos()).sub(curPosM);

    // Scale everything down by the distance from the body to the camera
    // Increases numerical stability?
    // During computation, scale everything down to alleviate numerical errors
    double scaleDown = cameraToBodyVec.len();
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
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), curPosM);

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
        Vector3D posInSpaceM = new Vector3D(curPosM).scalarDiv(scaleDown);
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
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), curPosM);

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
        Vector3D posInSpaceM = new Vector3D(curPosM).scalarDiv(scaleDown);
        Vector3D bodyPosInSpaceM = new Vector3D(posInSpaceM).add(cameraToBodyVec);

        // Uncomment this to only show visible part of sphere (subjective view)
        // Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c *
        // RADPERDEG, cameraToBodyVec);
        // Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(curXAxis, midQuat),
        // cameraToBodyVec);
        Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c * RADPERDEG, iniOrientation);
        Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(iniXAxis, midQuat), iniOrientation);
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
        Vector3D centerToSurfM = new Vector3D(iniOrientation).normalize().scalarMult(-bodyRad).qatRot(quat);

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
    Vector3D cameraToBodyVec = Vector3D.sub(body.getPos(), curPosM);

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

    for (int c = 0; c < contourDots; c++) {
      for (int i = 0; i <= innerDots; i++) {
        Vector3D posInSpaceM = new Vector3D(curPosM).scalarDiv(scaleDown);
        Vector3D bodyPosInSpaceM = new Vector3D(posInSpaceM).add(cameraToBodyVec);

        Quaternion midQuat = Quaternion.fromAxisAngle(360.0 / contourDots * c * RADPERDEG, iniOrientation);
        Vector3D rotAxis = Vector3D.cross(Vector3D.qatRot(iniXAxis, midQuat), iniOrientation);
        if (rotAxis.len() == 0) {
          rotAxis.setComponents(curYAxis);
        }
        Quaternion quat = Quaternion.fromAxisAngle(180 / ((double) innerDots) * i * RADPERDEG,
            rotAxis);

        // Vector going from the center of the sphere onto the surface
        Vector3D centerToSurfM = new Vector3D(iniOrientation).normalize().scalarMult(-bodyRad).qatRot(quat);

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
  public Vector3D getCurPosM() {
    return new Vector3D(curPosM);
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
    curOrientation = Vector3D.qatRot(iniOrientation, rotQ);
    curXAxis = Vector3D.qatRot(iniXAxis, rotQ);
    curYAxis = Vector3D.qatRot(iniYAxis, rotQ);
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
    curPosM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
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
    curPosM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
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
    curPosM.add(direction.scalarMult(stepDistanceM * Math.pow(boostValue, nbBoosts)));
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

  /**
   * Getter for the screenRatio
   *
   * @return The screen ratio
   */
  public double getScreenRatio() {
    return screenRatio;
  }

  /**
   * Getter for the rotation quaternion
   *
   * @return The quaternion
   */
  public Quaternion getRotQ() {
    return rotQ;
  }

  /**
   * Reset camera position to initialPositionM
   */
  public void reset() {
    curPosM.setComponents(iniPosM);
    curXAxis.setComponents(iniXAxis);
    curYAxis.setComponents(iniYAxis);
    curOrientation.setComponents(iniOrientation);
    rotQ = new Quaternion();
  }

  /**
   * Setter for maxImSize
   *
   * @param s New maximum image size before quality reduction
   */
  public void setMaxImSize(int s) {
    maxImSize = s;
  }

  /**
   * Getter for the maximum image size
   *
   * @return The maximum image size before quality reduction
   */
  public int getMaxImSize() {
    return maxImSize;
  }

  /**
   * Obtain the sensitivity of the mouse when in 3D mode
   *
   * @return the sensitivity value
   */
  public double getSensitivity() {
    return sensitivity;
  }

  /**
   * Set the sensitivity of the mouse when in 3D mode
   *
   * @param s The new sensitivity value
   */
  public void setSensitivity(double s) {
    sensitivity = s;
  }

  /**
   * Sets new position for the camera
   *
   * @param newPos New camera position
   */
  public void setCurPosM(Vector3D newPos) {
    curPosM.setComponents(newPos);
  }

  /**
   * Angles the camera towards a point
   *
   * @param pos Where to look at
   */
  public void lookAt(Vector3D pos) {
    Vector3D direction = Vector3D.sub(pos, curPosM);
    Vector3D rotVec = Vector3D.cross(curOrientation, direction);
    double angle = Vector3D.separationAngle(direction, curOrientation);
    // Don't do anything if you're already looking at the planet
    if (angle == 0 || rotVec.len() < 0.000001) {
      return;
    }
    Quaternion rot = Quaternion.fromAxisAngle(angle, rotVec.normalize());
    rotQ.mulQuaternionReverse(rot);
    // Update orientations
    curOrientation = Vector3D.qatRot(iniOrientation, rotQ);
    curXAxis = Vector3D.qatRot(iniXAxis, rotQ);
    curYAxis = Vector3D.qatRot(iniYAxis, rotQ);
  }

  /**
   * Get the scale of planets
   *
   * @return The value used to scale the radius
   */
  public int getScale() {
    return scale;
  }
  /**
   * Set the scale of planets
   *
   * @param scale The new scale value for the radius
   */
  public void setScale(int scale) {
    this.scale = scale;
  }
}
