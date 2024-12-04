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
        setSize(1400, 900);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with background (optional)
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("assets/bg.jpg");
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Form panel
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username with black text
        JLabel usernameLabel = createStyledLabel("Username:");
        usernameField = new JTextField(20);
        styleTextField(usernameField);

        // Password with black text
        JLabel passwordLabel = createStyledLabel("Password:");
        passwordField = new JPasswordField(20);
        styleTextField(passwordField);

        // Add components to form panel
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1;
        formPanel.add(passwordField, gbc);

        // Center the form panel
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerPanel.setOpaque(false);
        centerPanel.add(formPanel);

        // Button panel at bottom
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton loginButton = createStyledButton("Login");
        JButton registerButton = createStyledButton("Register");
        // JButton backButton = createStyledButton("Back to Menu");

        loginButton.addActionListener(e -> handleLogin());
        registerButton.addActionListener(e -> handleRegister());
        // backButton.addActionListener(e -> handleBack());

        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        // buttonPanel.add(backButton);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);

        formPanel.setBorder(BorderFactory.createEmptyBorder(150, 0, 0, 0));
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        return label;
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.BLACK),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        field.setPreferredSize(new Dimension(200, 30));
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(Color.YELLOW);
        button.setForeground(Color.BLACK);
        button.setFocusable(false);
        button.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(255, 255, 150));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.YELLOW);
            }
        });

        return button;
    }

    private void handleLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        try (Connection conn = KoneksiDatabase.getConnection()) {
            if (authenticateUser(conn, username, password)) {
                showSuccess("Login successful!");
                dispose();
                SwingUtilities.invokeLater(() -> new Puzzle().startGame());
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

    // private void handleBack() {
    //     dispose();
    //     // Navigate back to the main menu or previous screen
    //     // You can implement the main menu or home screen here
    // }

    private boolean authenticateUser(Connection conn, String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            return rs.next(); // Return true if a matching user is found
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}
