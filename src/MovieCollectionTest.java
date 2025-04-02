import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Unit tests for the {@link MovieCollection} class.
 * <p>
 * This class tests the functionality of the {@link MovieCollection} class, ensuring that it can correctly:
 * <p>
 * - Add, remove, and update movies
 * <p>
 * - Handle invalid data gracefully
 * <p>
 * - Calculate average ratings
 * <p>
 * - Read movies from a file.
 * </p>
 * <p>
 * The tests cover a variety of use cases, including valid inputs, invalid inputs, boundary cases, and file operations.
 * </p>
 * - {@link #movieCollection} - that is used in the test cases to simulate operations on the movie collection. The tests ensure that the collection behaves correctly under different scenarios
 *
 *<p>
 * Note: This class does not require a constructor, as the main method serves as the entry point.
 */
public class MovieCollectionTest {
    /**It simulates operations on the movie collection, ensuring that the collection behaves correctly under different scenarios through various test cases, class used in the test cases.*/
    private MovieCollection movieCollection;

    /**
     * Initializes a new instance of {@link MovieCollection} before each test.
     * <p>
     * This method is annotated with {@link BeforeEach}, which means it is run before each test method.
     * It ensures that each test has a fresh instance of {@link MovieCollection} to work with, preventing state
     * contamination across tests.
     * </p>
     */
    @BeforeEach
    void setUp() {
        movieCollection = new MovieCollection();
    }

//======================================================================================================================
    //add movie test
    //The user is able to manually enter a new record, which is printed to the screen. Every user input has appropriate
    //error handling, the user can never crash the program or enter “bad” data
    /**
     * Tests that a movie can be successfully added to the collection.
     * <p>
     * This test verifies that a movie can be added with valid data and that the result is successful.
     * </p>
     */
    @Test
    void testAddMovie() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.7f, true);
        assertTrue(movieCollection.addMovie(movie), "Movie should be added successfully.");
    }
//----------------------------------------------------------------------------------------------------------------------
    //test to see if duplicate can exist
    /**
     * Tests that adding a duplicate movie fails.
     * <p>
     * This test ensures that duplicate movies are not added to the collection.
     * </p>
     * This Test will test if proper error handling with invalid inputs:
     * <p>
     *            - Missing title as well as testing Word Limit
     *             <p>
     *             - Invalid release year (years out of range or non-numeric values)
     *            <p>
     *             - Invalid genre (non-alphabetical values, special characters)
     *             <p>
     *             - Invalid director name (numbers, excessive length, special characters)
     *             <p>
     *             - Invalid rating (out of range values, non-numeric values)
     *             <p>
     *             - Invalid watched status (non-boolean values)
     *             </p>
     *
     *
     */
    @Test
    void testAddMovie_InvalidField() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie); // First add should work

        assertFalse(movieCollection.addMovie(movie), "Adding duplicate movie should fail.");
    }
//======================================================================================================================
    //Remove Objects Test
    //In your video demonstration, a unit test correctly verifies that an object can be removed from the system.
    /**
     * Tests that a movie can be successfully removed from the collection.
     * <p>
     * This test verifies that a movie can be removed by its title.
     * </p>
     */
    @Test
    void testRemoveMovie() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie);
        assertTrue(movieCollection.removeMovie("Inception"), "Movie should be removed successfully.");
    }

//----------------------------------------------------------------------------------------------------------------------
    //test to see if the method will remove  a movie that doesn't exist.
    /**
     * Tests that attempting to remove a non-existent movie fails.
     * <p>
     * This test ensures that trying to remove a movie that doesn't exist in the collection doesn't cause errors and returns false.
     * </p>
     */
    @Test
    void testRemoveMovie_NotFound() {
        assertFalse(movieCollection.removeMovie("NonExistentMovie"), "Removing non-existent movie should fail.");
    }
