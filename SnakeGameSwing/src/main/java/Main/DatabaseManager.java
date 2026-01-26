package Main;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 * @author : Asnit Bakhati
 */
public class DatabaseManager {
        private static final String DB_NAME = "sqliteSnake.db";
        private static final String USER_HOME = System.getProperty("user.home");
        private static final String DB_PATH = USER_HOME + File.separator + ".snakegame" + File.separator + DB_NAME;

        public Connection getConnection() {
            try {
                File folder = new File(USER_HOME + File.separator + ".snakegame");
                if (!folder.exists()) folder.mkdirs();

                String url = "jdbc:sqlite:" + DB_PATH;
                return DriverManager.getConnection(url);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }
        }

    public DatabaseManager(){
        initializeDatabase();
    }



    public void initializeDatabase(){
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT NOT NULL UNIQUE," +
                    "password TEXT NOT NULL)";

            String sql2 = "CREATE TABLE IF NOT EXISTS score (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "highscore INTEGER NOT NULL DEFAULT 0)";

            stmt.execute(sql);
            stmt.execute(sql2);

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
        initializeScore();
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



    public String getStoredUsername() throws SQLException {
        String sql = "SELECT name FROM users LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getString("name");
            }
            return null;
        }
    }


    private void initializeScore() throws SQLException {
        String sql = "INSERT INTO score (highscore) VALUES (0)";
        try (Connection conn = getConnection();
             PreparedStatement st = conn.prepareStatement(sql)) {
            st.executeUpdate();
        }
    }


    public int getHighScore() throws SQLException {
        String sql = "SELECT highscore FROM score LIMIT 1";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("highscore");
            }
            return 0;
        }
    }


    public void updateHighScore(int newScore) throws SQLException {
        String checkSql = "SELECT COUNT(*) as count FROM score";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(checkSql)) {

            if (rs.next() && rs.getInt("count") == 0) {
                String insertSql = "INSERT INTO score (highscore) VALUES (?)";
                try (PreparedStatement st = conn.prepareStatement(insertSql)) {
                    st.setInt(1, newScore);
                    st.executeUpdate();
                }
            } else {
                String updateSql = "UPDATE score SET highscore = ? WHERE id = 1";
                try (PreparedStatement st = conn.prepareStatement(updateSql)) {
                    st.setInt(1, newScore);
                    st.executeUpdate();
                }
            }
        }
    }
}