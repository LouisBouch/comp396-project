package environment;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public enum Texture {
  /**
   * Contains texture options for bodies
   */
  Minute("minute.jpg"),
  O("O.jpg"),
  B("B.jpg"),
  A("A.jpg"),
  F("F.jpg"),
  G("G.jpg"),
  K("K.jpg"),
  M("M.jpg"),
  Mercury("Mercury.jpg"),
  Venus("Venus.jpg"),
  Earth("Earth.jpg"),
  Mars("Mars.jpg"),
  Jupiter("Jupiter.jpg"),
  Saturn("Saturn.jpg"),
  Uranus("Uranus.jpg"),
  Neptune("Neptune.jpg"),
  Moon("Moon.jpg"),
  Haumea("Haumea.jpg"),
  Ceres("Ceres.jpg"),
  Eris("Eris.jpg"),
  Pink("Pink.jpg"),
  Crashed("Crashed.jpg");

  private String resName;
  private BufferedImage UVMap;

  /**
   * Constructor for textures
   * @param resName filename of UVMap in resource folder
   */
  Texture(String resName) {
    this.resName = resName;
    loadUVMap();
  }

  /**
   * Getter for UVMap of texture
   * @return buffered image that contains UV map
   */
  public BufferedImage getUVMap() {
    return UVMap;
  }

  /**
   * Load UVMap for texture
   */
  public void loadUVMap() {
    // Get texture
    try {
      BufferedImage texture = ImageIO.read(getClass().getResource("/" + resName));
      UVMap = texture;
    } catch (IOException e) {
      e.printStackTrace();
      UVMap = null;
    }
  }
}