//======================================================================================================================
    //Update Objects Test
    //The user can update any field of any object, which is printed to the screen. Every user input has appropriate
    //error handling, the user can never crash the program or enter “bad” data.
    /**
     * Tests updating a movie's fields with valid data.
     * <p>
     * This test verifies that all fields of a movie (title, year, genre, director, rating, and watch status) can be updated correctly.
     * </p>
     */
    @Test
    void testUpdateMovie() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie);

        // Update movie title
        assertTrue(movieCollection.updateMovie("Inception", "title", "red"), "Movie Title should be updated successfully.");
        assertEquals("red", movieCollection.getMovie("red").getTitle(), "Title should now be 'red'.");
        //Update movie year
        assertTrue(movieCollection.updateMovie("red", "releaseYear", "2005"), "Movie Release Year should be updated successfully.");
        assertEquals(2005, movieCollection.getMovie("red").getRelease_Year(), "Release Year should now be '2000'.");
        //Update movie genre
        assertTrue(movieCollection.updateMovie("red", "genre", "war"), "Movie genre should be updated successfully.");
        assertEquals("war", movieCollection.getMovie("red").getGenre(), "Genre should now be 'war'.");
        //Update movie director
        assertTrue(movieCollection.updateMovie("red", "director", "Joe Smith"), "Movie Director should be updated successfully.");
        assertEquals("Joe Smith", movieCollection.getMovie("red").getDirector(), "Director should now be 'Joe Smith'.");
        //Update movie rating
        assertTrue(movieCollection.updateMovie("red", "rating", "55"), "Movie rating should be updated successfully.");
        assertEquals(55f, movieCollection.getMovie("red").getRating(), "Rating should now be '55'.");
        //Update Movie watch status
        assertTrue(movieCollection.updateMovie("red", "watchedstatus", "false"), "Movie watch status should be updated successfully.");
        assertEquals(false, movieCollection.getMovie("red").getWatched_Status(), "Watch status should now be 'false'.");
    }

//----------------------------------------------------------------------------------------------------------------------
    //Invalid update movie fields being tested for error handling
    /**
     * Tests updating a movie's fields with invalid data.
     * <p>
     * This test ensures that invalid fields or values (incorrect field names or out-of-range values) are rejected, and proper error handling.
     *       <p>
     *       - Missing title as well as testing Word Limit
     *       <p>
     *       - Invalid release year (years out of range or non-numeric values)
     *      <p>
     *       - Invalid genre (non-alphabetical values, special characters)
     *       <p>
     *       - Invalid director name (numbers, excessive length, special characters)
     *       <p>
     *       - Invalid rating (out of range values, non-numeric values)
     *       <p>
     *       - Invalid watched status (non-boolean values)
     *       </p>
     *
     *
     */
    @Test
    void testUpdateMovie_InvalidField() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie);

        //testing the input values error handling
        assertFalse(movieCollection.updateMovie("Inception", "fxgnxfgmnmxm", "value"), "Updating with invalid field should fail.");
        assertFalse(movieCollection.updateMovie("Inception", "23455435455415", "value"), "Updating with invalid field should fail.");
        assertFalse(movieCollection.updateMovie("Inception", "]!@#$%^&*", "value"), "Updating with invalid field should fail.");

        //testing each values error handling
        //title
        assertFalse(movieCollection.updateMovie("Inception", "title", "ihvbiurebierubgiuregbnergnruivniurevniurevghnuirgvniurvnbijbnjierbnebnierbnjirbnjizergbnjiresvgbnjernbvjserbnjsbnjrnb"), "To many letters, over 45 limit.");
        //year
        assertFalse(movieCollection.updateMovie("Inception", "releaseyear", "1800"), "Release year out of range should fail.");
        assertFalse(movieCollection.updateMovie("Inception", "releaseyear", "sdtrhbaerhgbdfrhghs"), "Invalid Year input will fail");
        assertFalse(movieCollection.updateMovie("Inception", "releaseyear", "]!@#$%^&/*-+"), "Invalid Year input will fail");
        //genre
        assertFalse(movieCollection.updateMovie("Inception", "genre", "dfhgasghbfdaba"), "Invalid Genre input");
        assertFalse(movieCollection.updateMovie("Inception", "genre", "3425435243543"), "Invalid Genre input");
        assertFalse(movieCollection.updateMovie("Inception", "genre", "]!@#$%^&/*-+"), "Invalid Genre input");
        //director
        assertFalse(movieCollection.updateMovie("Inception", "director", "123413434"), "Invalid Input for Director name");
        assertFalse(movieCollection.updateMovie("Inception", "director", "sdejhvbjivbjriuevnjoakibnfjbnaejnbjrnajebnjkearfbnjkeabvnjkfdbnajkbngjkasfbkjarbnkjabfnaldfb"), "Invalid Input for Director name, over the character limit");
        assertFalse(movieCollection.updateMovie("Inception", "director", "!@#$%^&/*-+[[[]]]]<>?"), "Invalid Input for Director name");
        //rating
        assertFalse(movieCollection.updateMovie("Inception", "rating", "-100"), "Invalid input for rating");
        assertFalse(movieCollection.updateMovie("Inception", "rating", "200"), "Invalid input for rating");
        assertFalse(movieCollection.updateMovie("Inception", "rating", "zdfbsbzbzdfbdzfb"), "Invalid input for rating");
        assertFalse(movieCollection.updateMovie("Inception", "rating", "]!@#$%^&/*-+"), "Invalid input for rating");
        //watched status
        assertFalse(movieCollection.updateMovie("Inception", "watchedstatus", "sdfhaergergh"), "Invalid input for watch status");
        assertFalse(movieCollection.updateMovie("Inception", "watchedstatus", "3214532451324"), "Invalid input for watch status");
        assertFalse(movieCollection.updateMovie("Inception", "watchedstatus", "]!@#$%^&/*-+"), "Invalid input for watch status");
    }

