package environment;

public enum StarType {
    O(35000, Texture.Sun),
    B(20000, Texture.Sun),
    A(8000, Texture.Sun),
    F(7000, Texture.Sun),
    G(5500, Texture.Sun),
    K(4000, Texture.Sun),
    M(3000, Texture.Sun);

    double temperature;

    Texture texture;

    StarType(double temp, Texture text) {
        this.temperature = temp;
        this.texture = text;
    }

}