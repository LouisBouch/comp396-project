package environment;

import java.awt.*;
import java.util.ArrayList;
import lib.Paintable;

/**
 * SolarSystem
 */
public class SolarSystem implements Paintable{

    ArrayList bodies = new ArrayList();
    double timeElapsedSeconds = 0;

    public SolarSystem(){
    }

    public void paintThis(Graphics2D g2d){
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(10, 10, 10, 10);
    }
}


