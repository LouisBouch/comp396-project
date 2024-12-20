package graphInterface.simulation;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;

import environment.Camera3D;
import environment.SolarSystem;
import environment.Systems;
import graphInterface.settings.CarouselRow;
import graphInterface.settings.LabelRow;
import graphInterface.settings.SettingsManager;
import graphInterface.settings.SliderRow;
import lib.Vector3D;
import lib.keyBinds;

public class SimulationP extends JPanel implements Runnable {
  // Set of keys currently being held down
  private final Set<Integer> heldKeys = new HashSet<>();
  // Define actions for the held down keys
  // private final Map<Integer, Runnable> heldKeyActions =
  // Collections.synchronizedMap(new HashMap<>());
  private final Map<Integer, Runnable> heldKeyActions = new ConcurrentHashMap<>();
  private boolean running = false;
  // Time between physics iterations
  private int sleepTime = 20;
  // Time increment between simulation steps
  private double dt = 1;
  // Number of physics iteration before a repaint
  private int nbItBeforeRepaint = 1;

  private static final long serialVersionUID = 4223433857831514467L;

  private SolarSystem solarSystem;

  private double basePixelPerMeter = 1115 / 4.5e11;
  private double pixelPerMeter = basePixelPerMeter;
  /**
   * Position of the observer in meters
   */
  private Camera3D camera;

  private Thread simThread;

  /**
   * Cursor position and translation information
   */
  private Point2D lastMouseClickPosM = new Point2D.Double();
  private Point lastMouseClickPosP = new Point(-1, -1);
  private boolean captured = false;
  /**
   * Can be used to move the mouse around
   */
  private Robot r;
  JLabel captureLabel;
  JLabel timeLabel;
  JLabel positionInSpaceLabel;
  JLabel orientationInSpaceLabel;
  JButton settingsButton;
  SettingsManager settings;

