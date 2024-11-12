package environment;

import lib.Vector3D;

import java.util.ArrayList;

public class atmosphere {
    private double pressure = 0;
    private double temperature = 0;

    private double albedo = 0;

    private Gas gas;

    public atmosphere(double pressure, double temperature) {
        this.pressure = pressure;
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public atmosphere habitability(RockyPlanet planet) {



        double radius = planet.getRadius();
        Vector3D position = planet.getPos();
        double atm_thick = getAtmThickness(radius);

        double atm_volume = ((4/3)*Math.PI*Math.pow((radius + atm_thick), 3)) - ((4/3)*Math.PI*Math.pow(radius, 3));

        ArrayList<Body> suns;
        //suns = SolarSystem.getSuns();




        return new atmosphere(pressure, temperature);
    }

    private static double getAtmThickness(double planetRadius){
        double atmThick = 0.02 * planetRadius; // 2% of planet radius

        return atmThick;
    }
}
