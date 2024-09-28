package graphInterface.simulation;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import environment.Camera3D;
import environment.SolarSystem;
import lib.Vector3D;
import lib.keyBinds;

public class SimulationP extends JPanel implements Runnable {
  private boolean running = false;
  // Time between physics iterations
  private int sleepTime = 20;
  // Number of physics iteration before a repaint
  private int nbItBeforeRepaint = 1;

  private static final long serialVersionUID = 4223433857831514467L;

  private SolarSystem solarSystem;

  private double basePixelPerMeter = 1115 / 4.5e11;
  private double pixelPerMeter = basePixelPerMeter;
  // Positive means zoom in, negative means zoom out
  private double nbZooms = 0;
  // Each level of nbZooms multiplies pixelPerMeter by this value
  private double zoomValue = 1.5;
  private Vector3D translationM = new Vector3D();
  /**
   * Position of the observer in meters
   */
  private Camera3D camera;
  private Vector3D lastMouseClickPosM = new Vector3D();

  private boolean orthoView = true;

  // TODO: Testing values, remove or concretize
  private Robot r;
  private double totalDist = 0;
  private int x = 0;
  private int y = 0;
  private Vector3D lastMouseClickPosP = new Vector3D();

  /**
   * Create the panel.
   */
  public SimulationP() {
    solarSystem = new SolarSystem();
    camera = new Camera3D(new Vector3D(0, 0, -10.96340e8), solarSystem, 90, 1);

    try {
      r = new Robot();
    } catch (AWTException e) {
      e.printStackTrace();
    }

    // Sets background color
    this.setBackground(Color.BLACK);

    // Sets up MouseWheelListener
    setupMouseWheelListerner();
    // Sets up MouseMotionListener
    setupMouseMovListener();
    // Sets up MouseListener
    setupMouseListerner();
    // Sets up keybindings
    setupKeyBindings();
  }

  /**
   * Steps the simulation
   */
  public void step() {
  }

  /**
   * Starts the simulation
   */
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
    x = (int) this.getLocationOnScreen().getX();
    y = (int) this.getLocationOnScreen().getY();
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Save the original transform
    AffineTransform originalTransform = g2d.getTransform();

    // Orthonormal / perspective paint
    if (orthoView)
      orthoPaint(g2d);
    else
      cameraPaint(g2d);
    // Restore the original transform
    g2d.setTransform(originalTransform);

    // Paints point at center of universe
    // g2d.setColor(Color.WHITE);
    // g2d.translate(translationM.getX() * pixelPerMeter, translationM.getY() *
    // pixelPerMeter);
    // g2d.fillRect(0, 0, 1, 1);
  }

  /**
   * Prints the solar system using a top-down projection
   *
   * @param g2d graphics component
   */
  public void orthoPaint(Graphics2D g2d) {
    // THE FOLLOWING TRANSFORMATIONS ARE EXECTUED IN THE REVERSE ORDER THAT THEY
    // APPEAR
    // translate -> scale

    // Scale by necessary factor because bodies use their coordinates in meters
    g2d.scale(pixelPerMeter, pixelPerMeter);

    // Translate by necesary amount in meters
    g2d.translate(translationM.getX(), translationM.getY());

    // Paint solar system
    solarSystem.paintThis(g2d);
  }

  /**
   * Prints the solar system using a camera
   *
   * @param g2d graphics component
   */
  public void cameraPaint(Graphics2D g2d) {
    camera.setScreenWidth(this.getWidth());
    camera.setScreenHeight(this.getHeight());
    camera.paintThis(g2d);
  }

  /**
   * Sets up mouse movement listener
   */
  public void setupMouseMovListener() {
    // Adds listener for mouse drag
    this.addMouseMotionListener(new MouseMotionListener() {
      @Override
      public void mouseDragged(MouseEvent e) {
        translationM.setX(translationM.getX() + (e.getX() / pixelPerMeter - lastMouseClickPosM.getX()));
        translationM.setY(translationM.getY() + (e.getY() / pixelPerMeter - lastMouseClickPosM.getY()));
        lastMouseClickPosM.setX(e.getX() / pixelPerMeter);
        lastMouseClickPosM.setY(e.getY() / pixelPerMeter);

        // Only move mouse when far away from initial position?
        // Cause rn, moving right may not change the pixel pos, but moving left will.
        // This causes innacuracies when computing total distance travelled.
        totalDist += (e.getX() - lastMouseClickPosP.getX());
        System.out.println(totalDist);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
      }
    });
  }

  /**
   * Sets up mouse click listeners
   */
  public void setupMouseListerner() {
    JComponent panel = this;
    // Adds listener for mouse click
    this.addMouseListener(new MouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        lastMouseClickPosM.setX(e.getX() / pixelPerMeter);
        lastMouseClickPosM.setY(e.getY() / pixelPerMeter);
        lastMouseClickPosP.setX(e.getX());
        lastMouseClickPosP.setY(e.getY());
      }

      @Override
      public void mouseClicked(MouseEvent e) {
      }

      @Override
      public void mouseEntered(MouseEvent e) {
      }

      // Pulls mouse back to the center when it exits the screen
      @Override
      public void mouseExited(MouseEvent e) {
        lastMouseClickPosM.setX(panel.getWidth() / 2 / pixelPerMeter);
        lastMouseClickPosM.setY(panel.getHeight() / 2 / pixelPerMeter);
        lastMouseClickPosP.setX(panel.getWidth() / 2);
        lastMouseClickPosP.setY(panel.getHeight() / 2);
        r.mouseMove((int) panel.getLocationOnScreen().getX() + panel.getWidth() / 2,
            (int) panel.getLocationOnScreen().getY() + panel.getHeight() / 2);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
      }

    });
  }

  /**
   * Sets up the mouse wheel listener
   */
  public void setupMouseWheelListerner() {
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
  }

  /**
   * Sets up the keybindings
   */
  public void setupKeyBindings() {
    keyBinds.addKeyBindingPressed(this, KeyEvent.VK_W, 0, "Move Forward",
        evt -> System.out.println("you are moving forward"));

    keyBinds.addKeyBindingPressed(this, KeyEvent.VK_A, 0, "Move Left", new AbstractAction() {

      @Override
      public void actionPerformed(ActionEvent e) {
        System.out.println("oi");
      }
    });
  }
}
