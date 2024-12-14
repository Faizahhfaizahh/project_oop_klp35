import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public Login() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        //setResizable(false);

        // Main panel with background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("assets/bg2.jpeg"); // Path ke gambar background
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Transparent form panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false); // Membuat panel transparan
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 150, 50, 150)); // Padding

        JLabel usernameLabel = createStyledLabel("Username");
        usernameField = new JTextField();
        styleTextField(usernameField);

        JLabel passwordLabel = createStyledLabel("Password");
        passwordField = new JPasswordField();
        styleTextField(passwordField);

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Spacer
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);

        // Add panels to main panel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(Color.BLACK); // Agar teks terlihat di background gambar
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        field.setMaximumSize(new Dimension(400, 30));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(new Color(50, 150, 250));
        button.setForeground(Color.WHITE);
        button.setFocusable(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));

        button.setBorder(BorderFactory.createEmptyBorder());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(30, 130, 230));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(50, 150, 250));
            }
        });

        return button;
    }

    private void handleLogin() {
        String inputUsername = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        if (inputUsername.isEmpty() || inputPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        try (Connection conn = KoneksiDatabase.getConnection()) {
            String username = authenticateAndFetchUsername(conn, inputUsername, inputPassword);
            if (username != null) {
                showSuccess("Login successful!");
                dispose(); // Close the login frame
                SwingUtilities.invokeLater(() -> new Puzzle().openMenu(username));
            } else {
                showError("Invalid username or password");
            }
        } catch (SQLException e) {
            showError("Database error: " + e.getMessage());
        }
    }

    private void handleRegister() {
        dispose();
        new Registrasi().setVisible(true);
    }

    private String authenticateAndFetchUsername(Connection conn, String inputUsername, String inputPassword) throws SQLException {
        String sql = "SELECT username FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, inputUsername);
            pstmt.setString(2, inputPassword);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("username"); // Return the username from the database
                }
            }
        }
        return null; // Return null if no match found
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
