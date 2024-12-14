import javax.swing.*;
import java.awt.*;

public class UI extends JFrame implements Runnable {
    private JFrame frame;
    private GUISlidingNumber puzzleAngka;
    private int dimension;
    private int margin;

    public UI(int dimension, int margin) {
        this.dimension = dimension;
        this.margin = margin;
    }

    @Override
    public void run() {
        // Inisialisasi frame
        frame = createFrame();
        
        // Tambahkan komponen ke frame
        setupFrameComponents(); 
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JFrame createFrame() {
        JFrame frame = new JFrame("Sliding Number");
        frame.setPreferredSize(new Dimension(dimension + 200, dimension + margin));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(true);
        return frame;
    }

    private void setupFrameComponents() {
        puzzleAngka = new GUISlidingNumber(dimension, margin);

        Container contentPane = frame.getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(puzzleAngka, BorderLayout.CENTER);
    }

    public JFrame getFrame() {
        return frame;
    }

}