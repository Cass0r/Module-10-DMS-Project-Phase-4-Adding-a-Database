//Required for the functions to operate such as Hashmap, scanner and Map.
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.*;
import java.text.DecimalFormat;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class MovieCollection {

    //private Map<String,Movie> movies;
    //made public for the unit testing
    //public Map<String,Movie> movies;
    public Map<String,Movie> movies;
    private List<Movie> moviesdb;         //Change to List to use add()

    public MovieCollection(){
        movies = new HashMap<>();
        db_Handler = new DatabaseHandler();
        // connect to database MovieCollection constructor
        if (db_Handler.connect()) {
            //db_Handler.displayAllMovies();   // Display movies after connecting

            //used to help keep movies within map
            refreshMovies();
        } else {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database.");
        }
    }

    //initialize the class for Database
    DatabaseHandler db_Handler = new DatabaseHandler();
//======================================================================================================================
    //used for unit testing
    public Movie getMovie(String title) {
        return movies.get(title);
    }

//======================================================================================================================
//addMovie(movie: Movie): boolean
    public  boolean addMovie(Movie movie){
       /* if (movies.containsKey(movie.getTitle())) {
            System.out.println("Error: Movie already exists in the collection.");
            return false; // Movie already exists
        }
        movies.put(movie.getTitle(), movie);
        db_Handler.addMovie();
*/

        if (movies.containsKey(movie.getTitle())) {
            System.out.println("Error: Movie already exists in the collection.");
            return false; // Movie already exists
        }
        movies.put(movie.getTitle(), movie);

        // Add movie to the SQLite database
        db_Handler.addMovie(
                movie.getTitle(),
                movie.getRelease_Year(),
                movie.getGenre(),
                movie.getDirector(),
                movie.getRating(),
                movie.getWatched_Status()
        );
        // Add the movie to the in-memory list
        //movies.add(movie);
        System.out.println("Movie successfully added to the collection and database.");
        return true;
    }
//======================================================================================================================
//removeMovie(title: String) boolean
    //leave message for movie that was removed
    public boolean removeMovie(String title) {
        /*
        if (movies.remove(title) == null) {
            System.out.println("Error: Movie not found.");
            return false;
        }
        System.out.println("Movie has been found and removed.");
        return true;
        */
        db_Handler.removeMovie(title);
        return true;
    }
//======================================================================================================================
//updateMovie(movie :Movie) boolean
    //works
    public boolean updateMovie(String title, String field, String newValue) {
        Movie movie = movies.get(title);  // Get from in-memory collection

        if (movie == null) {
            System.out.println("Error: Movie not found.");
            return false;
        }

        // ✅ Update the in-memory movie object
        switch (field.toLowerCase()) {
            case "title":
                if (movies.containsKey(newValue)) {
                    System.out.println("Error: A movie with this title already exists.");
                    return false;
                }
                movie.setTitle(newValue);
                movies.remove(title);
                movies.put(newValue, movie);
                break;

            case "release_year":
                try {
                    int newYear = Integer.parseInt(newValue);
                    if (newYear < 1900 || newYear > 2025) {
                        System.out.println("Error: Release year must be between 1900 and 2025.");
                        return false;
                    }
                    movie.setRelease_Year(newYear);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid year format.");
                    return false;
                }
                break;

            case "genre":
                movie.setGenre(newValue);
                break;

            case "director":
                movie.setDirector(newValue);
                break;

            case "rating":
                try {
                    float newRating = Float.parseFloat(newValue);
                    if (newRating < 0 || newRating > 100) {
                        System.out.println("Error: Rating must be between 0 and 100.");
                        return false;
                    }
                    movie.setRating(newRating);
                } catch (NumberFormatException e) {
                    System.out.println("Error: Invalid rating format.");
                    return false;
                }
                break;

            case "watched_status":
                //Convert "true"/"false" to "1"/"0" for SQLite
                String booleanValue = newValue.equalsIgnoreCase("true") ? "1" : "0";
                movie.setWatched_Status(Boolean.parseBoolean(newValue));
                newValue = booleanValue;  // Use "1"/"0" when passing to the database
                break;

        }

        //Update the database
        boolean updated = db_Handler.updateMovieAttribute(title, field, newValue);

        if (updated) {
            refreshMovies();  //Refresh in-memory collection
            System.out.println("Movie updated successfully.");
        } else {
            System.out.println("Failed to update movie.");
        }

        return updated;
    }
//----------------------------------------------------------------------------------------------------------------------
// 🔥 Refresh the in-memory movie list from the database
    public void refreshMovies() {
        movies.clear();         // Clear the Map

        String sql = "SELECT * FROM Movies;";
        try (Statement stmt = db_Handler.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Movie movie = new Movie(
                        rs.getString("title"),
                        rs.getInt("Release_Year"),
                        rs.getString("genre"),
                        rs.getString("director"),
                        rs.getFloat("rating"),
                        rs.getBoolean("watched_status")
                );

                movies.put(movie.getTitle(), movie);  //Add to the Map
            }
            System.out.println("In-memory collection refreshed.");
        } catch (SQLException e) {
            System.out.println("Failed to refresh in-memory collection: " + e.getMessage());
        }
    }

    public void close() {
        db_Handler.close();
    }
