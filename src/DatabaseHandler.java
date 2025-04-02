import java.io.File;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

//This class will import from the codes geerated from the db helper
// This class handles SQLite database operations for the movie collection
/**
 * The DatabaseHandler class manages SQLite database operations for a movie collection.
 * <p>
 * This class is a singleton that provides methods to connect to a SQLite database, perform CRUD (Create, Read, Update, Delete) operations
 * on the `Movies` table, and calculate the average movie rating. It also handles checking if the required table exists in the database and
 * displays information to the user using JOptionPane dialogs.
 *
 * </p>
 *
 * - {@link #conn} - This attribute is Database connection instance.
 *
 * <p>
 * Note: This class does not require a constructor, as the main method serves as the entry point.
 */
public class DatabaseHandler {
    // Database connection instance
    /**
     * The database connection instance used to interact with the SQLite database.
     */
    private Connection conn;

    // Singleton instance of the handler
    /**
     * Returns the singleton instance of DatabaseHandler.
     * <p>
     * If an instance does not already exist, it will be created.
     * </p>
     */
    private static DatabaseHandler instance;


    /**
     * Returns the single instance of the {@code DatabaseHandler} class (Singleton pattern).
     * <p>
     * If an instance of {@code DatabaseHandler} does not already exist, a new instance is created and returned.
     * This method ensures that only one instance of the {@code DatabaseHandler} exists throughout the application's lifecycle.
     * </p>
     *
     * @return the single {@code DatabaseHandler} instance.
     */
    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }
        return instance;
    }
//----------------------------------------------------------------------------------------------------------------------
//Connects to the database
    /**
     * Establishes a connection to the SQLite database by prompting the user for a file path.
     * <p>
     * The method continuously asks the user for a valid database file path until a successful connection is established. If the `Movies`
     * table does not exist in the database, the connection will be closed, and the user will be notified.
     * </p>
     *
     * @return true if the connection was successful, false otherwise
     */
    public boolean connect() {
        boolean connected = false;
        while (!connected) {
            String dbPath = JOptionPane.showInputDialog("Enter the SQLite database file path:");

            //cancel button
            if (dbPath == null) {  // Cancel clicked
                JOptionPane.showMessageDialog(null, "Connection canceled.");
                return false;       // Exit the method gracefully
            }

            //Handle empty or null path
            if (dbPath == null || dbPath.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Invalid file path. Please try again.");
                continue;
            }

            File dbFile = new File(dbPath);

            // Validate if file exists
            if (!dbFile.exists() || !dbFile.isFile()) {
                JOptionPane.showMessageDialog(null, "File not found. Please enter a valid database path.");
                continue;
            }

            try {
                conn = DriverManager.getConnection("jdbc:sqlite:" + dbPath);

                //Check if the `Movies` table exists
                if (!doesTableExist("Movies")) {
                    JOptionPane.showMessageDialog(null, "Error: The database is missing the required 'Movies' table!");
                    conn.close();  // Close the connection gracefully
                    conn = null;   // Reset connection
                    continue;
                }

                JOptionPane.showMessageDialog(null, "Connected to database: " + dbPath);
                connected = true;
                return true;

            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to connect: " + e.getMessage() + "\nPlease try again.");
                conn = null;  // Ensure connection is reset on failure
            }
        }

        return false;
    }
//----------------------------------------------------------------------------------------------------------------------
//Checks if a table exists in the database.
    /**
     * Checks if a specified table exists in the SQLite database.
     * <p>
     * This method queries the database's system catalog to check for the existence of a table with the given name.
     * </p>
     *
     * @param tableName the name of the table to check for existence
     * @return true if the table exists, false otherwise
     */
    private boolean doesTableExist(String tableName) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';")) {

            return rs.next();  //table exists if there's a result
        } catch (SQLException e) {
            e.printStackTrace();
            return false;  //Treat SQL exceptions as "table missing"
        }
    }
//----------------------------------------------------------------------------------------------------------------------
// Add movie with additional
    /**
     * Adds a new movie record to the `Movies` table.
     * <p>
     * This method constructs an SQL `INSERT` statement with the provided movie details and executes it to add the movie to the database.
     * </p>
     *
     * @param title the title of the movie
     * @param Release_Year the release year of the movie
     * @param genre the genre of the movie
     * @param director the director of the movie
     * @param rating the rating of the movie
     * @param watchedStatus the watched status of the movie (true/false)
     */
    public void addMovie(String title, int Release_Year, String genre,  String director, float rating, boolean watchedStatus) {
        String sql = "INSERT INTO Movies (title, Release_Year, genre,  director, rating, watched_status) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Set parameters for the query
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
    /**
     * Removes a movie record from the `Movies` table by title.
     * <p>
     * This method constructs an SQL `DELETE` statement with the provided title and executes it to remove the corresponding movie from the database.
     * </p>
     *
     * @param title the title of the movie to remove will be used as the key to find the movie
     */
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
    /**
     * Updates a movie attribute in the `Movies` table based on the movie title.
     * <p>
     * This method constructs an SQL `UPDATE` statement and executes it to modify a specified field of a movie record.
     * </p>
     *
     * @param title the title of the movie to update
     * @param field the field to update (e.g., "rating", "watched_status")
     * @param newValue the new value to set for the specified field
     * @return true if the update was successful, false otherwise
     */
    public boolean updateMovieAttribute(String title, String field, String newValue) {
        String sql = "UPDATE Movies SET " + field + " = ? WHERE title = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newValue);
            pstmt.setString(2, title);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;   // Returns true if update was successful
        } catch (SQLException e) {
            System.out.println("Failed to update movie attribute: " + e.getMessage());
            return false;  // Return false on error
        }
    }

//----------------------------------------------------------------------------------------------------------------------
    //Checks if a movie exists in the database by title.
    /**
     * Checks if a movie exists in the `Movies` table by title.
     * <p>
     * This method queries the `Movies` table to see if any record exists with the specified title.
     * </p>
     *
     * @param title the title of the movie to check
     * @return true if the movie exists, false otherwise
     */
    public boolean movieExists(String title) {
        String sql = "SELECT 1 FROM Movies WHERE Title = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            return rs.next();  // Return true if at least one row is found
        } catch (SQLException e) {
            System.out.println("Error checking if movie exists: " + e.getMessage());
            return false;
        }
    }
//----------------------------------------------------------------------------------------------------------------------
    // Display all movies with complete info
    /**
     * Displays all movies in the `Movies` table in a user-friendly format.
     * <p>
     * This method retrieves all movie records from the database and displays them in a formatted message using `JOptionPane`.
     * </p>
     */
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
    // Calculates and displays the average movie rating.
    /**
     * Calculates and displays the average rating of all movies in the `Movies` table.
     * <p>
     * This method calculates the average movie rating using the `AVG` function and displays the result in a formatted message.
     * </p>
     */
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
    /**
     * Closes the database connection gracefully.
     * <p>
     * This method ensures the database connection is closed if it is open and not already closed.
     * </p>
     */
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
    /**
     * Returns the current database connection instance.
     * @return the current database connection
     */
public Connection getConnection() {
    return conn;   // Return the current database connection
    }
}//class
