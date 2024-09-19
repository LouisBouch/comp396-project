package graphInterface.simulation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import environment.SolarSystem;
import lib.Vector3D;

public class SimulationP extends JPanel implements Runnable {
  private boolean running = false;
  // Time between physics iterations
  private int sleepTime = 20;
  // Number of physics iteration before a repaint
  private int nbItBeforeRepaint = 1;

  private static final long serialVersionUID = 4223433857831514467L;

  private SolarSystem solarSystem;

  private double basePixelPerMeter = 1 / 10.0;
  private double pixelPerMeter = basePixelPerMeter;
  // Positive means zoom in, negative means zoom out
  private double nbZooms = 0;
  // Each level of nbZooms multiplies pixelPerMeter by this value
  private double zoomValue = 1.5;
  private Vector3D translationM = new Vector3D();
  /**
   * Position of the observer in meters
   */
  private Vector3D cameraPosM;
  private Vector3D lastMouseClickPosM = new Vector3D();

  /**
   * Create the panel.
   */
  public SimulationP() {
    // this.setPreferredSize(new Dimension((int) (dimensions.getWidth()), (int)
    // (dimensions.getHeight())));
    solarSystem = new SolarSystem();

    // Sets background color
    this.setBackground(Color.BLACK);

    // Adds event listener for mousewheel
    this.addMouseWheelListener(new MouseWheelListener() {
      @Override
      public void mouseWheelMoved(MouseWheelEvent e) {
        // If e.get.. is negative then we have scrollup, which is zoom in
        // So we need positive value
        nbZooms += -e.getWheelRotation();
        double zoomMulti = Math.pow(zoomValue, -e.getWheelRotation());
        translationM.setX((e.getX() / pixelPerMeter * (1 - zoomMulti) + zoomMulti * translationM.getX()) / zoomMulti);
        translationM.setY((e.getY() / pixelPerMeter * (1 - zoomMulti) + zoomMulti * translationM.getY()) / zoomMulti);
        pixelPerMeter = basePixelPerMeter * Math.pow(zoomValue, nbZooms);
      }
    });
    // Adds listener for mouse click
    this.addMouseListener(new MouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        lastMouseClickPosM.setX(e.getX() / pixelPerMeter);
        lastMouseClickPosM.setY(e.getY() / pixelPerMeter);
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      @Override
      public void mouseExited(MouseEvent e) {
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

    });
    // Adds listener for mouse drag
    this.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        translationM.setX(translationM.getX() + (e.getX() / pixelPerMeter - lastMouseClickPosM.getX()));
        translationM.setY(translationM.getY() + (e.getY() / pixelPerMeter - lastMouseClickPosM.getY()));
        lastMouseClickPosM.setX(e.getX() / pixelPerMeter);
        lastMouseClickPosM.setY(e.getY() / pixelPerMeter);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
      }
    });
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
  public void stop() {
    if (running) {
      running = false;
    }
  }

  /**
   * TODO: Resets simulation
   */
  public void reset() {

  }

  /**
   * Handles the stepping forward of the simulation
   */
  @Override
  public void run() {
    int iteration = 0;
    while (running) {
      step();
      if (iteration == 0) {
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

  /**
   * Handles the drawing of the solar system and adjusts it according to the
   * scaling factor pixelPerMeter
   *
   * @param g Graphics component
   */
  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Save the original transform
    AffineTransform originalTransform = g2d.getTransform();

    // THE FOLLOWING TRANSFORMATIONS ARE EXECTUED IN THE REVERSE ORDER THAT THEY
    // APPEAR
    // translate -> scale

    // Scale by necessary factor because bodies use their coordinates in meters
    g2d.scale(pixelPerMeter, pixelPerMeter);

    // Translate by necesary amount in meters
    g2d.translate(translationM.getX(), translationM.getY());

    // Paint solar system
    solarSystem.paintThis(g2d);

    // Restore the original transform
    g2d.setTransform(originalTransform);

    // Paints point at center of universe
    g2d.setColor(Color.WHITE);
    g2d.translate(translationM.getX() * pixelPerMeter, translationM.getY() * pixelPerMeter);
    g2d.fillRect(0, 0, 1, 1);
  }
}
