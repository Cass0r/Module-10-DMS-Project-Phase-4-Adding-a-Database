import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;

public class MovieFrame extends JFrame {
    //All parts of the java form on the GUI
    private JTextField tfUserMenuSelect;
    private JButton btnExit;
    private JPanel MainPanel;
    private JLabel MenuTitle;
    private JButton addMovieButton;
    private JButton displayMovieCollectionButton;
    private JButton calculateAverageMovieRatingButton;
    private JButton removeMovieButton;
    private JButton exitMenuButton;
    private JButton updateMovieButton;

    //Calls back to the MovieCollection class as reference
    private MovieCollection movieCollection;
    //----------------------------------------------------------------------------------------------------------------------
//Constructor
    public MovieFrame() {
        //The settings for the GUI when the program runs
        setContentPane(MainPanel);
        setTitle("Movie Collection");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 700);
        setLocationRelativeTo(null);
        setVisible(true);

//The said theme from the UITheme class will have all pop-ups matching the theme
        UITheme.applyPopupTheme();
//Initialize movie collection
        movieCollection = new MovieCollection();

//**********************************************************************************************************************
        // Add Movie Button
        addMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addMovie();
            }
        });

        // Remove Movie Button
        removeMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeMovie();
            }
        });

        // Display Movie Collection Button
        displayMovieCollectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                displayMovies();
            }
        });

        // Update Movie Button
        updateMovieButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateMovie();
            }
        });

        // Calculate Average Movie Rating Button
        calculateAverageMovieRatingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateAverageRating();
            }
        });
//**********************************************************************************************************************
        // Exit Button
        exitMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(MovieFrame.this, "Are you sure you want to exit?", "Exit Confirmation", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
    }
    //----------------------------------------------------------------------------------------------------------------------
