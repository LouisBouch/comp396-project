package environment.habitablity;

public enum Gas {

    /**
     * Default gas composition templates using the body's average atmospheric
     * density, heat capacity, albedo, and molar mass
     */
    Earthlike(1.225, 1005, 0.3, 0.02896),

    Marslike(0.02, 742.6, 0.25, 0.0433),

    Venuslike(65, 1160, 0.75, 0.044),

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
