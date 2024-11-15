package environment.habitablity;

import environment.Texture;

public enum StarType {
    O(35000, Texture.Sun),
    B(20000, Texture.Sun),
    A(8000, Texture.Sun),
    F(7000, Texture.Sun),
    G(5500, Texture.Sun),
    K(4000, Texture.Sun),
    M(3000, Texture.Sun);

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