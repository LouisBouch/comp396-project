package environment.habitablity;

public enum Gas {
    Earthlike(1.225, 1005, 0.3, 0.02896),

    Marslike(0.02, 742.6, 0.25, 0.0433),

    Venuslike(65, 1160, 0.75, 0.044),

    Mercurylike(0, 0, 0.9, 0.1),// TODO: FIx that thing

    Vacuum(0, 0, 0.21, 0.026),// TODO: Vacuum

    Titanlike(1.5, 1040, 0.21, 0.026);


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
