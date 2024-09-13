package lib;

public class Vector3D {

    double[] position = new double[3];

    /**
     * Creates a 3D vector given 3 coordinates
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
     * Creates a 3D vector initialized to 0 for all 3 coordinates
     */
    public Vector3D() {
        position[0] = 0;
        position[1] = 0;
        position[2] = 0;
    }



}
