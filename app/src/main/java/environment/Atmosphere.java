package environment;

import lib.Vector3D;

import java.util.ArrayList;

public class Atmosphere {
    private double pressure = 0;
    private double temperature = 0;

    private double albedo = 0;

    private Gas gas;

    public Atmosphere(double pressure, double temperature) {
        this.pressure = pressure;
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public Atmosphere habitability(RockyPlanet planet, ArrayList<Body> suns) {

        double radius = planet.getRadius();
        Vector3D position = planet.getPos();

        double atm_thick = getAtmThickness(radius);
        double atm_volume = ((4/3.0)*Math.PI*Math.pow((radius + atm_thick), 3)) - ((4/3.0)*Math.PI*Math.pow(radius, 3));
        double atm_mass = gas.density * atm_volume;
        double atm_cross_sec_area = Math.PI*Math.pow((radius + atm_thick), 2);




        return new Atmosphere(pressure, temperature);
    }

    private static double getAtmThickness(double planetRadius){
        double atmThick = 0.02 * planetRadius; // 2% of planet radius

        return atmThick;
    }
}
