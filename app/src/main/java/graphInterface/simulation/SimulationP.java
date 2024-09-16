package graphInterface.simulation;

import environment.SolarSystem;
import lib.Vector3D;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SimulationP extends JPanel implements Runnable {
  private boolean running = false;
  // Time between physics iterations
  private int sleepTime = 20;
  // Number of physics iteration before a repaint
  private int nbItBeforeRepaint = 1;

  private static final long serialVersionUID = 4223433857831514467L;

  private SolarSystem solarSystem;

  private double pixelPerMeter;

  /**
   * Position of the observer in meters
   */
  private Vector3D cameraPosM;

  /**
   * Create the panel.
   */
  public SimulationP() {
    //this.setPreferredSize(new Dimension((int) (dimensions.getWidth()), (int) (dimensions.getHeight())));
    solarSystem = new SolarSystem();

    this.setBackground(Color.BLACK);
  }


  public void step() {
  }

  public void start() {
    if (!running) {
      running = true;
      new Thread(this).start();
    }
  }

  /**
   * Handles the stopping of the simulation
   */
  public void stop(){
    if (running) {
      running = false;
    }
  }

  /**
   * TODO: Resets simulation
   */
  public void reset(){

  }

  /**
   * Handles the stepping forward of the simulation
   */
  @Override
  public void run() {
    int iteration = 0;
    while (running) {
      step();
      if (iteration == 0){
        SwingUtilities.invokeLater(this::repaint);
      }
      iteration = (iteration + 1) % nbItBeforeRepaint;

      // Adds delay between iterations
      try {
        Thread.sleep(sleepTime);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Save the original transform
    AffineTransform originalTransform = g2d.getTransform();
    // Scale by 0.1 to reduce the size to 10%
    g2d.scale(pixelPerMeter, pixelPerMeter);

    solarSystem.paintThis(g2d);

    // Restore the original transform
    g2d.setTransform(originalTransform);
  }
}
