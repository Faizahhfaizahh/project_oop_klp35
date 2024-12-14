import java.sql.*;

abstract public class KoneksiDatabase {
    private static final String URL = "jdbc:mysql://localhost:3306/slidnumber";
    private static final String usn = "root";
    private static final String pwd = "";

    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, usn, pwd);
    }

    // Metode untuk menyimpan data
    public static void setDataMenang(int skorTinggi, String waktu, int userId, int detik) {
        String sql = "INSERT INTO data (skor_tinggi, waktu, detik, user_id) VALUES (?, ?, ?, ?)";
        PreparedStatement pstmt = null;

        try {
            Connection conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, skorTinggi);
            pstmt.setString(2, waktu);
            pstmt.setInt(3, detik);
            pstmt.setInt(4, userId);
            pstmt.executeUpdate();
            System.out.println("Data berhasil disimpan.");
        } catch (SQLException e) {
            System.err.println("Gagal menyimpan data: " + e.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException e) {
                System.err.println("Gagal menutup pstmt: " + e.getMessage());
            }
        }
    }
    public static int cekSkor_Tinggi() {
        int skor_tinggi=0;
        try (Connection conn = KoneksiDatabase.getConnection()) {
            String query = "SELECT skor_tinggi FROM data ORDER BY skor_tinggi ASC LIMIT 1";
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (rs.next()) {
                skor_tinggi = rs.getInt("skor_tinggi");
            } else {
                skor_tinggi = 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return skor_tinggi;
    }
    public static String cekWaktu() {
        String waktu="0 h 0 m 0 s";
        try (Connection conn = KoneksiDatabase.getConnection()) {
            String query = "SELECT skor_tinggi, waktu FROM data ORDER BY detik ASC LIMIT 1";
            ResultSet rs = conn.createStatement().executeQuery(query);
            if (rs.next()) {
                waktu = rs.getString("waktu");
            } else {
                waktu = "0 h 0 m 0 s";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return waktu;
    }
}