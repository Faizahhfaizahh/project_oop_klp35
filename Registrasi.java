import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Registrasi extends JFrame {
    private JTextField usernameField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public Registrasi(){
        initializeUI();
    }

    

    private void initializeUI() {
        setTitle("Registrasi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setResizable(true);

        // Main panel with a background color
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(235, 235, 235));

        // Create a form panel with a GridBagLayout
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 

        // Set GridBagConstraints for form items
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username Field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField.setPreferredSize(new Dimension(350, 40)); // Ukuran lebih besar
        usernameField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Email Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField = new JTextField(20);
        emailField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailField.setPreferredSize(new Dimension(350, 40)); // Ukuran lebih besar
        emailField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Password Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField.setPreferredSize(new Dimension(350, 40)); // Ukuran lebih besar
        passwordField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Confirm Password Field
        JLabel confirmPasswordLabel = new JLabel("Confirm Password:");
        confirmPasswordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Arial", Font.PLAIN, 14));
        confirmPasswordField.setPreferredSize(new Dimension(350, 40)); // Ukuran lebih besar
        confirmPasswordField.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));

        // Register Button
        JButton registerButton = new JButton("Register");
        registerButton.setFont(new Font("Arial", Font.BOLD, 14));
        registerButton.setBackground(new Color(70, 130, 180));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Back to Login Button
        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Arial", Font.PLAIN, 12));
        backButton.setBackground(new Color(200, 200, 200));
        backButton.setForeground(Color.BLACK);
        backButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        // Register Button Action
        registerButton.addActionListener(this::handleRegistration);

        // Back Button Action
        backButton.addActionListener(e -> {
            dispose(); // Close registration form
            new Login().setVisible(true); // Open login form
        });

        // Add components to formPanel using GridBagConstraints
        gbc.gridx = 0; gbc.gridy = 0; formPanel.add(usernameLabel, gbc);
        gbc.gridx = 1; formPanel.add(usernameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; formPanel.add(emailLabel, gbc);
        gbc.gridx = 1; formPanel.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; formPanel.add(passwordLabel, gbc);
        gbc.gridx = 1; formPanel.add(passwordField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; formPanel.add(confirmPasswordLabel, gbc);
        gbc.gridx = 1; formPanel.add(confirmPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        formPanel.add(registerButton, gbc);

        gbc.gridy = 5; formPanel.add(backButton, gbc);

        // Add formPanel to the mainPanel
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private void handleRegistration(ActionEvent e) {
        String username = usernameField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        try (Connection conn = KoneksiDatabase.getConnection()) {
            if (isUsernameTaken(conn, username)) {
                showError("Username already taken");
                return;
            }

            if (isEmailTaken(conn, email)) {
                showError("Email already registered");
                return;
            }

            if (registerUser(conn, username, email, password)) {
                showSuccess("Registration successful! Please login.");
                dispose();
                new Login().setVisible(true);
            } else {
                showError("Registration failed");
            }
        } catch (Exception ex) {
            showError("Database error: " + ex.getMessage());
        }
    }

    private boolean isUsernameTaken(Connection conn, String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            showError("Error checking username: " + e.getMessage());
            return true;
        }
    }

    private boolean isEmailTaken(Connection conn, String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            showError("Error checking email: " + e.getMessage());
            return true;
        }
    }

    private boolean registerUser(Connection conn, String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            showError("Error registering user: " + e.getMessage());
            return false;
        }
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