//adding movies choice
    private void addMovie() {
        String[] options = {"Manually", "Database"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "How would you like to add the movie?",
                "Add Movie",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0] // Default selection
        );

        if (choice == 0) {
            addMovieManually(); // Calls manual input method
        } else if (choice == 1) {
            addMoviesFromDB(); // Calls file input method
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
//Adding movies manually, alot of what's done in this method will be repeated throughout the other methods for error
//handling as well as window setup
    private void addMovieManually() {
//**********************************************************************************************************************
        String title;
        do {
//Stating the instance through the JOption then using its utilities
            //Title
            title = JOptionPane.showInputDialog(this, "Enter Movie Title:");
            if (title == null) return;
            if (title.trim().isEmpty()) {
//Calling the theme class for the error method so that the error handling window shows up at red
                UITheme.applyErrorTheme(this, "Title cannot be empty. Please enter a valid title.","Error");
            }
        } while (title.trim().isEmpty());

        //year
        int year;
        while (true) {
            try {
                String yearInput = JOptionPane.showInputDialog(this, "Enter Release Year (1900-2025):");
                if (yearInput == null) return;
                year = Integer.parseInt(yearInput);
                if (year < 1900 || year > 2025) {
//Said error handling will be repeated throughout the class, much similar to the MovieCollection class
                    UITheme.applyErrorTheme(this, "Invalid Year! Must be between 1900 and 2025.","Error");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                UITheme.applyErrorTheme(this, "Invalid input! Please enter a valid year.","Error");
            }
        }

        //Genre
        String[] validGenres = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance",
                "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};
        String genre;
        boolean validGenre;
        do {
            genre = JOptionPane.showInputDialog(this, "Enter Genre (e.g., Action, Crime, Drama, etc.):");
            if (genre == null) return;

            validGenre = false;
            for (String valid : validGenres) {
                if (genre.equalsIgnoreCase(valid)) {
                    validGenre = true;
                    break;
                }
            }

            if (!validGenre) {
                UITheme.applyErrorTheme(this, "Error: Invalid genre. Please enter a valid genre from the list:" +
                        "\n Action, Crime, Drama, Fantasy, Horror, Comedy, Romance, Science Fiction, Sports, Thriller, Mystery, War, and Western." ,"Error");
            }
        } while (!validGenre);

        //Director
        String director;
        do {
            director = JOptionPane.showInputDialog(this, "Enter Director Name (2-25 characters):");
            if (director == null) return;
            if (!director.matches("^[a-zA-Z ]+$") || director.length() < 2 || director.length() > 25) {
                UITheme.applyErrorTheme(this, "Invalid director name! Must contain only letters and be 2-25 characters long.","Error");
            } else {
                break;
            }
        } while (true);

        //Rating
        float rating;
        while (true) {
            try {
                String ratingInput = JOptionPane.showInputDialog(this, "Enter Rating (0-100):");
                if (ratingInput == null) return;
                rating = Float.parseFloat(ratingInput);
                if (rating < 0 || rating > 100) {
                    UITheme.applyErrorTheme(this, "Invalid Rating! Must be between 0 and 100.","Error");
                } else {
                    break;
                }
            } catch (NumberFormatException e) {
                UITheme.applyErrorTheme(this, "Invalid input! Please enter a number between 0 and 100.","Error");
            }
        }

        //Watched Status
        boolean watched = JOptionPane.showConfirmDialog(this, "Have you watched it?", "Watched Status",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
//**********************************************************************************************************************
        Movie newMovie = new Movie(title, year, genre, director, rating, watched);
        if (movieCollection.addMovie(newMovie)) {
            JOptionPane.showMessageDialog(this, "Movie added successfully!");
        } else {
            UITheme.applyErrorTheme(this, "Movie already exists!","Error");
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
//Movies will be added through file (This class will share similar constraints as the last method
    private void addMoviesFromDB() {
        //  Ask for the database connection first
        if (!DatabaseHandler.getInstance().connect()) {
            UITheme.applyErrorTheme(this, "Failed to connect to the database. Operation cancelled.", "Connection Error");
            return;  // Stop execution if the connection fails
        }

        //  File chooser for selecting the movie text file
          JFileChooser fileChooser = new JFileChooser();
      //  int returnValue = fileChooser.showOpenDialog(this);

      /*  if (returnValue != JFileChooser.APPROVE_OPTION) {
            return;  // Cancel if no file is selected
        }*/

        File selectedFile = fileChooser.getSelectedFile();

        if (!selectedFile.exists() || !selectedFile.canRead()) {
            UITheme.applyErrorTheme(this, "Error: Cannot read the selected file. Please check file permissions.", "Error");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(selectedFile))) {
            String line;
            int successCount = 0, failCount = 0;
            StringBuilder errorMessages = new StringBuilder();
            String[] validGenres = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance",
                    "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length != 6) {
                    failCount++;
                    errorMessages.append("Skipping invalid line (incorrect number of fields): ").append(line).append("\n");
                    continue;
                }

                // Title
                String title = parts[0].trim();
                int year;

                try {
                    year = Integer.parseInt(parts[1].trim());
                    if (year < 1900 || year > 2025) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    failCount++;
                    errorMessages.append("Invalid year for movie: ").append(title).append(" (must be 1900-2025)\n");
                    continue;
                }

                // Genre validation
                String genre = parts[2].trim();
                boolean validGenre = false;
                for (String valid : validGenres) {
                    if (genre.equalsIgnoreCase(valid)) {
                        validGenre = true;
                        break;
                    }
                }

                if (!validGenre) {
                    failCount++;
                    errorMessages.append("Invalid genre for movie: ").append(title).append(" (must be one of " + String.join(", ", validGenres) + ")\n");
                    continue;
                }

                // Director validation
                String director = parts[3].trim();
                if (!director.matches("^[a-zA-Z ]+$") || director.length() < 2 || director.length() > 25) {
                    failCount++;
                    errorMessages.append("Invalid director name for movie: ").append(title).append(" (2-25 letters only)\n");
                    continue;
                }

                // Rating validation
                float rating;
                try {
                    rating = Float.parseFloat(parts[4].trim());
                    if (rating < 0 || rating > 100) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    failCount++;
                    errorMessages.append("Invalid rating for movie: ").append(title).append(" (must be 0-100)\n");
                    continue;
                }

                // Watched status validation
                boolean watched;
                if (parts[5].trim().equalsIgnoreCase("true") || parts[5].trim().equalsIgnoreCase("false")) {
                    watched = Boolean.parseBoolean(parts[5].trim());
                } else {
                    failCount++;
                    errorMessages.append("Invalid watched status for movie: ").append(title).append(" (must be 'true' or 'false')\n");
                    continue;
                }

                //  Add the movie to the database
                Movie movie = new Movie(title, year, genre, director, rating, watched);

                //  Use DatabaseHandler for database interaction
                if (!DatabaseHandler.getInstance().movieExists(title)) {
                   // DatabaseHandler.getInstance().addMovie(movie);
                    successCount++;
                } else {
                    failCount++;
                    errorMessages.append("Duplicate movie: ").append(title).append(" (already exists in database)\n");
                }
            }

            //  Display the summary of added movies
            String message = "Movies successfully added: " + successCount + "\nFailed entries: " + failCount;
            if (failCount > 0) {
                message += "\n\nErrors:\n" + errorMessages.toString();
            }
            JOptionPane.showMessageDialog(this, message);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error reading file: " + e.getMessage());
        }
    }
//----------------------------------------------------------------------------------------------------------------------
// Method to Remove Movie
    private void removeMovie() {
        String title = JOptionPane.showInputDialog(this, "Enter Movie Title to Remove:");
        if (title == null || title.trim().isEmpty()) return;

        if (movieCollection.removeMovie(title)) {
            JOptionPane.showMessageDialog(this, "Movie removed successfully!");
        } else {
            UITheme.applyErrorTheme(this, "Movie not found!","Error");
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
//Display Method
    private void displayMovies() {
        if (movieCollection.movies.isEmpty()) {
            UITheme.applyErrorTheme(this, "No movies in the collection.","Error");
            return;
        }

        // Build movie list as a formatted string
        StringBuilder movieList = new StringBuilder();
        for (Movie movie : movieCollection.movies.values()) {
            movieList.append(movie.toString()).append("\n\n");
        }

        // Create a JTextArea with scrollable pane
        JTextArea textArea = new JTextArea(movieList.toString());
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);

        // Create a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(500, 400)); // Adjust size as needed

        // Show the movie collection in a scrollable dialog
        JOptionPane.showMessageDialog(this, scrollPane, "Movie Collection", JOptionPane.INFORMATION_MESSAGE);
    }
    //----------------------------------------------------------------------------------------------------------------------
// Method to Update Movie
    private void updateMovie() {
        String title = JOptionPane.showInputDialog(this, "Enter Movie Title to Update:");
        if (title == null || title.trim().isEmpty()) return; // User canceled or empty input

        if (!movieCollection.movies.containsKey(title)) {
            UITheme.applyErrorTheme(this, "Movie not found!","Error");
            return;
        }

        // Field selection with retry if canceled or invalid
        String[] options = {"Title", "Release_Year", "Genre", "Director", "Rating", "Watched_Status"};
        String field;
        do {
            field = (String) JOptionPane.showInputDialog(this, "Select Field to Update:", "Update Movie",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

            if (field == null) return; // User canceled
        } while (field.trim().isEmpty());

        String newValue;
        boolean validUpdate = false;

        do {
            newValue = JOptionPane.showInputDialog(this, "Enter new value for " + field + ":");

            // Loop again to retry
            if (newValue == null || newValue.trim().isEmpty()) {
                UITheme.applyErrorTheme(this, "Invalid input! Please enter a valid value.","Error");
                continue;
            }
//**********************************************************************************************************************
            switch (field.toLowerCase().replace(" ", "")) {

                //Title
                case "title":
                    if (newValue.length() < 1 || newValue.length() > 45) {
                        UITheme.applyErrorTheme(this, "Error: Title must be between 1 and 45 characters.","Error");
                    } else {
                        validUpdate = true;
                    }
                    break;

                //year
                case "release_year":
                    try {
                        int newYear = Integer.parseInt(newValue);
                        if (newYear < 1900 || newYear > 2025) {
                            UITheme.applyErrorTheme(this, "Error: Year must be between 1900 and 2025.","Error");
                        } else {
                            validUpdate = true;
                        }
                    } catch (NumberFormatException e) {
                        UITheme.applyErrorTheme(this, "Invalid input! Please enter a valid year.","Error");
                    }
                    break;
                //genre
                case "genre":
                    String[] validGenres = {"Action", "Crime", "Drama", "Fantasy", "Horror", "Comedy", "Romance",
                            "Science Fiction", "Sports", "Thriller", "Mystery", "War", "Western"};
                    boolean isValidGenre = false;
                    for (String genre : validGenres) {
                        if (newValue.equalsIgnoreCase(genre)) {
                            isValidGenre = true;
                            break;
                        }
                    }
                    if (!isValidGenre) {
                        UITheme.applyErrorTheme(this, "Error: Invalid genre. Please enter a valid genre from the list:" +
                                "\n Action, Crime, Drama, Fantasy, Horror, Comedy, Romance, Science Fiction, Sports, Thriller, Mystery, War, and Western." ,"Error");
                    } else {
                        validUpdate = true;
                    }
                    break;

                //Director
                case "director":
                    if (!newValue.matches("^[a-zA-Z ]+$") || newValue.length() < 2 || newValue.length() > 25) {
                        UITheme.applyErrorTheme(this, "Error: Director name must be 2-25 letters only.","Error");
                    } else {
                        validUpdate = true;
                    }
                    break;

                //Rating
                case "rating":
                    try {
                        float newRating = Float.parseFloat(newValue);
                        if (newRating < 0 || newRating > 100) {
                            UITheme.applyErrorTheme(this, "Error: Rating must be between 0 and 100.","Error");
                        } else {
                            validUpdate = true;
                        }
                    } catch (NumberFormatException e) {
                        UITheme.applyErrorTheme(this, "Invalid input! Please enter a number between 0 and 100.","Error");
                    }
                    break;

                //Watched status
                case "watched_status":
                    if (!newValue.equalsIgnoreCase("true") && !newValue.equalsIgnoreCase("false")) {
                        UITheme.applyErrorTheme(this, "Error: Watched status must be 'true' or 'false'.","Error");
                    } else {
                        validUpdate = true;
                    }
                    break;
//**********************************************************************************************************************
                default:
                    UITheme.applyErrorTheme(this, "Error: Invalid field.","Error");
            }
        }
        // Keep looping until a valid value is entered
        while (!validUpdate);

        // If we get a valid value, proceed with update
        boolean success = movieCollection.updateMovie(title, field.toLowerCase().replace(" ", ""), newValue);
        if (success) {
            JOptionPane.showMessageDialog(this, "Movie updated successfully!");
        } else {
            UITheme.applyErrorTheme(this, "Failed to update movie.","Error");
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    // Method to Calculate Average Rating
    private void calculateAverageRating() {
        float avg = movieCollection.calculateAverageRating();
        JOptionPane.showMessageDialog(this, "Average Movie Rating: " + avg);
    }
    //----------------------------------------------------------------------------------------------------------------------
    // Main Method to begin the program and Run GUI
    public static void main(String[] args) {
        new MovieFrame();
    }


}
