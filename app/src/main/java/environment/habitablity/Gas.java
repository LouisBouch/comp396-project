package environment.habitablity;

public enum Gas {
    Earthlike(1.225, 1005, 0.3, 0.02896),

    Marslike(0.02, 742.6, 0.25, 0.0433);

    public double density;

    public double heat_capacity;

    public double albedo;

    public double molar_mass;

    Gas(double density, double heat_capacity, double albedo, double molar_mass) {
        this.density = density;
        this.heat_capacity = heat_capacity;
        this.albedo = albedo;
        this.molar_mass = molar_mass;
    }

}