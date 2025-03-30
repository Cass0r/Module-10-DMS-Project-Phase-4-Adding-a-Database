import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public class DatabaseHandler {
    private Connection conn;
//----------------------------------------------------------------------------------------------------------------------
    public boolean connect() {
        boolean connected = false;
        while (!connected) {
            String dbPath = JOptionPane.showInputDialog("Enter the SQLite database file path:");
            if (dbPath == null || dbPath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid file path. Please try again.");
                continue;
            }

            File dbFile = new File(dbPath);
            if (!dbFile.exists() || !dbFile.isFile()) {
                JOptionPane.showMessageDialog(null, "File not found. Please enter a valid database path.");
                continue;
            }

            try {
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
                JOptionPane.showMessageDialog(null, "Connected to database: " + dbPath);
                connected = true;
                return true;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage() + "\nPlease try again.");
            }
        }
        return false;
    }
//----------------------------------------------------------------------------------------------------------------------
    // Add movie with additional fields
    public void addMovie(String title, int Release_Year, String genre,  String director, float rating, boolean watchedStatus) {
        String sql = "INSERT INTO Movies (title, Release_Year, genre,  director, rating, watched_status) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.setInt(2, Release_Year);
            stmt.setString(3, genre);
            stmt.setString(4, director);
            stmt.setFloat(5, rating);
            stmt.setBoolean(6, watchedStatus);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Movie added successfully.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to add movie: " + e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    // Remove movie by title
    public void removeMovie(String title) {
        String sql = "DELETE FROM Movies WHERE title = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            int rowsAffected = stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, rowsAffected + " movie(s) removed.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to remove movie: " + e.getMessage());
        }
    }

//----------------------------------------------------------------------------------------------------------------------
    // Update movie attribute by title
    //this works
    public boolean updateMovieAttribute(String title, String field, String newValue) {
        String sql = "UPDATE Movies SET " + field + " = ? WHERE title = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, title);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;   // ✅ Returns true if update was successful
        } catch (SQLException e) {
            System.out.println("Failed to update movie attribute: " + e.getMessage());
            return false;  // Return false on error
        }
    }


//----------------------------------------------------------------------------------------------------------------------
    //works
    public boolean movieExists(String title) {
        String sql = "SELECT 1 FROM Movies WHERE Title = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();  // ✅ Return true if at least one row is found
        } catch (SQLException e) {
            System.out.println("Error checking if movie exists: " + e.getMessage());
            return false;
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    // Display all movies with complete info
    public void displayAllMovies() {
        String sql = "SELECT * FROM Movies;";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            StringBuilder result = new StringBuilder("Movies in Collection:\n");
            while (rs.next()) {
                result.append(rs.getString("title")).append(" - ")
                        .append(rs.getString("genre")).append(" - ")
                        .append(rs.getInt("Release_Year")).append(" - ")
                        .append(rs.getString("director")).append(" - ")
                        .append(rs.getFloat("rating")).append(" - ")
                        .append(rs.getBoolean("watched_status") ? "Watched" : "Not Watched").append("\n");
            }
            JOptionPane.showMessageDialog(null, result.toString());
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to retrieve movies: " + e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    // Calculate average rating
    public void calculateAverageRating() {
        String sql = "SELECT AVG(rating) AS avg_rating FROM Movies;";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                double avgRating = rs.getDouble("avg_rating");
                DecimalFormat df = new DecimalFormat("#0.00");
                JOptionPane.showMessageDialog(null, "Average Rating: " + df.format(avgRating));
            } else {
                JOptionPane.showMessageDialog(null, "No movies found.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to calculate average rating: " + e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    // Close connection
    public void close() {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to close connection: " + e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
// Add this method to DatabaseHandler
public Connection getConnection() {
    return conn;   // Return the current database connection
}

}
