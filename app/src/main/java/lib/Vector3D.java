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
   * Creates a 3D vector given a Vector3D
   * 
   * @param v Vector3D to copy
   */
  public Vector3D(Vector3D v) {
    components[0] = v.getX();
    components[1] = v.getY();
    components[2] = v.getZ();
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
  public Vector3D setComponents(double[] comp) {
    components[0] = comp[0];
    components[1] = comp[1];
    components[2] = comp[2];
    return this;
  }
  /**
   * Sets all three value of the Vector3D
   *
   * @param x The x value of the vector
   * @param y The y value of the vector
   * @param z The z value of the vector
   */
  public Vector3D setComponents(double x, double y, double z) {
    components[0] = x;
    components[1] = y;
    components[2] = z;
    return this;
  }

  /**
   * Copy values of another Vector3D
   *
   * @param vec The Vector3D to copy
   */
  public Vector3D setComponents(Vector3D vec) {
    components[0] = vec.getX();
    components[1] = vec.getY();
    components[2] = vec.getZ();
    return this;
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
   * Obtains length of vector
   * 
   * @param v Vector3D from which to get the length
   *
   * @return returns the length of the 2 norm of our vector
   */
  public static double len(Vector3D v) {
    return Math.sqrt(v.getX() * v.getX() + v.getY() * v.getY() + v.getZ() * v.getZ());
  }

  /**
   * Normalize the self Vector3D
   */
  public Vector3D normalize() {
    double len = this.len();
    this.setX(this.getX() / len);
    this.setY(this.getY() / len);
    this.setZ(this.getZ() / len);
    return this;
  }

  /**
   * Normalize the Vector3D
   *
   * @param v The Vector3D to normalize
   *
   * @return The new normalized vector
   */
  public static Vector3D normalize(Vector3D v) {
    double len = v.len();
    return new Vector3D(v.getX() / len, v.getY() / len, v.getZ() / len);
  }

  /**
   * Project the self Vector3D on v
   *
   * @param v The vector to project onto
   */
  public Vector3D project(Vector3D v) {
    double scalar = this.dot(v) / v.dot(v);
    this.setX(scalar * v.getX());
    this.setY(scalar * v.getY());
    this.setZ(scalar * v.getZ());
    return this;
  }

  /**
   * Project a Vector3D onto another
   *
   * @param v1 The vector to project
   * @param v2 The vector to project onto
   *
   * @return The resulting projected Vector3D
   */
  public static Vector3D project(Vector3D v1, Vector3D v2) {
    double scalar = v1.dot(v2) / v2.dot(v2);
    return Vector3D.scalarMult(v2, scalar);
  }

  /**
   * Adds Vector3D to the current Vector3D
   * 
   * @param vec Vector3D to add to our current Vector3D
   */
  public Vector3D add(Vector3D vec) {
    components[0] += vec.getX();
    components[1] += vec.getY();
    components[2] += vec.getZ();
    return this;
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
  public Vector3D sub(Vector3D vec) {
    components[0] -= vec.getX();
    components[1] -= vec.getY();
    components[2] -= vec.getZ();
    return this;
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
  public Vector3D scalarMult(double scalar) {
    components[0] *= scalar;
    components[1] *= scalar;
    components[2] *= scalar;
    return this;
  }

  /**
   * Scales a Vector3D by a scalar
   *
   * @param v      Initial Vector3D
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
  public Vector3D scalarDiv(double scalar) {
    components[0] /= scalar;
    components[1] /= scalar;
    components[2] /= scalar;
    return this;
  }

  /**
   * Divides a Vector3D by a scalar
   *
   * @param v      Initial Vector3D
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
  public Vector3D scalarExp(double scalar) {
    components[0] = Math.pow(components[0], scalar);
    components[1] = Math.pow(components[1], scalar);
    components[2] = Math.pow(components[2], scalar);
    return this;
  }

  /**
   * Exponentiate Vector3D by a scalar
   *
   * @param v      Initial Vector3D
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
    this.setX(a2 * b3 - a3 * b2);
    this.setY(a3 * b1 - a1 * b3);
    this.setZ(a1 * b2 - a2 * b1);
    return this;
  }

  /**
   * Crosses one Vector3D with another and saves the result in the first vector
   *
   * @param v1 Vector3D first Vector3D
   * @param v2 Vector3D second Vector3D
   */
  public static Vector3D cross(Vector3D v1, Vector3D v2) {
    double a1 = v1.getX();
    double a2 = v1.getY();
    double a3 = v1.getZ();
    double b1 = v2.getX();
    double b2 = v2.getY();
    double b3 = v2.getZ();
    return new Vector3D(a2 * b3 - a3 * b2, a3 * b1 - a1 * b3, a1 * b2 - a2 * b1);
  }

  /**
   * Rotates the vector with the help of a Quaternion
   *
   * @param q The quaternion that defines rotation
   *
   * @return The resulting changed vector
   */
  public Vector3D qatRot(Quaternion q) {
    // Start by normalizing vector
    double len = this.len();
    this.normalize();

    // Get values of quaternion
    double s = q.getScalar();
    double i = q.getImaginaryVector().getX();
    double j = q.getImaginaryVector().getY();
    double k = q.getImaginaryVector().getZ();

    // Make vector quaternion
    double sv = 0;
    double iv = getX();
    double jv = getY();
    double kv = getZ();

    // First multiplication (new = q*v)
    double newS = s * sv - i * iv - j * jv - k * kv;
    double newI = s * iv + i * sv + j * kv - k * jv;
    double newJ = s * jv - i * kv + j * sv + k * iv;
    double newK = s * kv + i * jv - j * iv + k * sv;

    // Second multiplication (final = new*q')
    sv = newS * s + newI * i + newJ * j + newK * k;
    iv = -newS * i + newI * s - newJ * k + newK * j;
    jv = -newS * j + newI * k + newJ * s - newK * i;
    kv = -newS * k - newI * j + newJ * i + newK * s;

    // Sets new rotated vector
    this.setX(iv);
    this.setY(jv);
    this.setZ(kv);
    // Give vector its length back
    this.scalarMult(len);
    return this;
  }

  /**
   * Rotates a vector with the help of a Quaternion
   *
   * @param v The vector to rotate
   * @param q The quaternion that defines rotation
   *
   * @return The resulting changed vector
   */
  public static Vector3D qatRot(Vector3D v, Quaternion q) {
    Vector3D newV = new Vector3D(v);
    // Start by normalizing vector
    double len = newV.len();
    newV.normalize();

    // Get values of quaternion
    double s = q.getScalar();
    double i = q.getImaginaryVector().getX();
    double j = q.getImaginaryVector().getY();
    double k = q.getImaginaryVector().getZ();

    // Make vector quaternion
    double sv = 0;
    double iv = newV.getX();
    double jv = newV.getY();
    double kv = newV.getZ();

    // First multiplication (new = q*v)
    double newS = s * sv - i * iv - j * jv - k * kv;
    double newI = s * iv + i * sv + j * kv - k * jv;
    double newJ = s * jv - i * kv + j * sv + k * iv;
    double newK = s * kv + i * jv - j * iv + k * sv;

    // Second multiplication (final = new*q')
    sv = newS * s + newI * i + newJ * j + newK * k;
    iv = -newS * i + newI * s - newJ * k + newK * j;
    jv = -newS * j + newI * k + newJ * s - newK * i;
    kv = -newS * k - newI * j + newJ * i + newK * s;

    // Sets new rotated vector
    newV.setX(iv);
    newV.setY(jv);
    newV.setZ(kv);

    // Give vector its length back
    newV.scalarMult(len);
    return newV;
  }

  /**
   * Get the angle of separation between Vector3Ds
   *
   * @param v Vector3D used to compute the separation angle
   *
   * @return The separation angle in Radians
   */
  public double separationAngle(Vector3D v) {
    if (this.len() == 0 || v.len() == 0) return 0;
    return Math.acos(this.dot(v) / (this.len() * v.len()));
  }

  /**
   * Get the angle of separation between two Vector3D
   *
   * @param v1 First Vector3D used to compute the separation angle
   * @param v2 Second Vector3D used to compute the separation angle
   *
   * @return The separation angle in Radians
   */
  public double separationAngle(Vector3D v1, Vector3D v2) {
    return v1.separationAngle(v2);
  }
}
