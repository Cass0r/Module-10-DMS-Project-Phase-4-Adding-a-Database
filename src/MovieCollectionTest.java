import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class MovieCollectionTest {

    private MovieCollection movieCollection;

    @BeforeEach
    void setUp() {
        movieCollection = new MovieCollection();
    }

    //----------------------------------------------------------------------------------------------------------------------
    //add movie test
    //The user is able to manually enter a new record, which is printed to the screen. Every user input has appropriate
    //error handling, the user can never crash the program or enter “bad” data
    @Test
    void testAddMovie() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.7f, true);
        assertTrue(movieCollection.addMovie(movie), "Movie should be added successfully.");
    }

    //test to see if duplicate can exist
    @Test
    void testAddMovie_InvalidField() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie); // First add should work

        assertFalse(movieCollection.addMovie(movie), "Adding duplicate movie should fail.");
    }
//----------------------------------------------------------------------------------------------------------------------
    //Remove Objects Test
    //In your video demonstration, a unit test correctly verifies that an object can be removed from the system.
    @Test
    void testRemoveMovie() {
        Movie movie = new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true);
        movieCollection.addMovie(movie);
        assertTrue(movieCollection.removeMovie("Inception"), "Movie should be removed successfully.");
    }

    //test to see if the method will remove  a movie that doesn't exist.
    @Test
    void testRemoveMovie_NotFound() {
        assertFalse(movieCollection.removeMovie("NonExistentMovie"), "Removing non-existent movie should fail.");
    }
//----------------------------------------------------------------------------------------------------------------------
    //Update Objects Test
    //The user can update any field of any object, which is printed to the screen. Every user input has appropriate
    //error handling, the user can never crash the program or enter “bad” data.

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
    @Test
    void testCalculateAverageRating() {
        movieCollection.addMovie(new Movie("Inception", 2010, "Science Fiction", "Christopher Nolan", 95.0f, true));
        movieCollection.addMovie(new Movie("The Dark Knight", 2008, "Action", "Christopher Nolan", 90.0f, true));

        float averageRating = movieCollection.calculateAverageRating();
        assertEquals(92.5f, averageRating, 0.1, "Average rating should be correctly calculated.");
    }

//----------------------------------------------------------------------------------------------------------------------
    //Opening a File Test (Reading from File)
    //In your video demonstration, a unit test correctly verifies that a file can be opened.
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

    //test out the path finding
    @Test
    void testAddMoviesFromFile_Invalidpath() {
        //Test out code error handling for wrong path
        movieCollection.addMoviesFromFile("nonexistentfile.txt");
        // You can't assert an exception directly in your current code, but your console will show the error message.
        // You could redirect output to capture this if needed (optional extra).
    }

    //Testing out invalid inputs within the textfile
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