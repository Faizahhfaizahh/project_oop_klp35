import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Menu extends JFrame {
    private Image backgroundImage;
    public Menu() {
        setTitle("MENU");
        setSize(815, 670);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setResizable(false);
        setLayout(new BorderLayout());

        backgroundImage = Toolkit.getDefaultToolkit().getImage("assets/bg.jpg");
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(30, 20, 10, 20));
        JLabel titleLabel = new JLabel("SLIDING NUMBER", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Bebas Neue", Font.BOLD, 25));
        titleLabel.setForeground(new Color(255, 255, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.add(Box.createRigidArea(new Dimension(0,130)));
        buttonPanel.setOpaque(false);

        JButton play = createButtonMenu("PLAY", 175, 70);
        JButton exit = createButtonMenu("EXIT", 160, 70);

        ActionListener ActionMenu = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == play) {
                JFrame frame = new JFrame();
                GUISlidingNumber puzzle = new GUISlidingNumber(600, 30);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.add(puzzle);
                frame.setTitle("Sliding Number");
                frame.setResizable(false);
                // frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                frame.pack();
                } else if (e.getSource() == exit) {
                    System.exit(0);
                }
            }
        };

        play.addActionListener(ActionMenu);
        exit.addActionListener(ActionMenu);

        hoverButton(play, Color.BLUE, Color.WHITE);
        hoverButton(exit, Color.RED, Color.WHITE);
        
        buttonPanel.add(play);
        buttonPanel.add(exit);

        backgroundPanel.add(titleLabel, BorderLayout.NORTH);
        backgroundPanel.add(buttonPanel, BorderLayout.CENTER);
        add(backgroundPanel, BorderLayout.CENTER);
}
        

    public JButton createButtonMenu(String text, int widht, int height) {
        JButton playerButton = new JButton();
               
        playerButton.setBorder(BorderFactory.createEmptyBorder());
        playerButton.setLayout(new BorderLayout());
        playerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playerButton.setMaximumSize(new Dimension(widht, height));
        playerButton.setPreferredSize(new Dimension(widht, height));
        playerButton.setContentAreaFilled(false);

        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Bebas Neue", Font.BOLD, 60));
        label.setForeground(Color.WHITE);
        
        playerButton.add(label, BorderLayout.CENTER);
        return playerButton;
    }

    private void hoverButton(JButton button, Color hoverColor, Color defaultColor) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.getComponent(0).setForeground(hoverColor);
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.getComponent(0).setForeground(defaultColor);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu homePage = new Menu();
            homePage.setVisible(true);
        });
    }
}