  /**
   * Create the panel.
   */
  public SimulationP() {
    solarSystem = new SolarSystem(Systems.SolarSystem);
    // solarSystem = new SolarSystem(Systems.SolarSystem);
    camera = new Camera3D(new Vector3D(0, 0, -1.5e10), solarSystem, 90, 1);
    // camera.rotateCamera(new Point(500, 0), false);

    // Create labels and buttons that will be hosted by the SimulationP panel
    makeJComponents();

    // Create robot which will move cursor
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
   * Create and setup the JComponents
   */
  public void makeJComponents() {
    SpringLayout sLayout = new SpringLayout();
    this.setLayout(sLayout);

    // Label that says whether the mouse is captured or not
    captureLabel = new JLabel();
    sLayout.putConstraint(SpringLayout.SOUTH, captureLabel, -5, SpringLayout.SOUTH, this);
    sLayout.putConstraint(SpringLayout.EAST, captureLabel, -5, SpringLayout.EAST, this);
    this.add(captureLabel);
    capturedLabel();

    // Label that contains the position in space of the camera
    positionInSpaceLabel = new JLabel();
    sLayout.putConstraint(SpringLayout.NORTH, positionInSpaceLabel, 5, SpringLayout.NORTH, this);
    sLayout.putConstraint(SpringLayout.WEST, positionInSpaceLabel, 5, SpringLayout.WEST, this);
    this.add(positionInSpaceLabel);
    updatePosLabel();

    // Label that contains the orientation in space of the camera
    orientationInSpaceLabel = new JLabel();
    sLayout.putConstraint(SpringLayout.NORTH, orientationInSpaceLabel, 5, SpringLayout.SOUTH, positionInSpaceLabel);
    sLayout.putConstraint(SpringLayout.WEST, orientationInSpaceLabel, 5, SpringLayout.WEST, this);
    this.add(orientationInSpaceLabel);
    updateOriLabel();

    // Label that contains time of the simulation
    timeLabel = new JLabel();
    sLayout.putConstraint(SpringLayout.SOUTH, timeLabel, -5, SpringLayout.SOUTH, this);
    sLayout.putConstraint(SpringLayout.WEST, timeLabel, 5, SpringLayout.WEST, this);
    this.add(timeLabel);
    updateTimeLabel();

    // Button used to access settings options for the simulation
    settingsButton = new JButton();
    sLayout.putConstraint(SpringLayout.NORTH, settingsButton, 0, SpringLayout.NORTH, this);
    sLayout.putConstraint(SpringLayout.EAST, settingsButton, -0, SpringLayout.EAST, this);
    settingsButton.setOpaque(false);
    settingsButton.setContentAreaFilled(false);
    settingsButton.setBorderPainted(false);
    settingsButton.setText("<html>" +
        "<span style='color:#FFFFFF; font-size: 18; vertical-align: bottom;'>" +
        "⚙" +
        "</span>" + "</html>");
    settingsButton.setFocusable(false);
    settingsButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.add(settingsButton);

    JPanel thisP = this;
    // Settings window
    // Opens settings window
    settingsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        // This is the window frame
        JFrame ownerFrame = (JFrame) SwingUtilities.getWindowAncestor(thisP);
        settings.initializeJDialog(ownerFrame);
        settings.setVisible(true);
      }
    });
    // Initialize settings if not done already
    if (settings == null) {
      // The frame where the settings belong
      settings = new SettingsManager(null, new Dimension(500, 750), "Settings");
      // Add the different settings

      // The camera setting section
      settings.addSetting(new LabelRow("Camera Settings"));

      // Sensitivity setting
      double defaultSentivity = camera.getSensitivity();
      settings.addSetting(
          new SliderRow("Sensitivity", defaultSentivity / 10.0, 4.0 * defaultSentivity, defaultSentivity, (v) -> {
            camera.setSensitivity(v);
          }));

      // Quality setting
      // This list contains the possible quality values for the texture
      ArrayList<Map.Entry<String, Double>> s = new ArrayList<>();
      int defaultQuality = camera.getMaxImSize();
      s.add(new AbstractMap.SimpleEntry<>("Very Low", defaultQuality / 3.0));
      s.add(new AbstractMap.SimpleEntry<>("Low", defaultQuality / 1.5));
      s.add(new AbstractMap.SimpleEntry<>("Medium", (double) defaultQuality));
      s.add(new AbstractMap.SimpleEntry<>("High", defaultQuality * 1.5));
      s.add(new AbstractMap.SimpleEntry<>("Very High", defaultQuality * 3.0));
      s.add(new AbstractMap.SimpleEntry<>("Just don't", Double.MAX_VALUE));
      settings.addSetting(new CarouselRow("Texture Quality", s, 2, (v) -> {
        camera.setMaxImSize(v.intValue());
      }));
    }
    // Load setttings if they exists
    settings.loadSettings();
  }

  /**
   * Steps the simulation
   */
  public void step(double dt) {
    handleKeys();
    solarSystem.step(dt);
  }

  /**
   * Starts the simulation
   */
  public void start() {
    if (!running) {
      running = true;
      simThread = new Thread(this);
      simThread.start();
    }
  }

  /**
   * Handles the stopping of the simulation
   */
  public void stop() {
    running = false;
  }

  /**
   * Resets the simulation
   */
  public void reset() {
    // Stops simulation
    stop();
    try {
      // Joins threads
      if (simThread != null) {
        simThread.join();
      }
      //// If no thread exists, there's nothing to reset
      // else {
      // return;
      // }
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    // Restarts solar system
    solarSystem.reset();
    camera.reset();
    // Create new thread
    start();
  }

  public void resume() {
    running = true;
  }

  /**
   * Handles the stepping forward of the simulation
   */
  @Override
  public void run() {
    int iteration = 0;
    double start = System.nanoTime();
    double end;
    double timeSpent;
    while (running) {
      step(dt);
      if (iteration == 0) {
        SwingUtilities.invokeLater(this::updatePosLabel);
        SwingUtilities.invokeLater(this::updateOriLabel);
        SwingUtilities.invokeLater(this::updateTimeLabel);
        SwingUtilities.invokeLater(this::repaint);
      }
      iteration = (iteration + 1) % nbItBeforeRepaint;

      // Adds delay between iterations
      try {
        end = System.nanoTime();
        // Remove time spent computing from sleepTime
        timeSpent = (end - start) / 1000000.0;
        Thread.sleep(sleepTime - (int) (timeSpent > sleepTime ? 0 : timeSpent));
        start = System.nanoTime();
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

    cameraPaint(g2d);
    // Restore the original transform
    g2d.setTransform(originalTransform);

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
        lastMouseClickPosM.setLocation(e.getX() / pixelPerMeter, e.getY() / pixelPerMeter);
      }

      @Override
      public void mouseMoved(MouseEvent e) {
        if (captured && !(lastMouseClickPosP.getX() == -1 && lastMouseClickPosP.getY() == -1)) {
          Point pixDis = new Point(e.getX() - (int) lastMouseClickPosP.getX(),
              e.getY() - (int) lastMouseClickPosP.getY());
          Boolean roll = (e.getModifiersEx() & InputEvent.SHIFT_DOWN_MASK) != 0;
          camera.rotateCamera(pixDis, roll);
        }
        lastMouseClickPosP.setLocation(e.getX(), e.getY());
      }
    });
  }

  /**
   * Sets up mouse click listeners
   */
  public void setupMouseListerner() {
    // Adds listener for mouse click
    this.addMouseListener(new MouseListener() {
      @Override
      public void mousePressed(MouseEvent e) {
        lastMouseClickPosM.setLocation(e.getX() / pixelPerMeter, e.getY() / pixelPerMeter);
        lastMouseClickPosP.setLocation(e.getX(), e.getY());
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
        if (!captured)
          return;
        JComponent panel = (JComponent) e.getSource();
        lastMouseClickPosM.setLocation(panel.getWidth() / 2.0 / pixelPerMeter, panel.getHeight() / 2.0 / pixelPerMeter);
        lastMouseClickPosP.setLocation(panel.getWidth() / 2, panel.getHeight() / 2);
        r.mouseMove((int) panel.getLocationOnScreen().getX() + panel.getWidth() / 2,
            (int) panel.getLocationOnScreen().getY() + panel.getHeight() / 2);
      }

      @Override
      public void mouseReleased(MouseEvent e) {
        lastMouseClickPosP.setLocation(e.getX(), e.getY());
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
        // Increases boost
        camera.addBoost(-e.getWheelRotation());
      }

    });
  }

  /**
   * Sets up the keybindings and creates the actions that will be run in the step
   * loop for pressed donw keys
   */
  public void setupKeyBindings() {
    // synchronized (heldKeys) {
    // Binding to monitor W
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_W, "Pressed W",
        evt -> heldKeys.add(KeyEvent.VK_W));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_W, "Released W",
        evt -> heldKeys.remove(KeyEvent.VK_W));
    // Binding to monitor S
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_S, "Pressed S",
        evt -> heldKeys.add(KeyEvent.VK_S));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_S, "Released S",
        evt -> heldKeys.remove(KeyEvent.VK_S));
    // Binding to monitor A
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_A, "Pressed A",
        evt -> heldKeys.add(KeyEvent.VK_A));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_A, "Released A",
        evt -> heldKeys.remove(KeyEvent.VK_A));
    // Binding to monitor D
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_D, "Pressed D",
        evt -> heldKeys.add(KeyEvent.VK_D));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_D, "Released D",
        evt -> heldKeys.remove(KeyEvent.VK_D));
    // Binding to monitor CONTROL
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_CONTROL, "Pressed CONTROL",
        evt -> heldKeys.add(KeyEvent.VK_CONTROL));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_CONTROL, "Released CONTROL",
        evt -> heldKeys.remove(KeyEvent.VK_CONTROL));
    // Binding to monitor SPACE
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_SPACE, "Pressed SPACE",
        evt -> heldKeys.add(KeyEvent.VK_SPACE));
    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_SPACE, "Released SPACE",
        evt -> heldKeys.remove(KeyEvent.VK_SPACE));
    // }
    // Toggle mouse capture in simulation
    keyBinds.addKeyBindingPressedNoMod(this, KeyEvent.VK_C, "Toggle mouse capture", new AbstractAction() {
      @Override
      public void actionPerformed(ActionEvent e) {
        captured = !captured;
        if (captured) {
          BufferedImage invIm = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
          invIm.setRGB(0, 0, 0x00000000);
          Cursor invCur = Toolkit.getDefaultToolkit().createCustomCursor(invIm, new Point(0, 0), "invisible");
          ((JComponent) e.getSource()).setCursor(invCur);
        } else {
          ((JComponent) e.getSource()).setCursor(Cursor.getDefaultCursor());
        }
        capturedLabel();
      }
    });

    keyBinds.addKeyBindingReleasedNoMod(this, KeyEvent.VK_R, "Released R",
        evt -> reset());

    // Setup pressed down key actions
    heldKeyActions.put(KeyEvent.VK_W, () -> camera.moveAlongView(Camera3D.FORWARDS));
    heldKeyActions.put(KeyEvent.VK_S, () -> camera.moveAlongView(Camera3D.BACKWARDS));
    heldKeyActions.put(KeyEvent.VK_A, () -> camera.moveSideways(Camera3D.LEFT));
    heldKeyActions.put(KeyEvent.VK_D, () -> camera.moveSideways(Camera3D.RIGHT));
    heldKeyActions.put(KeyEvent.VK_SPACE, () -> camera.moveVertical(Camera3D.UP));
    heldKeyActions.put(KeyEvent.VK_CONTROL, () -> camera.moveVertical(Camera3D.DOWN));
  }

  /**
   * Does actions depending on which keys are pressed
   */
  public void handleKeys() {
    Runnable action;
    // synchronized (heldKeys) {
    for (int key : heldKeys) {
      action = heldKeyActions.get(key);
      if (action == null)
        continue;
      action.run();
    }
    // }
  }

  /**
   * Changes the label depending on the capture status of the mouse
   */
  public void capturedLabel() {
    if (captured) {
      captureLabel.setText("<html>"
          + "<span style='color:#00FF00; font-size: 18; vertical-align: bottom;'>•</span>"
          + "<span style='color:#FFFFFF; font-size: 12; vertical-align: middle;'>Press 'c' to release mouse capture</span>"
          + "</html>");
    } else {
      captureLabel.setText("<html>"
          + "<span style='color:#FF0000; font-size: 18; vertical-align: bottom;'>•</span>"
          + "<span style='color:#FFFFFF; font-size: 12; vertical-align: middle;'>Press 'c' to capture mouse</span>"
          + "</html>");
    }
  }

  /**
   * Shows the current position of the camera in space
   */
  public void updatePosLabel() {
    Vector3D positionInSpaceCamM = camera.getCurPosM();
    BigDecimal bdX = new BigDecimal(positionInSpaceCamM.getX());
    BigDecimal bdY = new BigDecimal(positionInSpaceCamM.getY());
    BigDecimal bdZ = new BigDecimal(positionInSpaceCamM.getZ());
    int sigFigs = 3;
    int scaleX = sigFigs - bdX.precision() + bdX.scale();
    int scaleY = sigFigs - bdY.precision() + bdY.scale();
    int scaleZ = sigFigs - bdZ.precision() + bdZ.scale();
    bdX = bdX.setScale(scaleX, RoundingMode.HALF_UP);
    bdY = bdY.setScale(scaleY, RoundingMode.HALF_UP);
    bdZ = bdZ.setScale(scaleZ, RoundingMode.HALF_UP);
    positionInSpaceLabel.setText("<html>" +
        "<span style='color:#FFFFFF; font-size: 16; vertical-align: bottom;'>" +
        "Position in space: ["
        + bdX + ", " + bdY + " , " + bdZ + "]</span>" + "</html>");
  }

  /**
   * Show the current orientation of the camera in space
   */
  public void updateOriLabel() {
    Vector3D orientationCam = camera.getCurOrientation();
    BigDecimal bdX = new BigDecimal(orientationCam.getX());
    BigDecimal bdY = new BigDecimal(orientationCam.getY());
    BigDecimal bdZ = new BigDecimal(orientationCam.getZ());
    int nbDec = 3;
    bdX = bdX.setScale(nbDec, RoundingMode.HALF_UP);
    bdY = bdY.setScale(nbDec, RoundingMode.HALF_UP);
    bdZ = bdZ.setScale(nbDec, RoundingMode.HALF_UP);
    orientationInSpaceLabel.setText("<html>" +
        "<span style='color:#FFFFFF; font-size: 16; vertical-align: bottom;'>" + "Camera orientation: ["
        + bdX + ", " + bdY + " , " + bdZ + "]</span>" + "</html>");
  }

  /**
   * Show the current time of the simulation
   */
  public void updateTimeLabel() {
    double time = solarSystem.getTime();
    timeLabel.setText("<html>" +
        "<span style='color:#FFFFFF; font-size: 16; vertical-align: bottom;'>" +
        "Elapsed time<br>"
        + formatTime(time) + "</span>" + "</html>");
  }

  /**
   * Formats a time in seconds to the xxx days yyy hours zzz minutes format
   *
   * @param time The time in seconds since the start of the simuation
   *
   * @return The formatted string of time
   */
  public String formatTime(double time) {
    double sIY = 3600 * 24 * 365;
    double sID = 3600 * 24;
    double sIH = 3600;
    double sIM = 60;

    int y = (int) (time / sIY);
    int d = (int) ((time - sIY * y) / sID);
    int h = (int) ((time - sIY * y - sID * d) / sIH);
    int m = (int) ((time - sIY * y - sID * d - sIH * h) / sIM);

    String format = "";
    format += "year: " + y + "<br>";
    format += "day: " + d + "<br>";
    format += "hour: " + h + "<br>";
    format += "minute: " + m + "<br>";
    return format;
  }

  /**
   * Getter for the solar system object
   *
   * @return The solar system of the sim
   */
  public SolarSystem getSolarSystem() {
    return solarSystem;
  }

  /**
   * Obtain the incremenet of time between simulation steps
   *
   * @return dt
   */
  public double getDt() {
    return dt;
  }

  /**
   * Sets the incremenet of time between simulation steps
   *
   * @param dt New time step value
   */
  public void setDt(double dt) {
    this.dt = dt;
  }

  /**
   * Getter for the camera
   *
   * @return The camera that sees the solar system
   */
  public Camera3D getCamera() {
    return camera;
  }
}
