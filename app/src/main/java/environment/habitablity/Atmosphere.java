package environment.habitablity;

public class Atmosphere {
    public static final double BOLTZ = 5.67e-8;

    public static final double R = 8.31;
    private double pressure = 0;
    private double temperature = 0;
    private Gas gas;

    public Atmosphere(double temperature, Gas gas) {
        this.temperature = temperature;
        this.gas = gas;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }
    public Gas getGas() {
        return gas;
    }

    public void setPressure(double pressure) {this.pressure = pressure;}

    public void setTemperature(double temp) {this.temperature = temp;}


    public double getAtmThickness(double planetRadius){
        double atmThick = 0.02 * planetRadius; // 2% of planet radius
        return atmThick;
    }

}
