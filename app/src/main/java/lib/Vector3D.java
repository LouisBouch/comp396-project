package lib;

public class Vector3D {

  double[] components = new double[3];

  /**
   * Creates a 3D vector given 3 component
   * 
   * @param x x component
   * @param y y component
   * @param z z component
   */
  public Vector3D(double x, double y, double z) {
    components[0] = x;
    components[1] = y;
    components[2] = z;
  }

  /**
   * Creates a 3D vector given a component array
   * 
   * @param components A vector containing the vector components
   */
  public Vector3D(double[] components) {
    this.components[0] = components[0];
    this.components[1] = components[1];
    this.components[2] = components[2];
  }

  /**
   * Creates a 3D vector initialized to 0 for all 3 components
   */
  public Vector3D() {
    components[0] = 0;
    components[1] = 0;
    components[2] = 0;
  }

  /**
   * Getter for the Vector3D
   * 
   * @return Returns the Vector3D compnents
   */
  public double[] components() {
    return components;
  }

  /**
   * Getter for the x value
   * 
   * @return Returns x component
   */
  public double getX() {
    return components[0];
  }

  /**
   * Getter for the y value
   * 
   * @return Returns Y compoenent
   */
  public double getY() {
    return components[1];
  }

  /**
   * Getter for the z value
   * 
   * @return Returns Z component
   */
  public double getZ() {
    return components[2];
  }

  /**
   * Sets the value of the X value
   *
   * @param v value of the X component
   */
  public void setX(double v) {
    components[0] = v;
  }

  /**
   * Sets the value of the Y value
   *
   * @param v value of the Y component
   */
  public void setY(double v) {
    components[1] = v;
  }

  /**
   * Sets the value of the Z value
   *
   * @param v value of the Z component
   */
  public void setZ(double v) {
    components[2] = v;
  }

  /**
   * Sets all three value of the Vector3D
   *
   * @param comp The values of the vector to set
   */
  public void setComponents(double[] comp) {
    components[0] = comp[0];
    components[1] = comp[1];
    components[2] = comp[2];
  }

  /**
   * Copy values of another Vector3D
   *
   * @param vec The Vector3D to copy
   */
  public void setComponents(Vector3D vec) {
    components[0] = vec.getX();
    components[1] = vec.getY();
    components[2] = vec.getZ();
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
   * @param vec Vector3D to add to our current Vector3D
   */
  public void add(Vector3D vec) {
    components[0] += vec.getX();
    components[1] += vec.getY();
    components[2] += vec.getZ();
  }

  /**
   * Adds two Vector3D together and return new Vector3D that is the sum of both
   *
   * @param v1 First Vector3D to add
   * @param v2 Second Vector3D to add
   * @return Vector3D that is the sum of both Vector3Ds
   */
  public static Vector3D add(Vector3D v1, Vector3D v2) {
    return new Vector3D(v1.getX() + v2.getX(), v1.getY() + v2.getY(), v1.getZ() + v2.getZ());
  }

  /**
   * Subtracts Vector3D to the current Vector3D
   * 
   * @param vec Vector3D to subtract to from current Vector3D
   */
  public void sub(Vector3D vec) {
    components[0] -= vec.getX();
    components[1] -= vec.getY();
    components[2] -= vec.getZ();
  }

  /**
   * Subtracts one Vector3D from another and return new Vector3D that is the
   * difference of both
   *
   * @param v1 Vector3D to subtract from
   * @param v2 Vector3D used to subtract
   * @return Vector3D that is the difference of v1-v2.
   */
  public static Vector3D sub(Vector3D v1, Vector3D v2) {
    return new Vector3D(v1.getX() - v2.getX(), v1.getY() - v2.getY(), v1.getZ() - v2.getZ());
  }

  /**
   * Multiply Vector3D by a scalar
   * 
   * @param scalar scalar used to multiply our Vector3D
   */
  public void scalarMult(double scalar) {
    components[0] *= scalar;
    components[1] *= scalar;
    components[2] *= scalar;
  }

  /**
   * Scales a Vector3D by a scalar
   *
   * @param v Initial Vector3D
   * @param scalar scalar used to multiply our Vector3D
   * @return scaled Vector3D
   */
  public static Vector3D scalarMult(Vector3D v, double scalar) {
    return new Vector3D(v.getX() * scalar, v.getY() * scalar, v.getZ() * scalar);
  }

  /**
   * Divides Vector3D by a scalar
   * 
   * @param scalar scalar used to divide our Vector3D
   */
  public void scalarDiv(double scalar) {
    components[0] /= scalar;
    components[1] /= scalar;
    components[2] /= scalar;
  }

  /**
   * Divides a Vector3D by a scalar
   *
   * @param v Initial Vector3D
   * @param scalar scalar used to divide our Vector3D
   * @return divided Vector3D
   */
  public static Vector3D scalarDiv(Vector3D v, double scalar) {
    return new Vector3D(v.getX() / scalar, v.getY() / scalar, v.getZ() / scalar);
  }

  /**
   * Exponentiate Vector3D by a scalar
   * 
   * @param scalar scalar used to exponentiate our Vector3D
   */
  public void scalarExp(double scalar) {
    components[0] = Math.pow(components[0], scalar);
    components[1] = Math.pow(components[1], scalar);
    components[2] = Math.pow(components[2], scalar);
  }

  /**
   * Exponentiate Vector3D by a scalar
   *
   * @param v Initial Vector3D
   * @param scalar scalar used to exponentiate our Vector3D
   * @return exponentiated Vector3D
   */
  public static Vector3D scalarExp(Vector3D v, double scalar) {
    return new Vector3D(Math.pow(v.getX(), scalar), Math.pow(v.getY(), scalar), Math.pow(v.getZ(), scalar));
  }

  /**
   * Dots Vector3D with the current Vector3D
   * 
   * @param vec Vector3D to dot with our current Vector3D
   * @return resulting dot product
   */
  public double dot(Vector3D vec) {
    return getX() * vec.getX() + getY() * vec.getY() + getZ() * vec.getZ();
  }

  /**
   * Dots one Vector3D with another
   *
   * @param v1 Vector3D first Vector3D
   * @param v2 Vector3D second Vector3D
   * @return Resulting output form dott product
   */
  public static double dot(Vector3D v1, Vector3D v2) {
    return v1.getX() * v2.getX() + v1.getY() * v2.getY() + v1.getZ() * v2.getZ();
  }

  /**
   * Takes cross product of Vector3D with the current Vector3D
   * 
   * @param vec Vector3D to cross with our current Vector3D
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
   * @param v1 Vector3D first Vector3D
   * @param v2 Vector3D second Vector3D
   * @return Resulting output form dot product
   */
  public static Vector3D cross(Vector3D v1, Vector3D v2) {
    return v1.cross(v2);
  }
}
