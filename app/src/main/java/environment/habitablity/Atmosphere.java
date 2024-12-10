package environment.habitablity;

public class Atmosphere {
    public static final double BOLTZ = 5.67e-8; // Stefan-Boltzmann constant, W * m^(-2) * K^(-4)

    public static final double R = 8.31; // Ideal gas constant, J * mol^(-1) * K^(-1)
    private double pressure = 0; // Pa
    private double temperature = 0; // K
    private Gas gas; // Gas composition

    /**
     * Constructor for an Atmosphere
     * @param temperature in Kelvin
     * @param gas from Gas class
     */
    public Atmosphere(double temperature, Gas gas) {
        this.temperature = temperature;
        this.gas = gas;
    }

    /**
     * Getter for the Atmoshpere's pressure
     * @return pressure
     */
    public double getPressure() {
        return pressure;
    }

    /**
     * Getter for the Atmoshpere's temperature
     * @return temperature
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Getter for the Atmoshpere's gas composition
     * @return gas
     */
    public Gas getGas() {
        return gas;
    }

    /**
     * Setter for the Atmoshpere's pressure
     * @param pressure in Pascals
     */
    public void setPressure(double pressure) {this.pressure = pressure;}

    /**
     * Setter for the Atmoshpere's temperature
     * @param temp in Kelvin
     */
    public void setTemperature(double temp) {this.temperature = temp;}

    /**
     * Getter for the Atmoshpere's thickness, assuming it's 2% of planet's radius
     * @param planetRadius in meters
     * @return atmThick
     */
    public double getAtmThickness(double planetRadius){
        double atmThick = 0.02 * planetRadius; // 2% of planet radius
        return atmThick;
    }

}
