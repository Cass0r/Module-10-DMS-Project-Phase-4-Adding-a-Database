//import java.util.concurrent.LinkedBlockingDeque;
////This is the entry point of the program. It creates an instance of MovieCollection and starts the menu for user to
////begin program.
/**
 * The MainApplication class serves as the entry point of the program.
 * It initializes an instance of the MovieCollection class and invokes the
 * movie menu to allow users to interact with the movie collection system.
 *
 * Role in the system: This class launches the program and sets up the main user interface
 * for interacting with movie data through the MovieCollection class.
 *
 * Usage example:
 * <pre>
 * MainApplication.main(new String[]{});
 * </pre>
 *
 * Note: This class does not require a constructor, as the main method serves as the entry point.
 */
public class MainApplication {
    /**
     * The main method that serves as the entry point to the program.
     * It initializes a MovieCollection object and calls the Movie_Menu method
     * to display the menu for user interaction.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        // Initialize an instance of MovieCollection to manage movie data
        MovieCollection MC = new MovieCollection();
        // Call the Movie_Menu method to display the menu to the user
        MC.Movie_Menu();
    }//main
}//class