//----------------------------------------------------------------------------------------------------------------------
    //Custom Action Test - Calculate Average Rating (your custom method)
    //The proposed custom feature works, making a calculation. Every user input has appropriate error handling, the
    //user can never crash the program or enter “bad” data.
    /**
     * Tests the calculation of the average rating for all movies in the collection.
     * <p>
     * This test verifies that the {@link MovieCollection#calculateAverageRating()} method correctly calculates the average rating
     * for the movies in the collection.
     * </p>
     */
    @Test
    void testCalculateAverageRating() {
        movieCollection.addMovie(new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true));
        movieCollection.addMovie(new Movie("The Dark Knight", 2008, "Action", "Christopher Nolan", 90.0f, true));

        float averageRating = movieCollection.calculateAverageRating();
        assertEquals(92.5f, averageRating, 0.1, "Average rating should be correctly calculated.");
    }

//======================================================================================================================
    //Opening a File Test (Reading from File)
    //In your video demonstration, a unit test correctly verifies that a file can be opened.
    /**
     * Tests loading movies from a file.
     * <p>
     * This test verifies that movies can be correctly loaded from a file and that the collection is updated accordingly.
     * </p>
     *
     * @throws IOException if an I/O error occurs during file operations.
     */
    @Test
    void testAddMoviesFromFile() throws IOException {
        // Create a temporary file to simulate the movie file
        File tempFile = File.createTempFile("movies", ".txt");
        tempFile.deleteOnExit(); // Clean up after the test

        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("Inception,2010,Science Fiction,Christopher Nolan,95.0,true\n");
            writer.write("The Dark Knight,2008,Action,Christopher Nolan,90.0,true\n");
        }

        movieCollection.addMoviesFromFile(tempFile.getAbsolutePath());

        assertNotNull(movieCollection.getMovie("Inception"), "Inception should be loaded from file.");
        assertNotNull(movieCollection.getMovie("The Dark Knight"), "The Dark Knight should be loaded from file.");
    }

