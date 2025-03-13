public class Movie {
    //These are the Variables used for the Patrons information
    private String title;
    private int Release_Year;
    private String Genre;
    private String Director;
    private float Rating;
    private boolean Watched_Status;

    //Constructor for how information will be inputted
    public Movie(String title, int Release_Year, String Genre, String Director, float Rating, boolean Watched_Status){
        this.title = title;
        this.Release_Year = Release_Year;
        this.Genre = Genre;
        this.Director = Director;
        this.Rating = Rating;
        this.Watched_Status = Watched_Status;
    }

    //Getters
    public String getTitle() {
        return title;
    }

    public int getRelease_Year() {
        return Release_Year;
    }

    public String getGenre() {
        return Genre;
    }

    public String getDirector() {
        return Director;
    }

    public float getRating() {
        return Rating;
    }

    public boolean getWatched_Status() {
        return Watched_Status;
    }

    //Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setRelease_Year(int release_Year) {
        Release_Year = release_Year;
    }

    public void setGenre(String genre) {
        Genre = genre;
    }

    public void setDirector(String director) {
        Director = director;
    }

    public void setRating(float rating) {
        Rating = rating;
    }

    public void setWatched_Status(boolean watched_Status) {
        Watched_Status = watched_Status;
    }

    //Display Movie Info
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

