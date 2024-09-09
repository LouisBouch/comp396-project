package graphInterface.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class SimulationP extends JPanel implements Runnable {
  private boolean running = false;
  // Time between physics iterations
  private int sleepTime = 20;
  // Number of physics iteration before a repaint
  private int nbItBeforeRepaint = 1;

  private static final long serialVersionUID = 4223433857831514467L;

  /**
   * Create the panel.
   */
  public SimulationP(Rectangle dimensions) {
    this.setPreferredSize(new Dimension((int) (dimensions.getWidth()), (int) (dimensions.getHeight())));

    this.setBackground(Color.BLACK);
    // For testing purposes only. Remove as soon as possible
    start();
  }

  // TODO the following 2 functions should belong in the environment class. This
  // is temporary
  private int x = 0;
  public void step() {
    x = (x + 1) % 200;
  }

  public void start() {
    if (!running) {
      running = true;
      new Thread(this).start();
    }
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
    // TODO this is just an example. Remove it later
    g2d.setColor(Color.YELLOW);
    g2d.fillOval(x, 10, 10, 10);
  }
}
