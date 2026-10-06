import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;
 
/**
 * All database work (SQLite via JDBC) lives here.
 * Every query uses PreparedStatement to prevent SQL injection.
 */
public class DatabaseHelper {
 
    private static final String URL = "jdbc:sqlite:reservation.db";
    private static final Random RANDOM = new Random();
 
    private DatabaseHelper() {
    }
 
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
 
    /** Creates tables and inserts demo users and trains on first run. */
    public static void initialize() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("SQLite JDBC driver not found. "
                    + "Add the sqlite-jdbc jar to the classpath.", e);
        }
 
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users ("
                    + "username TEXT PRIMARY KEY, "
                    + "password_hash TEXT NOT NULL)");
 
            st.executeUpdate("CREATE TABLE IF NOT EXISTS trains ("
                    + "train_number INTEGER PRIMARY KEY, "
                    + "train_name TEXT NOT NULL)");
 
            st.executeUpdate("CREATE TABLE IF NOT EXISTS reservations ("
                    + "pnr TEXT PRIMARY KEY, "
                    + "passenger_name TEXT NOT NULL, "
                    + "train_number INTEGER NOT NULL, "
                    + "train_name TEXT NOT NULL, "
                    + "class_type TEXT NOT NULL, "
                    + "journey_date TEXT NOT NULL, "
                    + "source TEXT NOT NULL, "
                    + "destination TEXT NOT NULL, "
                    + "booked_by TEXT NOT NULL)");
        }
        seedUsers();
        seedTrains();
    }
 
    private static void seedUsers() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM users")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }
        addUser("admin", "admin123");
        addUser("user1", "pass123");
    }
 
    private static void seedTrains() throws SQLException {
        try (Connection conn = getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM trains")) {
            if (rs.next() && rs.getInt(1) > 0) {
                return;
            }
        }
        addTrain(12301, "Howrah Rajdhani Express");
        addTrain(12951, "Mumbai Rajdhani Express");
        addTrain(12627, "Karnataka Express");
        addTrain(12002, "Bhopal Shatabdi Express");
        addTrain(12723, "Telangana Express");
    }
 
    private static void addUser(String username, String password) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hash(password));
            ps.executeUpdate();
        }
    }
 
    private static void addTrain(int number, String name) throws SQLException {
        String sql = "INSERT INTO trains (train_number, train_name) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, number);
            ps.setString(2, name);
            ps.executeUpdate();
        }
    }
 
    /** SHA-256 hash so passwords are not stored in plain text. */
    public static String hash(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
 
    public static boolean authenticate(String username, String password) throws SQLException {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getString("password_hash").equals(hash(password));
            }
        }
    }
 
    /** Returns the train name for a train number, or null if it does not exist. */
    public static String getTrainName(int trainNumber) throws SQLException {
        String sql = "SELECT train_name FROM trains WHERE train_number = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, trainNumber);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getString("train_name") : null;
            }
        }
    }
 
    /** Saves a booking and returns the generated PNR. */
    public static String createReservation(String passengerName, int trainNumber, String trainName,
                                           String classType, String journeyDate,
                                           String source, String destination,
                                           String bookedBy) throws SQLException {
        String pnr = generateUniquePnr();
        String sql = "INSERT INTO reservations (pnr, passenger_name, train_number, train_name, "
                + "class_type, journey_date, source, destination, booked_by) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pnr);
            ps.setString(2, passengerName);
            ps.setInt(3, trainNumber);
            ps.setString(4, trainName);
            ps.setString(5, classType);
            ps.setString(6, journeyDate);
            ps.setString(7, source);
            ps.setString(8, destination);
            ps.setString(9, bookedBy);
            ps.executeUpdate();
        }
        return pnr;
    }
 
    private static String generateUniquePnr() throws SQLException {
        while (true) {
            long number = 1_000_000_000L + (long) (RANDOM.nextDouble() * 9_000_000_000L);
            String pnr = String.valueOf(number);
            if (findReservation(pnr) == null) {
                return pnr;
            }
        }
    }
 
    public static Reservation findReservation(String pnr) throws SQLException {
        String sql = "SELECT * FROM reservations WHERE pnr = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pnr);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                return new Reservation(
                        rs.getString("pnr"),
                        rs.getString("passenger_name"),
                        rs.getInt("train_number"),
                        rs.getString("train_name"),
                        rs.getString("class_type"),
                        rs.getString("journey_date"),
                        rs.getString("source"),
                        rs.getString("destination"));
            }
        }
    }
 
    /** Deletes a booking. Returns true if a row was removed. */
    public static boolean cancelReservation(String pnr) throws SQLException {
        String sql = "DELETE FROM reservations WHERE pnr = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pnr);
            return ps.executeUpdate() > 0;
        }
    }
}