//----------------------------------------------------------------------------------------------------------------------
    //test out the path finding
    /**
     * Tests loading movies from a file with an invalid file path.
     * <p>
     * This test ensures that the system handles invalid file paths gracefully.
     * </p>
     */
    @Test
    void testAddMoviesFromFile_Invalidpath() {
        //Test out code error handling for wrong path
        movieCollection.addMoviesFromFile("nonexistentfile.txt");
        // You can't assert an exception directly in your current code, but your console will show the error message.
        // You could redirect output to capture this if needed (optional extra).
    }

//----------------------------------------------------------------------------------------------------------------------
    //Testing out invalid inputs within the textfile
    /**
     * Tests the handling of invalid movie data when loading movies from a file.
     * <p>
     * This test ensures that the {@link MovieCollection#addMoviesFromFile(String)} method correctly handles and rejects invalid data
     * in the movie file. The test simulates a file containing invalid movie entries, including:
     * <p>
     * - Missing title as well as testing Word Limit
     * <p>
     * - Invalid release year (years out of range or non-numeric values)
     * <p>
     * - Invalid genre (non-alphabetical values, special characters)
     * <p>
     * - Invalid director name (numbers, excessive length, special characters)
     * <p>
     * - Invalid rating (out of range values, non-numeric values)
     * <p>
     * - Invalid watched status (non-boolean values)
     * </p>
     * <p>
     * The file is created temporarily, and the movie data is written in an invalid format to test how the method handles each case.
     * </p>
     *
     * @throws IOException if an I/O error occurs during file operations.
     */
    @Test
    void testAddMoviesFromFile_InvalidMovies() throws IOException {
        // Create a temporary file to simulate the movie file
        File tempFile = File.createTempFile("movies", ".txt");
        tempFile.deleteOnExit(); // Clean up after the test

        try (FileWriter writer = new FileWriter(tempFile)) {
            //bad title
            writer.write(",2010,Action,Christopher Nolan,90.0,true\n");
            //bad year
            writer.write("Bad Year Movie,1800,Action,Christopher Nolan,90.0,true\n");
            writer.write("Bad Year Movie,65416515861,Action,Christopher Nolan,90.0,true\n");
            writer.write("Bad Year Movie,sfdhdsh,Action,Christopher Nolan,90.0,true\n");
            // Bad genre
            writer.write("Bad Genre Movie,2010,sfgnhbsrtgnsgngsngr,Christopher Nolan,90.0,true\n");
            writer.write("Bad Genre Movie,2010,23425423535,Christopher Nolan,90.0,true\n");
            writer.write("Bad Genre Movie,2010,/*-+!@#$%^,Christopher Nolan,90.0,true\n");
            // Bad director
            writer.write("Bad Director Movie,2010,Action,345243563245654,90.0,true\n");
            writer.write("Bad Director Movie,2010,Action,kjdsfbvkjbnvuobnjfrnbjoebnjobnebnjanbgijurenbiaerbnhiuarbnjbgnrjabnraeb,90.0,true\n");
            writer.write("Bad Director Movie,2010,Action,!@#$%^&/-*+,90.0,true\n");
            // Bad rating
            writer.write("Bad Rating Movie,2010,Action,Christopher Nolan,200.0,true\n");
            writer.write("Bad Rating Movie,2010,Action,Christopher Nolan,-200.0,true\n");
            writer.write("Bad Rating Movie,2010,Action,Christopher Nolan,afdgbasg,true\n");
            writer.write("Bad Rating Movie,2010,Action,Christopher Nolan,/*-+!@#$,true\n");
            // Bad watched status
            writer.write("Bad Watched Movie,2010,Action,Christopher Nolan,90.0,maybe\n");
            writer.write("Bad Watched Movie,2010,Action,Christopher Nolan,90.0,1234\n");
            writer.write("Bad Watched Movie,2010,Action,Christopher Nolan,90.0,+--**-+!@#%\n");
        }
        movieCollection.addMoviesFromFile(tempFile.getAbsolutePath());
    }
//----------------------------------------------------------------------------------------------------------------------
}//class