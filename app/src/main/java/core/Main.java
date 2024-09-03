package core;

import java.awt.EventQueue;
import java.awt.Rectangle;

import javax.swing.JFrame;


public class Main {
  private int frameWidth = 800;
  private int frameHeight = 500;

  private JFrame frame;

  /**
   * Launch the application
   */
  public static void main(String[] args) {
    EventQueue.invokeLater(new Runnable() {
      public void run() {
        try {
          Main window = new Main();
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
  public Main() {
    initialize();
  }

  /**
   * Initialize the content of the frame
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, frameWidth, frameHeight);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);

    // Fetches border widths and heights and remove them from the panel dimensions
    int panelWidth = frameWidth - (frame.getInsets().left - frame.getInsets().right);
    int panelHeight = frameHeight - (frame.getInsets().top - frame.getInsets().bottom);

    More mainPanel = new More(new Rectangle(panelWidth, panelHeight));
    frame.setContentPane(mainPanel);
    frame.pack(); // Resizes the frame to match the size of the mainPanel

  }

}
