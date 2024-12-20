package graphInterface.core;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.UIManager;

public class Window {
  private int frameWidth = 1200;
  private int frameHeight = 700;

  private JFrame frame;

  /**
   * Launch the application
   */
  public static void main(String[] args) {
    // System.setProperty("sun.java2d.opengl", "true"); // Solves stuttering issues
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Window window = new Window();
          window.frame.setVisible(true);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application
   */
  public Window() {
    initialize();
  }

  /**
   * Initialize the content of the frame
   */
  private void initialize() {
    // Make every platform look the same
    try {
      UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
    } catch (Exception e) {
      e.printStackTrace();
    }
    frame = new JFrame();
    // Set windowed size
    frame.setBounds(100, 100, frameWidth, frameHeight);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    // Fetches border widths and heights and remove them from the panel dimensions
    int panelWidth = frameWidth - (frame.getInsets().left - frame.getInsets().right);
    int panelHeight = frameHeight - (frame.getInsets().top - frame.getInsets().bottom);

    MainPanel mainPanel = new MainPanel(new Rectangle(panelWidth, panelHeight));
    frame.setContentPane(mainPanel);
    frame.pack(); // Resizes the frame to match the size of the mainPanel
    // Makes it windowed fullscreen
    frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
  }
}
