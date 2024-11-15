package environment.habitablity;

public enum Gas {
    Earthlike(1225, 1005, 0.3, 28.96);

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