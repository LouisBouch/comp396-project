package environment;

import environment.habitablity.Gas;
import environment.habitablity.Atmosphere;
import lib.Vector3D;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;

import static lib.Vector3D.sub;

/**
 * RockyPlanet
 */
public class RockyPlanet extends Body {

  Atmosphere atmosphere;
  boolean habitable = false;
  public RockyPlanet(double radius, double mass, Vector3D position, Vector3D velocity, Texture texture, String bodyName, Gas gas, double initP, double initT) {
    super(radius, mass, position, velocity, texture, bodyName);
    setColor(Color.blue);
    atmosphere = new Atmosphere(initP, initT, gas);
  }
  /**
   * Copy constructor
   */
  public RockyPlanet(RockyPlanet planet) {
    super(planet);
    setColor(Color.blue);

    atmosphere = new Atmosphere(0, 0, planet.getAtm().getGas());
  }
  /**
   * Creates a copy of the planet
   */
  public RockyPlanet copy() {
    return new RockyPlanet(this);
  }

  /**
   * TODO: getTemp() for rocky planets
   */
  public double getTemp(){
    return 0;
  }

  public Atmosphere getAtm(){return atmosphere;}


  public boolean getHab(){return habitable;}
  public void setHab(boolean hab){this.habitable = hab;}

  public void update_habitability(ArrayList<Star> suns, double dt) {

    Gas gas = atmosphere.getGas();
    double temperature = atmosphere.getTemperature();
    double pressure = atmosphere.getPressure();

    double radius = getRadius();
    Vector3D position = getPos();

    double atm_thick = atmosphere.getAtmThickness(radius);
    double atm_volume = ((4/3.0)*Math.PI*Math.pow((radius + atm_thick), 3)) - ((4/3.0)*Math.PI*Math.pow(radius, 3));
    double atm_mass = gas.density * atm_volume;
    double atm_cross_sec_area = Math.PI*Math.pow((radius + atm_thick), 2);
    double atm_moles = atm_mass/gas.molar_mass;

    double total_power = 0;
    double total_energy = 0;

    for (Body sun : suns){
      double distance_sun = sub(position, sun.getPos()).len();
      total_power += powerFromSun(sun.getRadius(), sun.getTemp(), atm_cross_sec_area, distance_sun);
      total_energy = total_power*(1-gas.albedo)*dt;
    }

    double energy_loss = Atmosphere.BOLTZ * Math.pow(temperature, 4)*dt;

    double net_energy = total_energy - energy_loss;

    double delta_temp = net_energy / (atm_mass * gas.heat_capacity);

    double slope = (temperature + delta_temp)/temperature;

    temperature = temperature + delta_temp;

    if (temperature < 0){
      temperature = 0;
    }

    //double pressure = (atm_moles*Atmosphere.R*temperature)/atm_volume;

    pressure = pressure * slope;

    atmosphere.setPressure(pressure);
    atmosphere.setTemperature(temperature);

    if(temperature > 273.15 + -0.0001*(pressure-101325) && Math.log(pressure) > 23.196-(3816/temperature)){
      this.habitable = true;
      }
    else{
      this.habitable = false;
    }
  }

  public double powerFromSun(double rad_star, double temp_star, double area_planet, double distance){
    double luminosity_star = 4 * Math.PI * Math.pow(rad_star, 2) * Atmosphere.BOLTZ * Math.pow(temp_star, 4);
    double intensity_star = luminosity_star/(4 * Math.PI * Math.pow(distance, 2));
    double power_star = intensity_star * area_planet;

    return power_star;

  }

  @Override
  public void paintThis(Graphics2D g2d) {
    g2d.setColor(getColor());
    Ellipse2D.Double shape = new Ellipse2D.Double(this.getX() - this.getRadius(), this.getY() - this.getRadius(),
        this.getRadius() * 2 * this.getScale(), this.getRadius() * 2 * this.getScale());
    g2d.fill(shape);
  }
}