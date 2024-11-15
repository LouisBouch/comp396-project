package environment.habitablity;

public enum Gas {
    Earthlike(1.225, 1005);

    double density;

    double heat_capacity;

    Gas(double density, double heat_capacity) {
        this.density = density;
        this.heat_capacity = heat_capacity;
    }

}