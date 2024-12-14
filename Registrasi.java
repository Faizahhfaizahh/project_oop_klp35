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

    public Registrasi() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Registrasi");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        // Main panel with a background image
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon background = new ImageIcon("assets/bg2.png"); // Ganti path sesuai lokasi gambar
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        mainPanel.setLayout(new BorderLayout());

        // Transparent form panel
        JPanel formPanel = new JPanel();
        formPanel.setOpaque(false); // Membuat form transparan agar background terlihat
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        // Add fields with labels
        JLabel titleLabel = createStyledLabel("Registrasi Akun");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel usernameLabel = createStyledLabel("Username");
        usernameField = createStyledTextField();

        JLabel emailLabel = createStyledLabel("Email");
        emailField = createStyledTextField();

        JLabel passwordLabel = createStyledLabel("Password");
        passwordField = createStyledPasswordField();

        JLabel confirmPasswordLabel = createStyledLabel("Confirm Password");
        confirmPasswordField = createStyledPasswordField();

        // Buttons
        JButton registerButton = createStyledButton("Register", new Color(50, 150, 250), Color.WHITE);
        registerButton.addActionListener(this::handleRegistration);

        JButton backButton = createStyledButton("Back to Login", new Color(200, 200, 200), Color.BLACK);
        backButton.addActionListener(e -> {
            dispose(); 
            new Login().setVisible(true);
        });

        // Add components to the formPanel
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20))); 
        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(confirmPasswordLabel);
        formPanel.add(confirmPasswordField);
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(registerButton);
        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        formPanel.add(backButton);

        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
<<<<<<< Updated upstream
        label.setForeground(Color.WHITE); // Teks putih agar terlihat pada background
=======
        label.setForeground(Color.BLACK); 
>>>>>>> Stashed changes
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 40));
        field.setMaximumSize(new Dimension(350, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(350, 40));
        field.setMaximumSize(new Dimension(350, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(350, 40));
        button.setMaximumSize(new Dimension(350, 40));
        button.setBorder(BorderFactory.createEmptyBorder());
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(background.darker());
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(background);
            }
        });
        return button;
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
            return true; // Jika terjadi error, anggap username sudah ada
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
            return true; // Jika terjadi error, anggap email sudah terdaftar
        }
    }

    private boolean registerUser(Connection conn, String username, String email, String password) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, email);
            pstmt.setString(3, password); // Anda dapat menambahkan hash password di sini
            return pstmt.executeUpdate() > 0;
        } catch (Exception e) {
            showError("Error registering user: " + e.getMessage());
            return false;
        }
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

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Registrasi().setVisible(true));
    }
}