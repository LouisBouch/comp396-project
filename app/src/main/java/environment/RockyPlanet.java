package environment;

import environment.habitablity.Gas;
import environment.habitablity.Atmosphere;
import lib.Vector3D;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import static lib.Vector3D.sub;

public class RockyPlanet extends Body {

  Atmosphere atmosphere; // Planet's atmosphere
  boolean habitable = false; // Planet's habitability
  double initTemp; // Initial temperature of planet's atmosphere

  /**
   * Constructor for rocky planets
   * @param radius in meters
   * @param mass in kilograms
   * @param position as 3D vector in meters
   * @param velocity as 3D vector in meters/second
   * @param texture
   * @param bodyName as string
   * @param gas composition of atmosphere
   * @param initT initial atmospheric temperature in Kelvin
   */
  public RockyPlanet(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName, Gas gas, double initT) {
    super(radius, mass, position, velocity, texture, bodyName);
    atmosphere = new Atmosphere(initT, gas);
    initTemp = initT;
  }

  /**
   * Copy constructor for a rocky planet
   * @param planet to be copied
   */
  public RockyPlanet(RockyPlanet planet) {
    super(planet);
    atmosphere = new Atmosphere(planet.getInitTemp(), planet.getAtm().getGas());
  }
  /**
   * Method to copy rocky planet
   * @return copy of rocky planet
   */
  public RockyPlanet copy() {
    return new RockyPlanet(this);
  }

  /**
   * Getter for a rocky planet's atmosphere
   * @return atmosphere of planet
   */
  public Atmosphere getAtm(){return atmosphere;}


    /**
     * Getter for a rocky planet's initial atmospheric temperature
     * @return initial atmospheric temperature of planet
     */
    public double getInitTemp(){return initTemp;}

  /**
   * Getter for a rocky planet's habitability
   * @return habitability (true/false)
   */
  public boolean getHab(){return habitable;}

  /**
   * Setter for a rocky planet's habitability
   * @param hab boolean
   */
  public void setHab(boolean hab){this.habitable = hab;}

  /**
   * Calculate the power received by a planet from a star (assuming black body radiation)
   * @param rad_star radius of star in meters
   * @param temp_star temperature of star in Kelvin
   * @param area_planet area of atmosphere that is receiving the power in meters^2
   * @param distance distance between star and planet in meters
   *
   * @return power received by planet's surface in Joules/second
   */
  public double powerFromSun(double rad_star, double temp_star, double area_planet, double distance){
    double luminosity_star = 4 * Math.PI * Math.pow(rad_star, 2) * Atmosphere.BOLTZ * Math.pow(temp_star, 4);
    double intensity_star = luminosity_star/(4 * Math.PI * Math.pow(distance, 2));
    double power_star = intensity_star * area_planet;

    return power_star;

  }

  /**
   * Update temperature and pressure of a rocky planet's atmosphere and check its habitability
   *
   * Calculate the total power received by the planet from all the stars,
   * find the energy it absorbs and radiates (assuming black body radiation),
   * and calculate the resulting change in temperature and pressure in the atmoshpere
   *
   * Then, check the pressure and temperature values against the phase diagram of water to evaluate the planet's habitability
   *
   * @param suns List of stars in the system
   * @param dt Time-step of simulation in seconds
   */
  public void update_habitability(ArrayList<Star> suns, double dt) {

      Gas gas = atmosphere.getGas();

      double temperature = atmosphere.getTemperature();

      double radius = getRadius(); // Radius of planet in meters
      Vector3D position = getPos(); // Position of planet as 3D vector in meters

      // Find relevant dimensions of atmosphere
      double atm_thick = atmosphere.getAtmThickness(radius);
      double atm_volume = ((4 / 3.0) * Math.PI * Math.pow((radius + atm_thick), 3)) - ((4 / 3.0) * Math.PI * Math.pow(radius, 3));
      double atm_mass = gas.density * atm_volume;
      double atm_area = 4 * Math.PI * Math.pow(radius + atm_thick, 2);
      double atm_absorbing_area = atm_area / 2; // Half the atmosphere's area is facing the star
      double atm_moles = atm_mass / gas.molar_mass;

      // Calculate energy absorbed from all stars over time step
      double total_power = 0;
      for (Star sun : suns) {
        double distance_sun = sub(position, sun.getPos()).len();
        double powerFromCurrentSun = powerFromSun(sun.getRadius(), sun.getTemp(), atm_absorbing_area, distance_sun);
        total_power += powerFromCurrentSun;
      }
      double total_energy = total_power * (1 - gas.albedo) * dt;

      // Energy by atmosphere lost due to blackbody radiation
      double energy_loss = Atmosphere.BOLTZ * Math.pow(temperature, 4) * dt * atm_area;

      // Net energy and temperature change
      double net_energy = total_energy - energy_loss;
      double delta_temp = net_energy / (atm_mass * gas.heat_capacity);

      // Update temperature (ensuring no negative values) and pressure (ideal gas law)
      temperature = Math.max(temperature + delta_temp, 0);
      double pressure = (atm_moles * Atmosphere.R * temperature) / atm_volume;

      atmosphere.setPressure(pressure);
      atmosphere.setTemperature(temperature);

      // Determine habitability by comparing P & T with phases of water
      if (temperature > 273.15 + -0.0001 * (pressure - 101325) &&
              Math.log(pressure) > 23.196 - (3816 / temperature)) {
        this.habitable = true;
      } else {
        this.habitable = false;
      }

  }

}
