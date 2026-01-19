package Main;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * @author : Asnit Bakhati
 */
public class DatabaseManager {

    private final String DbUrl = "jdbc:sqlite:snakeGame.db";

    public DatabaseManager(){
        initializeDatabase();
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DbUrl);
    }

    public void initializeDatabase(){
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)";
            stmt.execute(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String userName, String password) throws SQLException, NoSuchAlgorithmException {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Username and password must be at least 8 characters long");
        }

        String hashedPass = hashString(password);
        String query = "INSERT INTO users (name, password) VALUES(?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, userName);
            st.setString(2, hashedPass);
            st.executeUpdate();
        }
    }

    public boolean checkLogin(String userName, String password) throws SQLException, NoSuchAlgorithmException {
        String sql = "SELECT password FROM users WHERE name = ?";
        String hashedPass = hashString(password);

        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.setString(1, userName);

            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    String dbPass = rs.getString("password");
                    return dbPass.equals(hashedPass);
                }
                return false;
            }
        }
    }

    public String hashString(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] passHash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : passHash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    public boolean noUser() throws SQLException {
        String sql = "SELECT COUNT(*) as count FROM users";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("count") == 0;
            }
            return true;
        }
    }

    /**
     * test code
     */

//    public static void main(String[] args) {
//        DatabaseManager dbm = new DatabaseManager();
//        try {
//            if(dbm.checkLogin("Mobile","MobileUser123")) System.out.println("Success");
//            else System.out.println("Failed");
//            if(dbm.checkLogin("Mobile","RandomPass")) System.out.println("Failed");
//            else System.out.println("Success");
//
//            if(dbm.checkLogin("Monley","HenlyeTshirt")) System.out.println("Failed");
//            System.out.println("Success");
//        }catch (Exception ex){
//            System.out.println("Some Exception Occurred");
//        }
//    }
}