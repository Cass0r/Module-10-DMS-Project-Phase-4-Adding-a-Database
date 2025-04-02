/**
 * The Movie class represents a movie with attributes such as title, release year, genre,
 * director, rating, and watched status. This class is used to store and manage movie information
 * for a collection, enabling easy tracking of movie details.
 *
 * Role in the system: The Movie class is primarily used to store and retrieve movie data,
 * and it can be part of a larger system that tracks or manages movies.
 *
 * Usage example:
 * <pre>
 * Movie myMovie = new Movie("Inception", 2010, "Sci-Fi", "Christopher Nolan", 8.8f, false);
 * System.out.println(myMovie.toString());
 * </pre>
 */

public class Movie {
    //These are the Variables used for the Patrons information
    /**
     * The title of the movie.
     */
    private String title;
    /**
     * The year the movie was released.
     */
    private int Release_Year;
    /**
     * The genre of the movie (e.g., Action, Drama, Comedy).
     */
    private String Genre;
    /**
     * The director of the movie.
     */
    private String Director;
    /**
     * The rating of the movie, typically on a scale of 0 to 10.
     */
    private float Rating;
    /**
     * The watched status of the movie.
     * True if the movie has been watched, false otherwise.
     */
    private boolean Watched_Status;
//----------------------------------------------------------------------------------------------------------------------
    //Constructor for how information will be inputted
    /**
     * Constructs a Movie object with the specified details.
     * @param title the title of the movie
     * @param Release_Year the release year of the movie
     * @param Genre the genre of the movie
     * @param Director the director of the movie
     * @param Rating the rating of the movie (on a scale of 1 to 100)
     * @param Watched_Status indicates if the movie has been watched (true/false)
     *
     */
    public Movie(String title, int Release_Year, String Genre, String Director, float Rating, boolean Watched_Status){
        this.title = title;
        this.Release_Year = Release_Year;
        this.Genre = Genre;
        this.Director = Director;
        this.Rating = Rating;
        this.Watched_Status = Watched_Status;
    }

//----------------------------------------------------------------------------------------------------------------------
    //Getters is a method used to retrieve the value of a private field in a class. It allows controlled access to an
    //object's data, while keeping the field itself private
    /**
     * Gets the title of the movie.
     * @return the title of the movie
     */
    public String getTitle() {
        return title;
    }

    /**
     * Gets the release year of the movie.
     * @return the release year of the movie
     */
    public int getRelease_Year() {
        return Release_Year;
    }

    /**
     * Gets the genre of the movie.
     * @return the genre of the movie
     */
    public String getGenre() {
        return Genre;
    }

    /**
     * Gets the director of the movie.
     * @return the director of the movie
     */
    public String getDirector() {
        return Director;
    }

    /**
     * Gets the rating of the movie.
     * @return the rating of the movie
     */
    public float getRating() {
        return Rating;
    }

    /**
     * Gets the watched status of the movie.
     * @return true if the movie has been watched, false otherwise
     */
    public boolean getWatched_Status() {
        return Watched_Status;
    }

//----------------------------------------------------------------------------------------------------------------------
    //Setters is a method used to set or update the value of a private field in a class. It allows controlled
    //modification of an object's data while keeping the field itself private.
    /**
     * Sets the title of the movie.
     * @param title the title of the movie
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Sets the release year of the movie.
     * @param release_Year the release year of the movie
     */
    public void setRelease_Year(int release_Year) {
        Release_Year = release_Year;
    }

    /**
     * Sets the genre of the movie.
     * @param genre the genre of the movie
     */
    public void setGenre(String genre) {
        Genre = genre;
    }

    /**
     * Sets the director of the movie.
     * @param director the director of the movie
     */
    public void setDirector(String director) {
        Director = director;
    }

    /**
     * Sets the rating of the movie.
     * @param rating the rating of the movie
     */
    public void setRating(float rating) {
        Rating = rating;
    }

    /**
     * Sets the watched status of the movie.
     * @param watched_Status the watched status of the movie (true/false)
     */
    public void setWatched_Status(boolean watched_Status) {
        Watched_Status = watched_Status;
    }

//----------------------------------------------------------------------------------------------------------------------
    //Display Movie Info method in Java is used to provide a string representation of an object. It's defined in the
    //Object class and can be overridden in custom classes to return a more meaningful or readable string representation
    //of the object.
    /**
     * Returns a string representation of the movie's information.
     * @return a string with the movie's title, release year, genre, director, rating, and watched status. A string that provides a summary of the movie's details
     */
    public String toString(){
        return "=================Movie Info===============" +
                "\nMovie Title: " + title +
                "\nRelease Year: " + Release_Year +
                "\nGenre: " + Genre +
                "\nDirector: " + Director +
                "\nRating: " + Rating +
                "\nWatched Status: " + Watched_Status +
                "\n==========================================";
    }
}

