package environment;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Contains all possible textures
 */
public enum Texture {

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

  Texture(String resName) {
    this.resName = resName;
    loadUVMap();
  }

  /*
   * Return buffered image that contains UV map
   */
  public BufferedImage getUVMap() {
    return UVMap;
  }

  public void loadUVMap() {
    // Get texture
    try {
      BufferedImage texture = ImageIO.read(getClass().getResource("/" + resName));
      // BufferedImage textureARGB = new
      // BufferedImage(texture.getWidth(),texture.getHeight(),
      // BufferedImage.TYPE_INT_ARGB);
      // //Convert to transparent image
      // textureARGB.getGraphics().drawImage(texture, 0, 0, null);
      UVMap = texture;
    } catch (IOException e) {
      e.printStackTrace();
      UVMap = null;
    }
  }
}
