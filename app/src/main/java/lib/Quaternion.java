package lib;

public class Quaternion {

  private double scalar;
  private Vector3D imaginaryVec;

  /**
   * Default constructor for quaternion
   */
  public Quaternion() {
    scalar = 1;
    imaginaryVec = new Vector3D();
  }

  /**
   * Constructor for initializing a quaternion
   *
   * @param scalar       The scalar component of the quaternion
   * @param imaginaryVec The imaginary part of the quaternion
   */
  public Quaternion(double scalar, Vector3D imaginaryVec) {
    this.scalar = scalar;
    this.imaginaryVec = imaginaryVec;
  }

  /**
   * Creates a Quaternion from an angle and an axis of rotation
   *
   * @param angle The rotation amount in radians
   * @param axis  The rotation axis
   *
   * @return The corresponding Quaternion
   */
  public static Quaternion fromAxisAngle(double angle, Vector3D axis) {
    Vector3D normalizedAxis = Vector3D.normalize(axis).scalarMult(Math.sin(angle / 2.0));
    return new Quaternion(Math.cos(angle / 2.0), normalizedAxis);
  }

  /**
   * Getter for scalar of Quaternion
   *
   * @return The scalar of the Quaternion
   */
  public double getScalar() {
    return scalar;
  }

  /**
   * Getter for i value of imaginaryVec of Quaternion
   *
   * @return The i value of the Quaternion
   */
  public double getI() {
    return imaginaryVec.getX();
  }

  /**
   * Getter for j value of imaginaryVec of Quaternion
   *
   * @return The j value of the Quaternion
   */
  public double getJ() {
    return imaginaryVec.getY();
  }

  /**
   * Getter for k value of imaginaryVec of Quaternion
   *
   * @return The k value of the Quaternion
   */
  public double getK() {
    return imaginaryVec.getZ();
  }

  /**
   * Getter for imaginary part of Quaternion
   *
   * @return The imaginary part of the Quaternion
   */
  public Vector3D getImaginaryVector() {
    return imaginaryVec;
  }

  /**
   * Setter for scalar of Quaternion
   *
   * @param s The new scalar of the Quaternion
   */
  public void setScalar(double s) {
    scalar = s;
  }

  /**
   * Setter for i value of Quaternion
   *
   * @param i The new i value of the Quaternion
   */
  public void setI(double i) {
    imaginaryVec.setX(i);
  }

  /**
   * Setter for j value of Quaternion
   *
   * @param j The new j value of the Quaternion
   */
  public void setJ(double j) {
    imaginaryVec.setY(j);
  }

  /**
   * Setter for k value of Quaternion
   *
   * @param k The new k value of the Quaternion
   */
  public void setK(double k) {
    imaginaryVec.setZ(k);
  }

  /**
   * Setter for imaginary part of Quaternion
   *
   * @param imaginaryVec The new imaginary part of the Quaternion
   */
  public void setImaginaryVector(Vector3D imaginaryVec) {
    this.imaginaryVec = imaginaryVec;
  }

  /**
   * Normalize the quaternion, which is necessary to use it
   */
  public Quaternion normalize() {
    double len = Math.sqrt(scalar * scalar + imaginaryVec.getX() * imaginaryVec.getX()
        + imaginaryVec.getY() * imaginaryVec.getY() + imaginaryVec.getZ() * imaginaryVec.getZ());
    scalar /= len;
    imaginaryVec.scalarDiv(len);
    return this;
  }

  /**
   * Normalize the quaternion
   *
   * @param q The Quaternion to normalize
   * 
   * @return The normalized Quaternion
   */
  public static Quaternion normalize(Quaternion q) {
    double scalar = q.getScalar();
    Vector3D imaginaryVec = q.getImaginaryVector();
    double len = Math.sqrt(q.lenSquared());
    return new Quaternion(scalar / len, Vector3D.scalarDiv(imaginaryVec, len));
  }

  /**
   * Get the length squared of a Quaternion
   */
  public double lenSquared() {
    return scalar * scalar + imaginaryVec.getX() * imaginaryVec.getX()
        + imaginaryVec.getY() * imaginaryVec.getY() + imaginaryVec.getZ() * imaginaryVec.getZ();
  }

  /**
   * Get the length squared of a Quaternion
   */
  public double lenSquared(Quaternion q) {
    double scalar = q.getScalar();
    Vector3D imaginaryVec = q.getImaginaryVector();
    return scalar * scalar + imaginaryVec.getX() * imaginaryVec.getX()
        + imaginaryVec.getY() * imaginaryVec.getY() + imaginaryVec.getZ() * imaginaryVec.getZ();
  }

