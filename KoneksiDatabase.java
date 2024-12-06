import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KoneksiDatabase {
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver"; 
    static final String DB_URL = "jdbc:mysql://localhost:3306/slidnumber"; 
    static final String USER = "root";
    static final String PASS = "";

    private static Connection conn;

    // Konstruktor untuk inisialisasi koneksi
    public KoneksiDatabase() {
        try {
            // Register driver
            Class.forName(JDBC_DRIVER);

            // Inisialisasi koneksi
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Koneksi ke database berhasil.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Koneksi ke database gagal: " + e.getMessage());
        }
    }

    // Metode untuk mendapatkan koneksi
    public static Connection getConnection() {
        if (conn == null) {
            try {
                // Register driver
                Class.forName(JDBC_DRIVER);

                // Inisialisasi koneksi
                conn = DriverManager.getConnection(DB_URL, USER, PASS);
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Gagal mendapatkan koneksi: " + e.getMessage());
            }
        }
        return conn;
    }

    // Tutup koneksi (opsional)
    public static void closeConnection() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
                System.out.println("Koneksi database ditutup.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
