package environment.habitablity;

public enum Gas {

    /**
     * Default gas composition templates using the body's average atmospheric
     * density, heat capacity, albedo, and molar mass
     *
     * Note on Venuslike atmospheres:
     * Venus is given a staggering albedo of -15 (which is decidedly NOT between 0 and 1) for the following reasons-
     * Venus has exceptionally strong greenhouse gases which are a dominant force in the planet's temperature equilibrium
     * Greenhouse gases aren't taken into account in this simulation (as they are only have such a strong effect on Venus)
     * So, to compensate, Venuslike atmospheres are assumed to absorb 1500% of a star's energy to model the immense amount
     * of heat that continuously  is trapped within it (instead of trapping the energy, it's assumed to continuously
     * receive the corresponding amount of energy at each time step)
     *
     * The correct albedo of Venus is 0.7, however this approach balances our simplified model
     * nicely so that the temperature of Venus in default the Solar System reaches equilibrium at ~737 K (a realistic value)
     */
    Earthlike(1.225, 1005, 0.3, 0.02896),

    Marslike(0.02, 742.6, 0.25, 0.0433),

    Venuslike(65, 1160, -15, 0.044),

    Vacuum(0, 0, 0.2, 0),

    Titanlike(1.5, 1040, 0.21, 0.026);


    public double density; // kg * m^(-3)

    public double heat_capacity; // J * K^(-1)

    public double albedo; // unitless, between 0 and 1

    public double molar_mass; // kg * mol^(-1)


    /**
     * Constructor for an atmosphere's gas composition
     * @param density in kilgrams * meter^(-3)
     * @param heat_capacity in Joules * Kelvin^(-1)
     * @param albedo between 0 and 1
     * @param molar_mass in kilogram * mole^(-1)
     */
    Gas(double density, double heat_capacity, double albedo, double molar_mass) {
        this.density = density;
        this.heat_capacity = heat_capacity;
        this.albedo = albedo;
        this.molar_mass = molar_mass;
    }

}
