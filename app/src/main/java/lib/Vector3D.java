package lib;

public class Vector3D {

  double[] position = new double[3];

  /**
   * Creates a 3D vector given 3 coordinates
   * 
   * @param x in meters
   * @param y in meters
   * @param z in meters
   */
  public Vector3D(double x, double y, double z) {
    position[0] = x;
    position[1] = y;
    position[2] = z;
  }

  /**
   * Creates a 3D vector given a position array
   * 
   * @param position a position vector containing the vector coordinates in meters
   */
  public Vector3D(double[] position) {
    this.position[0] = position[0];
    this.position[1] = position[1];
    this.position[2] = position[2];
  }

  /**
   * Creates a 3D vector initialized to 0 for all 3 coordinates
   */
  public Vector3D() {
    position[0] = 0;
    position[1] = 0;
    position[2] = 0;
  }

  /**
   * Getter for the position
   * 
   * @return Returns the position vector
   */
  public double[] getPosition() {
    return position;
  }

  /**
   * Getter for the x value
   * 
   * @return Returns x value of position in meters
   */
  public double getX() {
    return position[0];
  }

  /**
   * Getter for the y value
   * 
   * @return Returns y value of position in meters
   */
  public double getY() {
    return position[1];
  }

  /**
   * Getter for the z value
   * 
   * @return Returns z value of position in meters
   */
  public double getZ() {
    return position[2];
  }

  /**
   * Obtains length of vector
   *
   * @return returns the length of the 2 norm of our vector
   */
  public double len() {
    return Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY() + this.getZ() * this.getZ());
  }

  /**
   * Adds Vector3D to the current Vector3D
   * 
   * @vec Position Vector3D to add to our current Vector3D
   */
  public void add(Vector3D vec) {
    position[0] += vec.getX();
    position[1] += vec.getY();
    position[2] += vec.getZ();
  }

  /**
   * Adds two Vector3D together and return new Vector3D that is the sum of both
   *
   * @v1 First Vector3D to add
   * @v2 Second Vector3D to add
   * @return Vector3D that is the sum of both Vector3Ds
   */
  public static Vector3D add(Vector3D v1, Vector3D v2) {
    return new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
  }

  /**
   * Subtracts Vector3D to the current Vector3D
   * 
   * @vec Position Vector3D to subtract to from current Vector3D
   */
  public void sub(Vector3D vec) {
    position[0] -= vec.getX();
    position[1] -= vec.getY();
    position[2] -= vec.getZ();
  }

  /**
   * Subtracts one Vector3D from another and return new Vector3D that is the
   * difference of both
   *
   * @v1 Vector3D to subtract from
   * @v2 Vector3D used to subtract
   * @return Vector3D that is the difference of v1-v2.
   */
  public static Vector3D sub(Vector3D v1, Vector3D v2) {
    return new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
  }

  /**
   * Multiply Vector3D by a scalar
   * 
   * @scalar scalar used to multiply our Vector3D
   */
  public void scalarMult(double scalar) {
    position[0] *= scalar;
    position[1] *= scalar;
    position[2] *= scalar;
  }

  /**
   * Scales a Vector3D by a scalar
   *
   * @v Initial Vector3D
   * @scalar scalar used to multiply our Vector3D
   * @return scaled Vector3D
   */
  public static Vector3D scalarMult(Vector3D v, double scalar) {
    return new Vector3D(v.getX() * scalar, v.getY() * scalar, v.getZ() * scalar);
  }

  /**
   * Divides Vector3D by a scalar
   * 
   * @scalar scalar used to divide our Vector3D
   */
  public void scalarDiv(double scalar) {
    position[0] /= scalar;
    position[1] /= scalar;
    position[2] /= scalar;
  }

  /**
   * Divides a Vector3D by a scalar
   *
   * @v Initial Vector3D
   * @scalar scalar used to divide our Vector3D
   * @return divided Vector3D
   */
  public static Vector3D scalarDiv(Vector3D v, double scalar) {
    return new Vector3D(v.getX() / scalar, v.getY() / scalar, v.getZ() / scalar);
  }

  /**
   * Exponentiate Vector3D by a scalar
   * 
   * @scalar scalar used to exponentiate our Vector3D
   */
  public void scalarExp(double scalar) {
    position[0] = Math.pow(position[0], scalar);
    position[1] = Math.pow(position[1], scalar);
    position[2] = Math.pow(position[2], scalar);
  }

  /**
   * Exponentiate Vector3D by a scalar
   *
   * @v Initial Vector3D
   * @scalar scalar used to exponentiate our Vector3D
   * @return exponentiated Vector3D
   */
  public static Vector3D scalarExp(Vector3D v, double scalar) {
    return new Vector3D(Math.pow(v.getX(), scalar), Math.pow(v.getY(), scalar), Math.pow(v.getZ(), scalar));
  }

  /**
   * Dots Vector3D with the current Vector3D
   * 
   * @vec Position Vector3D to dot with our current Vector3D
   * @return resulting dot product
   */
  public double dot(Vector3D vec) {
    return getX() * vec.getX() + getY() * vec.getY() + getZ() * vec.getZ();
  }

  /**
   * Dots one Vector3D with another
   *
   * @v1 Vector3D first Vector3D
   * @v2 Vector3D second Vector3D
   * @return Resulting output form dott product
   */
  public static double dot(Vector3D v1, Vector3D v2) {
    return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
  }

  /**
   * Takes cross product of Vector3D with the current Vector3D
   * 
   * @vec Position Vector3D to cross with our current Vector3D
   * @return resulting cross product
   */
  public Vector3D cross(Vector3D vec) {
    double a1 = this.getX();
    double a2 = this.getY();
    double a3 = this.getZ();
    double b1 = vec.getX();
    double b2 = vec.getY();
    double b3 = vec.getZ();
    return new Vector3D(a2 * b3 - a3 * b2, a3 * b1 - a1 * b3, a1 * b2 - a2 * b1);
  }

  /**
   * Dots one Vector3D with another
   *
   * @v1 Vector3D first Vector3D
   * @v2 Vector3D second Vector3D
   * @return Resulting output form dott product
   */
  public static Vector3D cross(Vector3D v1, Vector3D v2) {
    return v1.cross(v2);
  }
}