  /**
   * Multiply self Quaternion with another quaternion
   *
   * @param q Quaternion used for multiplication (self=self*q)
   */
  public Quaternion mulQuaternion(Quaternion q) {
    double s1 = scalar;
    double i1 = imaginaryVec.getX();
    double j1 = imaginaryVec.getY();
    double k1 = imaginaryVec.getZ();

    Vector3D imaginaryVec2 = q.getImaginaryVector();
    double s2 = q.getScalar();
    double i2 = imaginaryVec2.getX();
    double j2 = imaginaryVec2.getY();
    double k2 = imaginaryVec2.getZ();

    double newS = s1 * s2 - i1 * i2 - j1 * j2 - k1 * k2;
    double newI = s1 * i2 + i1 * s2 + j1 * k2 - k1 * j2;
    double newJ = s1 * j2 - i1 * k2 + j1 * s2 + k1 * i2;
    double newK = s1 * k2 + i1 * j2 - j1 * i2 + k1 * s2;

    // Works, but requires creating new objects
    // double newS = scalar * scalar2 - imaginaryVec.dot(imaginaryVec2);
    // double newI = (imaginaryVec2.getX() * scalar + scalar2 * imaginaryVec.getX()
    // + Vector3D.cross(imaginaryVec, imaginaryVec2).getX());
    // double newJ = (imaginaryVec2.getY() * scalar + scalar2 * imaginaryVec.getY()
    // + Vector3D.cross(imaginaryVec, imaginaryVec2).getY());
    // double newK = (imaginaryVec2.getZ() * scalar + scalar2 * imaginaryVec.getZ()
    // + Vector3D.cross(imaginaryVec, imaginaryVec2).getZ());
    this.setScalar(newS);
    this.setI(newI);
    this.setJ(newJ);
    this.setK(newK);
    return this;
  }

  /**
   * Multiply self Quaternion with another quaternion in the reverse order
   *
   * @param q Quaternion used for multiplication (self=q*self)
   */
  public Quaternion mulQuaternionReverse(Quaternion q) {
    double s2 = scalar;
    double i2 = imaginaryVec.getX();
    double j2 = imaginaryVec.getY();
    double k2 = imaginaryVec.getZ();

    Vector3D imaginaryVec2 = q.getImaginaryVector();
    double s1 = q.getScalar();
    double i1 = imaginaryVec2.getX();
    double j1 = imaginaryVec2.getY();
    double k1 = imaginaryVec2.getZ();

    double newS = s1 * s2 - i1 * i2 - j1 * j2 - k1 * k2;
    double newI = s1 * i2 + i1 * s2 + j1 * k2 - k1 * j2;
    double newJ = s1 * j2 - i1 * k2 + j1 * s2 + k1 * i2;
    double newK = s1 * k2 + i1 * j2 - j1 * i2 + k1 * s2;



    // Works but creates objects
    //double newS = scalar * scalar2 - imaginaryVec.dot(imaginaryVec2);
    //double newI = (imaginaryVec2.getX() * scalar + scalar2 * imaginaryVec.getX()
    //    + Vector3D.cross(imaginaryVec2, imaginaryVec).getX());
    //double newJ = (imaginaryVec2.getY() * scalar + scalar2 * imaginaryVec.getY()
    //    + Vector3D.cross(imaginaryVec2, imaginaryVec).getY());
    //double newK = (imaginaryVec2.getZ() * scalar + scalar2 * imaginaryVec.getZ()
    //    + Vector3D.cross(imaginaryVec2, imaginaryVec).getZ());
    this.setScalar(newS);
    this.setI(newI);
    this.setJ(newJ);
    this.setK(newK);
    return this;
  }

  /**
   * Multiply two quaternions
   *
   * @param q1 First Quaternion used for multiplication
   * @param q2 Second Quaternion used for multiplication
   *
   * @return Multiplied Quaternion (q1*q2)
   */
  public static Quaternion mulQuaternion(Quaternion q1, Quaternion q2) {
    double s1 = q1.getScalar();
    double i1 = q1.getImaginaryVector().getX();
    double j1 = q1.getImaginaryVector().getY();
    double k1 = q1.getImaginaryVector().getZ();

    Vector3D imaginaryVec2 = q2.getImaginaryVector();
    double s2 = q2.getScalar();
    double i2 = imaginaryVec2.getX();
    double j2 = imaginaryVec2.getY();
    double k2 = imaginaryVec2.getZ();

    double newS = s1 * s2 - i1 * i2 - j1 * j2 - k1 * k2;
    double newI = s1 * i2 + i1 * s2 + j1 * k2 - k1 * j2;
    double newJ = s1 * j2 - i1 * k2 + j1 * s2 + k1 * i2;
    double newK = s1 * k2 + i1 * j2 - j1 * i2 + k1 * s2;

    return new Quaternion(newS, new Vector3D(newI, newJ, newK));
  }

  /**
   * Conjugates quaternion
   */
  public Quaternion conjugate() {
    imaginaryVec.scalarMult(-1);
    return this;
  }

  /**
   * Conjugates quaternion
   */
  public static Quaternion conjugate(Quaternion q) {
    return new Quaternion(q.getScalar(), Vector3D.scalarMult(q.getImaginaryVector(), -1));
  }

  /**
   * Inverts quaternion
   */
  public Quaternion invert() {
    double lenSquared = this.lenSquared();
    imaginaryVec.scalarDiv(-lenSquared);
    scalar /= -lenSquared;
    return this;
  }

  /**
   * Inverts quaternion
   *
   * @param q Quaternion to invert
   *
   * @return inverted Quaternion
   */
  public static Quaternion invert(Quaternion q) {
    double lenSquared = q.lenSquared();
    return new Quaternion(q.getScalar() / -lenSquared, Vector3D.scalarDiv(q.getImaginaryVector(), lenSquared));
  }

}
