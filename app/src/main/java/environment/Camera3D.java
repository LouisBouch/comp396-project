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

  Quaternion rotQ;


  /**
   * Creates basic camera with only position
   *
   * @param positionInSpaceM The position of the camera in space in meters
   * @param solarSystem The solar system in which the camera belongs
   */
  public Camera3D(Vector3D positionInSpaceM, SolarSystem solarSystem) {
    this.solarSystem = solarSystem;
    this.positionInSpaceM = positionInSpaceM;
    orientation = new Vector3D(0, 0, 1);
    xAxisDirection = new Vector3D(1, 0, 0);
    yAxisDirection = new Vector3D(0, 1, 0);
    rotQ = new Quaternion();
  }

  @Override
  public void paintThis(Graphics2D g2d) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'paintThis'");
  }

}
