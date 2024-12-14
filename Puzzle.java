// import javax.swing.SwingUtilities;

// public class Puzzle {

//   public void startGame() {
//     UI uiSliding = new UI(600, 30); 
//     SwingUtilities.invokeLater(uiSliding);
    
// }
//   public static void main(String[] args) {
//     SwingUtilities.invokeLater(() -> new Login().setVisible(true));
//   }
// }

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.*;

public class Puzzle {
    // Reference to the current active frame
    private static JFrame currentFrame;

    // Method to open Login screen
    public void openLogin() {
        SwingUtilities.invokeLater(() -> {
            closeCurrentFrame();
            currentFrame = new Login();
            currentFrame.setVisible(true);
        });
    }

    // Method to open Registration screen
    public void openRegistration() {
        SwingUtilities.invokeLater(() -> {
            closeCurrentFrame();
            currentFrame = new Registrasi();
            currentFrame.setVisible(true);
        });
    }

    // Method to open Menu screen
    public void openMenu() {
        SwingUtilities.invokeLater(() -> {
            closeCurrentFrame();
            currentFrame = new Menu();
            currentFrame.setVisible(true);
        });
    }

    // Method to open Sliding Number Game
    public void openSlidingNumberGame() {
        SwingUtilities.invokeLater(() -> {
            closeCurrentFrame();
            JFrame frame = new JFrame();
            GUISlidingNumber puzzle = new GUISlidingNumber(600, 30);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle("Sliding Number");
            frame.add(puzzle);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
            currentFrame = frame;
        });
    }

    // Method to close the current frame
    private void closeCurrentFrame() {
        if (currentFrame != null) {
            currentFrame.dispose();
        }
    }

    // Main method - single entry point for the entire application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Puzzle puzzle = new Puzzle();
            puzzle.openLogin(); // Start with the login screen
        });
    }
}