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

    public static StarType getStarType(double mass){
        double m_sun = 1.989e30;
        StarType type = null;

        if (mass < 0.45*m_sun){
            return type = M;
        }
        else if (mass < 0.8*m_sun){
            return type = K;
        }
        else if (mass < 1.04*m_sun){
            return type = G;
        }
        else if (mass < 1.4*m_sun){
            return type = F;
        }
        else if (mass < 2.1*m_sun){
            return type = A;
        }
        else if (mass < 16*m_sun){
            return type = B;
        }
        else if (mass >= 16*m_sun){
            return type = O;
        }
        return type;
    }

}