//======================================================================================================================
//getMovie():List<Movie>
    public void Display_MovieCollection(){
        if (movies.isEmpty()) {
            System.out.println("No movies in the collection.");
        } else {
            for (Movie movie : movies.values()) {
                System.out.println(movie);
            }
        }
        db_Handler.displayAllMovies();
    }
//======================================================================================================================
    public float calculateAverageRating() {
        if (movies.isEmpty()) {
            System.out.println("No movies to calculate an average rating.");
            return 0;
        }
        float sum = 0;
        for (Movie movie : movies.values()) {
            sum += movie.getRating();
        }
        float average = sum / movies.size();

        // Format the average to 2 decimal places
        DecimalFormat df = new DecimalFormat("#.0");
        String formattedAverage = df.format(average);

        // Output the formatted average
        System.out.println("Average Movie Rating: " + formattedAverage);
        return Float.parseFloat(formattedAverage);
    }
//======================================================================================================================
//upload data through textfile
    public void addMoviesFromFile(String filePath) {

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(","); // Assuming CSV format: Title,Year,Genre,Director,Rating,Watched

                if (parts.length != 6) {
                    System.out.println("Skipping invalid line (wrong number of fields): " + line);
                    continue;
                }
                //Focus on the inputs from the textfile
                String title = parts[0].trim();
                String yearStr = parts[1].trim();
                String genre = parts[2].trim();
                String director = parts[3].trim();
                String ratingStr = parts[4].trim();
                String watchedStr = parts[5].trim();

                // Validate title length
                if (title.length() < 1 || title.length() > 45) {
                    System.out.println("Skipping invalid movie (title length out of bounds): " + line);
                    continue;
                }

                // Validate year
                int year;
                try {
                    year = Integer.parseInt(yearStr);
                    if (year < 1900 || year > 2025) {
                        System.out.println("Skipping invalid movie (year out of range): " + line);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid movie (invalid year format): " + line);
                    continue;
                }

                // Validate genre (only letters and spaces, 3-20 characters)
                String[] validGenres = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance",
                        "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};

                boolean isValidGenre = false;
                for (String validGenre : validGenres) {
                    if (genre.equalsIgnoreCase(validGenre)) {
                        isValidGenre = true;
                        break;
                    }
                }

                if (!isValidGenre) {
                    System.out.println("Skipping invalid movie (invalid genre): " + line);
                    continue;
                }

                // Validate director (only letters and spaces, 2-25 characters)
                if (!director.matches("^[a-zA-Z ]+$") || director.length() < 2 || director.length() > 25) {
                    System.out.println("Skipping invalid movie (invalid director name): " + line);
                    continue;
                }

                // Validate rating
                float rating;
                try {
                    rating = Float.parseFloat(ratingStr);
                    if (rating < 0 || rating > 100) {
                        System.out.println("Skipping invalid movie (rating out of range): " + line);
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Skipping invalid movie (invalid rating format): " + line);
                    continue;
                }

                // Validate watched status (must be 'true' or 'false')
                if (!watchedStr.equalsIgnoreCase("true") && !watchedStr.equalsIgnoreCase("false")) {
                    System.out.println("Skipping invalid movie (invalid watched status): " + line);
                    continue;
                }
                boolean watched = Boolean.parseBoolean(watchedStr);

                // Add movie to collection
                addMovie(new Movie(title, year, genre, director, rating, watched));
                System.out.println("Added movie: " + title);
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }
//======================================================================================================================
//Menu
//This method displays a menu that allows the user to interact with the system.
    public void Movie_Menu () {
        //Menu test: letters failed, numbers passed, special failed)
        Scanner sc = new Scanner(System.in);
        int Option;

        do {
            System.out.println("\nMovie Collection Menu:");
            System.out.println("1. Add Movie");
            System.out.println("2. Remove Movie");
            System.out.println("3. Update Movie Collection");
            System.out.println("4. Display All Movies in Collection");
            System.out.println("5. Calculate Average Movie Rating");
            System.out.println("6. Exit Menu");
            System.out.print("Enter your option by the number associated: ");

            while (true) {
                // Check if the input is an integer
                if (!sc.hasNextInt()) {
                    System.out.println("\nInvalid input. Please enter a number between 1 and 6.");
                    sc.next(); // Clear the invalid input
                } else {
                    Option = sc.nextInt(); // Read the input

                    // Check if the input is within the valid range (1 or 2)
                    if (Option >= 1 && Option <= 6) {
                        break;  // Exit the loop if valid input is entered
                    } else {
                        System.out.println("Invalid option. Please enter a number between 1 and 2.");
                    }
                }
            }

//Using the switch for the menu so user can enter the option they choose by using the number associated in  the print
// above.
            switch (Option) {
//----------------------------------------------------------------------------------------------------------------------
                //Adding movie
                case 1:
                    //add the switch option for add movie manually or by text file
                    //Asking for manual  or text file option(letters failed, numbers failed, special failed)
                    System.out.println("How would you like to add the Movie");
                    //System.out.println("1. Textfile");
                    System.out.println("1. Database");
                    System.out.println("2. Manually");
                    System.out.print("Enter Option Here:");
                    int option2 ;//= sc.nextInt();

                    while (true) {
                        // Check if the input is an integer
                        if (!sc.hasNextInt()) {
                            System.out.println("Invalid input. Please enter a number between 1 and 2.");
                            sc.next(); // Clear the invalid input
                        } else {
                            option2 = sc.nextInt(); // Read the input

                            // Check if the input is within the valid range (1 or 2)
                            if (option2 >= 1 && option2 <= 2) {
                                break;  // Exit the loop if valid input is entered
                            } else {
                                System.out.println("Invalid option. Please enter a number between 1 and 2.");
                            }
                        }
                    }
                    switch (option2) {
//**********************************************************************************************************************
                        //--textfile option
                        case 1:
                            /*
                            //•	Never hardcode the file path the user must enter for the text file.  Always let the user enter the complete path.
                            System.out.print("Enter the full file path for the movies text file: ");
                            sc.nextLine();
                            String filePath = sc.nextLine();

                            // Validate that the input is not empty
                            while (filePath.trim().isEmpty()) {
                                System.out.println("Error: File path cannot be empty. Please enter a valid path.");
                                System.out.print("Enter the full file path for the movies text file: ");
                                filePath = sc.nextLine();
                            }
                            addMoviesFromFile(filePath);
                            */

                            if (db_Handler.connect()) {
                                System.out.println("Connected to the database successfully.");
                            } else {
                                System.out.println("Failed to connect to the database.");
                            }

                            break;
//**********************************************************************************************************************
                        //--manually
                        case 2:
//keep movie titles under 45 letters or no charString filePath = "C:\\movies.txt";
                            System.out.print("Enter Movie Title: ");
                            sc.nextLine();
                            String title = sc.nextLine();

                            while (title.length() < 1) {
                                System.out.println("Error: Title entered had no characters entered, please re-enter proper amount.");
                                System.out.print("Enter Movie Title: ");
                                title = sc.nextLine();
                            }

                            while (title.length() > 45) {
                                System.out.println("Error: Title exceeds 45 characters. Please enter a shorter title.");
                                System.out.print("Enter Movie Title: ");
                                title = sc.nextLine();
                            }

//allow movies from 1900 to 2025 make sure to allow re-entry
                            //or if letters are typed in
                            int year = 0;
                            while (true) {
                                System.out.print("Enter release year: ");
                                if (sc.hasNextInt()) {
                                    year = sc.nextInt();
                                    if (year >= 1890 && year <= 2025) {
                                        sc.nextLine(); // Consume leftover newline
                                        break;
                                    } else {
                                        System.out.println("Error: Year must be between 1890 and 2025.");
                                    }
                                } else {
                                    System.out.println("Error: Please enter a valid number.");
                                    sc.next(); // Consume invalid input
                                }
                            }

//only specific genre words in make sure to allow re-entry
                            String[] validGenres = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance", "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};
                            // Get the genre input and validate
                            String genre;
                            boolean validGenre = false;
                            do {
                                System.out.print("Enter genre: ");
                                genre = sc.nextLine();
                                // Check if the genre is valid
                                for (String valid : validGenres) {
                                    if (genre.equalsIgnoreCase(valid)) {
                                        validGenre = true;
                                        break;
                                    }
                                }
                                // If the genre is invalid, prompt the user to enter again
                                if (!validGenre) {
                                    System.out.println("Error: Invalid genre. Please enter a valid genre from the list: Action, Crime, Drama, Fantasy, Horror, Comedy, Romance, Science Fiction, Sports, Thriller, Mystery, War, Western.");
                                }
                            } while (!validGenre);

//keep up from 2 to 25 characters make sure to allow re-entry
                            System.out.print("Enter director: ");
                            String director = sc.nextLine();

// Regular expression: Only allows letters (a-z, A-Z) and spaces
                            while (!director.matches("^[a-zA-Z ]+$") || director.length() < 2 || director.length() > 25) {
                                if (!director.matches("^[a-zA-Z ]+$")) {
                                    System.out.println("Error: Name contains invalid characters. Please enter only letters and spaces.");
                                } else if (director.length() < 2) {
                                    System.out.println("Error: Name entered was below 2 characters, please re-enter name.");
                                } else if (director.length() > 25) {
                                    System.out.println("Error: Name entered was above 25 characters, please re-enter name.");
                                }

                                System.out.print("Enter director: ");
                                director = sc.nextLine();
                            }

//keep rating from 0 to 100 make sure to allow re-entry
                            float rating = -1; // Initialize with an invalid value

                            while (true) {
                                System.out.print("Enter rating: ");
                                String input = sc.nextLine();

                                // Check if the input is a valid floating-point number
                                try {
                                    rating = Float.parseFloat(input);

                                    // Check if it's within the valid range
                                    if (rating >= 0 && rating <= 100) {
                                        break; // Valid input, exit loop
                                    } else {
                                        System.out.println("Error: The rating must be between 0 and 100. Please enter a valid rating.");
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Invalid input. Please enter a number between 0 and 100.");
                                }
                            }

//Watch status should be true or false
                            boolean watched;
                            while (true) {
                                System.out.print("Watched? (true/false): ");
                                String input = sc.next().trim().toLowerCase();  // Use next() to capture input
                                // Clear the buffer after using next() to avoid leftover characters
                                sc.nextLine();

                                // Check if the input is valid (true or false)
                                if (input.equals("true")) {
                                    watched = true;
                                    break;  // Exit the loop once a valid boolean is entered
                                } else if (input.equals("false")) {
                                    watched = false;
                                    break;  // Exit the loop once a valid boolean is entered
                                } else {
                                    System.out.println("Error: Please enter 'true' or 'false'.");
                                }
                            }

//All attributes added will be added to constructor and onto to the list
                            addMovie(new Movie(title, year, genre, director, rating, watched));
                            break;
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                //Removing movie
                case 2:
                    //the movie will be checked from the method associated
                    System.out.print("Enter title to remove: ");
                    sc.nextLine();
                    String remove_title = sc.nextLine();
                    //removeMovie(remove_title);
                    //To integrate the database its best to add these instances at the end
                    //error handling
                    db_Handler.removeMovie(remove_title);



                    break;
//----------------------------------------------------------------------------------------------------------------------
                //Update Movie Collection
                case 3:
                    sc.nextLine(); // Clear the buffer before getting new input
                    // Enter the title of the movie to update
                    System.out.print("Enter title of the movie to update: ");
                    String updateTitle = sc.nextLine().trim();

                    /*
                    // Check if the movie exists before proceeding
                    if (!movies.containsKey(updateTitle)) {
                        System.out.println("Error: Movie not found.");
                        break;  }
                    */

                    if (!db_Handler.movieExists(updateTitle)) {  // <-- Check in the database
                        System.out.println("Error: Movie not found in the database.");
                        break;
                    }

                    // Ask for the field to update
                    System.out.print("Enter field to update (title, releaseYear, genre, director, rating, watchedStatus): ");
                    String field = sc.nextLine().trim();

                    // Ask for the new value to update
                    String newValue = "";
                    boolean validUpdate = false;

                    switch (field.toLowerCase()) {
//**********************************************************************************************************************
                        case "title":
                            // Keep movie titles under 45 characters
                            while (true) {
                                System.out.print("Enter new title (max 45 characters): ");
                                newValue = sc.nextLine().trim();
                                if (newValue.length() < 1) {
                                    System.out.println("Error: Title entered had no characters entered, please re-enter.");
                                } else if (newValue.length() > 45) {
                                    System.out.println("Error: Title exceeds 45 characters. Please enter a shorter title.");
                                } else {
                                    validUpdate = true;
                                    break;
                                }
                            }
                            break;
//**********************************************************************************************************************
                        case "release_year":
                            // Allow movies from 1900 to 2025
                            while (true) {
                                System.out.print("Enter new release year (1900-2025): ");
                                try {
                                    int year = Integer.parseInt(sc.nextLine().trim());
                                    if (year < 1900 || year > 2025) {
                                        System.out.println("Error: The year you entered was invalid. Please re-enter a year between 1900 and 2025.");
                                    } else {
                                        newValue = Integer.toString(year);
                                        validUpdate = true;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Invalid year format. Please enter a valid number.");
                                }
                            }
                            break;
//**********************************************************************************************************************
                        //use different variables for this case
                        case "genre":
                            // Only specific genre words are allowed
                            String[] validGenress = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance", "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};
                            while (true) {
                                System.out.print("Enter new genre: ");
                                newValue = sc.nextLine().trim();
                                boolean validGenree = false;
                                for (String valid : validGenress) {
                                    if (newValue.equalsIgnoreCase(valid)) {
                                        validGenree = true;
                                        break;
                                    }
                                }
                                if (!validGenree) {
                                    System.out.println("Error: Invalid genre. Please enter a valid genre from the list: Action, Crime, Drama, Fantasy, Horror, Comedy, Romance, Science Fiction, Sports, Thriller, Mystery, War, Western.");
                                } else {
                                    validUpdate = true;
                                    break;
                                }
                            }
                            break;
//**********************************************************************************************************************
                        case "director":
                            // Loop until a valid director name is entered
                            while (true) {
                                System.out.print("Enter new director's name (2-25 characters): ");
                                newValue = sc.nextLine().trim();  // Read input and remove leading/trailing spaces

                                // Check if the input is valid
                                if (newValue.length() < 2) {
                                    System.out.println("Error: Name entered was below 2 characters, please re-enter.");
                                } else if (newValue.length() > 25) {
                                    System.out.println("Error: Name entered was above 25 characters, please re-enter.");
                                } else if (!newValue.matches("^[a-zA-Z ]+$")) {  // Ensure the name contains only letters and spaces
                                    System.out.println("Error: Director name must contain only letters and spaces.");
                                } else {
                                    validUpdate = true;  // Mark the input as valid
                                    break;  // Exit the loop when valid input is entered
                                }
                            }
                            break;
//**********************************************************************************************************************
                        case "rating":
                            // Rating should be between 0 and 100
                            while (true) {
                                System.out.print("Enter new rating (0-100): ");
                                try {
                                    float rating = Float.parseFloat(sc.nextLine().trim());
                                    if (rating < 0 || rating > 100) {
                                        System.out.println("Error: The rating must be between 0 and 100. Please enter a valid rating.");
                                    } else {
                                        newValue = Float.toString(rating);
                                        validUpdate = true;
                                        break;
                                    }
                                } catch (NumberFormatException e) {
                                    System.out.println("Error: Invalid rating format. Please enter a valid number.");
                                }
                            }
                            break;
//**********************************************************************************************************************
                        case "watched_status":
                            // Watched status should be 'true' or 'false'
                            while (true) {
                                System.out.print("Watched? (true/false): ");
                                String input = sc.nextLine().trim().toLowerCase();
                                if (input.equals("true")) {
                                    newValue = "true";
                                    validUpdate = true;
                                    break;
                                } else if (input.equals("false")) {
                                    newValue = "false";
                                    validUpdate = true;
                                    break;
                                } else {
                                    System.out.println("Error: Please enter 'true' or 'false'.");
                                }
                            }
                            break;

                        default:
                            System.out.println("Error: Invalid field selected.");
                            break;
                    }

                    // Once a valid update value is entered, proceed with the update
                    if (validUpdate) {
                        if (updateMovie(updateTitle, field, newValue)) {
                            System.out.println("Movie updated successfully.");
                        } else {
                            System.out.println("Failed to update movie.");
                        }
                    }
                    break;
//----------------------------------------------------------------------------------------------------------------------
                //Display All Movies in Collection
                case 4:
                //add a error handling for empty table
                    //Display_MovieCollection();
                    db_Handler.displayAllMovies();
                    break;
//----------------------------------------------------------------------------------------------------------------------
                //Calculate Average Movie Rating
                case 5:
                    db_Handler.calculateAverageRating();
                    //calculateAverageRating();
                    break;
//----------------------------------------------------------------------------------------------------------------------
                //Exiting the menu
                case 6:
                    System.out.println("Exiting Menu...");
                    break;

                //incase there is a miss input of any other numbers then what is given.
                default:
                    System.out.println("Invalid option, please re-enter the option offered above.");
            }
        } while (Option != 6);
    }//menu method
}//class


//C:\sqlite\MovieCollectionDatabase.db