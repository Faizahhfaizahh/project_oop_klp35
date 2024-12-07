import javax.swing.SwingUtilities;

public class Puzzle {

  public void startGame() {
    UI uiSliding = new UI(600, 30); 
    SwingUtilities.invokeLater(uiSliding);
    
}
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> new Login().setVisible(true));
  }
}