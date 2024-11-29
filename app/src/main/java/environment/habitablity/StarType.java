package environment.habitablity;

import environment.Texture;

public enum StarType {
    O(35000, Texture.O),
    B(20000, Texture.B),
    A(8000, Texture.A),
    F(7000, Texture.F),
    G(5500, Texture.G),
    K(4000, Texture.K),
    M(3000, Texture.M);

    private double temperature;

    private Texture texture;

    StarType(double temp, Texture text) {
        this.temperature = temp;
        this.texture = text;
    }

    /**
     * Getter for texture of star
     * @return texture
     */
    public Texture getTexture(){return this.texture;}

    /**
     * Getter for temperature of star
     * @return temperature
     */
    public double getTemperature(){return this.temperature;}

}