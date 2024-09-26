/*
 * This source file was generated by the Gradle 'init' task
 */
package lib;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class Vector3DTest {

  @BeforeEach
  public void setup() {
    // System.out.println("Vector3D tests");
  }

  // Tests the self.add method
  @Test
  public void testAddSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(4, 13, 20);
    vec1.add(vec2);
    assertEquals(14, vec1.getX());
    assertEquals(15, vec1.getY());
    assertEquals(23, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(4, -13, -20);
    vec1.add(vec2);
    assertEquals(-6, vec1.getX());
    assertEquals(-11, vec1.getY());
    assertEquals(-23, vec1.getZ());
  }

  // Tests the static add method
  @Test
  public void testAddStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(4, 13, 20);
    Vector3D vec3 = Vector3D.add(vec1, vec2);
    assertEquals(14, vec3.getX());
    assertEquals(15, vec3.getY());
    assertEquals(23, vec3.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(4, -13, -20);
    vec3 = Vector3D.add(vec1, vec2);
    assertEquals(-6, vec3.getX());
    assertEquals(-11, vec3.getY());
    assertEquals(-23, vec3.getZ());
  }

  // Tests the self.sub method
  @Test
  public void testSubSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(4, 13, 20);
    vec1.sub(vec2);
    assertEquals(6, vec1.getX());
    assertEquals(-11, vec1.getY());
    assertEquals(-17, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(4, -13, -20);
    vec1.sub(vec2);
    assertEquals(-14, vec1.getX());
    assertEquals(15, vec1.getY());
    assertEquals(17, vec1.getZ());
  }

  // Tests the static sub method
  @Test
  public void testSubStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(4, 13, 20);
    Vector3D vec3 = Vector3D.sub(vec1, vec2);
    assertEquals(6, vec3.getX());
    assertEquals(-11, vec3.getY());
    assertEquals(-17, vec3.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(4, -13, -20);
    vec3 = Vector3D.sub(vec1, vec2);
    assertEquals(-14, vec3.getX());
    assertEquals(15, vec3.getY());
    assertEquals(17, vec3.getZ());
  }

  // Tests the self.scalarMult method
  @Test
  public void testScalarMultSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1.scalarMult(scalar);
    assertEquals(235.10000000000002, vec1.getX());
    assertEquals(47.02, vec1.getY());
    assertEquals(70.53, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    scalar = -31.11;
    vec1.scalarMult(scalar);
    assertEquals(311.1, vec1.getX());
    assertEquals(-62.22, vec1.getY());
    assertEquals(93.33, vec1.getZ());
  }

  // Tests the static scalarMult method
  @Test
  public void testScalarMultStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1 = Vector3D.scalarMult(vec1, scalar);
    assertEquals(235.10000000000002, vec1.getX());
    assertEquals(47.02, vec1.getY());
    assertEquals(70.53, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    scalar = -31.11;
    vec1 = Vector3D.scalarMult(vec1, scalar);
    assertEquals(311.1, vec1.getX());
    assertEquals(-62.22, vec1.getY());
    assertEquals(93.33, vec1.getZ());
  }

  // Tests the self.scalarDiv method
  @Test
  public void testScalarDivSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1.scalarDiv(scalar);
    assertEquals(10 / scalar, vec1.getX());
    assertEquals(2 / scalar, vec1.getY());
    assertEquals(3 / scalar, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 200, -3);
    scalar = -31.11;
    vec1.scalarDiv(scalar);
    assertEquals(-10 / scalar, vec1.getX());
    assertEquals(200 / scalar, vec1.getY());
    assertEquals(-3 / scalar, vec1.getZ());
  }

  // Tests the static scalarDiv method
  @Test
  public void testScalarDivStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1 = Vector3D.scalarDiv(vec1, scalar);
    assertEquals(10 / scalar, vec1.getX());
    assertEquals(2 / scalar, vec1.getY());
    assertEquals(3 / scalar, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 200, -3);
    scalar = -31.11;
    vec1 = Vector3D.scalarDiv(vec1, scalar);
    assertEquals(-10 / scalar, vec1.getX());
    assertEquals(200 / scalar, vec1.getY());
    assertEquals(-3 / scalar, vec1.getZ());
  }

  // Tests the self.scalarExp method
  @Test
  public void testScalarExpSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1.scalarExp(scalar);
    assertEquals(Math.pow(10.0, scalar), vec1.getX());
    assertEquals(Math.pow(2.0, scalar), vec1.getY());
    assertEquals(Math.pow(3.0, scalar), vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 20, -3);
    scalar = -31.11;
    vec1.scalarExp(scalar);
    assertEquals(Math.pow(-10, scalar), vec1.getX());
    assertEquals(Math.pow(20, scalar), vec1.getY());
    assertEquals(Math.pow(-3, scalar), vec1.getZ());
  }

  // Tests the static scalarExp method
  @Test
  public void testScalarExpStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    double scalar = 23.51;
    vec1 = Vector3D.scalarExp(vec1, scalar);
    assertEquals(Math.pow(10.0, scalar), vec1.getX());
    assertEquals(Math.pow(2.0, scalar), vec1.getY());
    assertEquals(Math.pow(3.0, scalar), vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 20, -3);
    scalar = -31.11;
    vec1 = Vector3D.scalarExp(vec1, scalar);
    assertEquals(Math.pow(-10, scalar), vec1.getX());
    assertEquals(Math.pow(20, scalar), vec1.getY());
    assertEquals(Math.pow(-3, scalar), vec1.getZ());
  }

  // Tests the self.dot method
  @Test
  public void testDotSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    double val = vec1.dot(vec2);
    assertEquals(10 * 18 + 2 * 82 + 3 * 23, val);

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, -23);
    val = vec1.dot(vec2);
    assertEquals(-10 * 18 + 2 * -82 + 3 * 23, val);
  }

  // Tests the static dot method
  @Test
  public void testDotStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    double val = Vector3D.dot(vec1, vec2);
    assertEquals(10 * 18 + 2 * 82 + 3 * 23, val);

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, -23);
    val = Vector3D.dot(vec1, vec2);
    assertEquals(-10 * 18 + 2 * -82 + 3 * 23, val);
  }

  // Tests the self.cross method
  @Test
  public void testCrossSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    vec1.cross(vec2);
    assertEquals(-200, vec1.getX());
    assertEquals(-176, vec1.getY());
    assertEquals(784, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, -23.5);
    vec1.cross(vec2);
    assertEquals(-293, vec1.getX());
    assertEquals(-289, vec1.getY());
    assertEquals(784, vec1.getZ());
  }

  // Tests the static cross method
  @Test
  public void testCrossStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    vec1 = Vector3D.cross(vec1, vec2);
    assertEquals(-200, vec1.getX());
    assertEquals(-176, vec1.getY());
    assertEquals(784, vec1.getZ());

    // Negative values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, -23.5);
    vec1 = Vector3D.cross(vec1, vec2);
    assertEquals(-293, vec1.getX());
    assertEquals(-289, vec1.getY());
    assertEquals(784, vec1.getZ());
  }

  // Test the projection on self method
  @Test
  public void testProjectionSelf() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    vec1.project(vec2);
    assertEquals(0.9811270951563943, vec1.getX());
    assertEquals(33866. / 7577.0, vec1.getY());
    assertEquals(9499.0 / 7577.0, vec1.getZ());

    // Positive values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, 23.5);
    vec1.project(vec2);
    assertEquals(-29844.0 / 30401.0, vec1.getX());
    assertEquals(135956.0 / 30401.0, vec1.getY());
    assertEquals(-38963.0 / 30401.0, vec1.getZ());

  }

  // Test the projection on static method
  @Test
  public void testProjectionStatic() {
    // Positive values
    Vector3D vec1 = new Vector3D(10, 2, 3);
    Vector3D vec2 = new Vector3D(18, 82, 23);
    vec1 = Vector3D.project(vec1, vec2);
    assertEquals(0.9811270951563943, vec1.getX());
    assertEquals(33866. / 7577.0, vec1.getY());
    assertEquals(9499.0 / 7577.0, vec1.getZ());

    // Positive values
    vec1 = new Vector3D(-10, 2, -3);
    vec2 = new Vector3D(18, -82, 23.5);
    vec1 = Vector3D.project(vec1, vec2);
    assertEquals(-29844.0 / 30401.0, vec1.getX());
    assertEquals(135956.0 / 30401.0, vec1.getY());
    assertEquals(-38963.0 / 30401.0, vec1.getZ());

  }

  // Test the rotation with quaternion on self method
  @Test
  public void testQatRotSelf() {
    // Big vector
    Quaternion q = Quaternion.fromAxisAngle(2.1, new Vector3D(1, 2, 3));
    Vector3D v = new Vector3D(412, 64, -214);
    v.qatRot(q);
    assertEquals(v.getX(), -361.9959664210556);
    assertEquals(v.getY(), 280.28058245491195);
    assertEquals(v.getZ(), -100.1883994962561);

    // Small vector
    q = Quaternion.fromAxisAngle(-0.1, new Vector3D(-0.1, 2.3, 31.2));
    v = new Vector3D(0.12, 0.04, -0.314);
    v.qatRot(q);
    assertEquals(v.getX(), 0.12569259165585064);
    assertEquals(v.getY(), 0.02783876288761145);
    assertEquals(v.getZ(), -0.31308525306012575);
  }
  // Test the rotation with static quaternion method
  @Test
  public void testQatRotStatic() {
    // Big vector
    Quaternion q = Quaternion.fromAxisAngle(2.1, new Vector3D(1, 2, 3));
    Vector3D v = new Vector3D(412, 64, -214);
    v = Vector3D.qatRot(v, q);
    assertEquals(v.getX(), -361.9959664210556);
    assertEquals(v.getY(), 280.28058245491195);
    assertEquals(v.getZ(), -100.1883994962561);

    // Small vector
    q = Quaternion.fromAxisAngle(-0.1, new Vector3D(-0.1, 2.3, 31.2));
    v = new Vector3D(0.12, 0.04, -0.314);
    v = Vector3D.qatRot(v, q);
    assertEquals(v.getX(), 0.12569259165585064);
    assertEquals(v.getY(), 0.02783876288761145);
    assertEquals(v.getZ(), -0.31308525306012575);
  }
  // Tests the angle separation method on self
  @Test
  public void testSeparationAngleSelf() {
    // Big vector
    Vector3D v = new Vector3D(412, 64, -214);
    Vector3D v2 = new Vector3D(0.64, -24, 37);
    double angle = v.separationAngle(v2);
    assertEquals(angle, 2.0315284146478922);
  }
}
