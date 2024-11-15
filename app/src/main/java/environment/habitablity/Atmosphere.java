package environment.habitablity;

import environment.Body;
import environment.RockyPlanet;
import lib.Vector3D;

import java.util.ArrayList;

import static lib.Vector3D.sub;

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

        double total_power = 0;

        for (Body sun : suns){
            double distance_sun = sub(planet.getPos(), sun.getPos()).len();
            total_power += powerFromSun(sun.getRadius(), sun.getTemp(), atm_cross_sec_area, distance_sun);
        }


        return new Atmosphere(pressure, temperature);
    }

    private static double getAtmThickness(double planetRadius){
        double atmThick = 0.02 * planetRadius; // 2% of planet radius
        return atmThick;
    }

    private static double powerFromSun(double rad_star, double temp_star, double area_planet, double distance){
        double boltz = 5.67e-8;

        double luminosity_star = 4 * Math.PI * Math.pow(rad_star, 2) * boltz * Math.pow(temp_star, 4);
        double intensity_star = luminosity_star/(4 * Math.PI * Math.pow(distance, 2));
        double power_star = intensity_star * area_planet;

        return power_star;

    }
}
