package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
//This class was created by the code generator class to make methods to aid with SQL queries in the connection to the SQLite database
/**
 * The {@code Movies} class is responsible for interacting with the "Movies" table in the SQLite database.
 * <p>
 * This class extends {@link DBHelper} to utilize the database connection, and provides methods for managing movie records
 * in the database, including performing operations such as adding, updating, removing, and retrieving movies.
 * </p>
 *
 * <p>
 * It defines constants for the table name and field names, which are used to reference columns in the "Movies" table:
 * <ul>
 *     <li>{@link #TABLE_NAME} - The name of the table in the database.
 *     <li>{@link #ID} - The field name for the movie ID.
 *     <li>{@link #Title} - The field name for the movie title.
 *     <li>{@link #Release_Year} - The field name for the movie release year.
 *     <li>{@link #Genre} - The field name for the movie genre.
 *     <li>{@link #Director} - The field name for the movie director.
 *     <li>{@link #Rating} - The field name for the movie rating.
 *     <li>{@link #Watched_Status} - The field name for the movie watched status.
 * </ul>
 *
 *
 * <p>
 * The {@code Movies} class is crucial for the interaction with the database layer to persist movie data and retrieve it
 * as required by the application.
 * </p>
 *
 * <p>
 * Note: This class does not require a constructor.
 * @see DBHelper
 *
 * <p>
 * DBHelper
 */
public class Movies extends DBHelper {
	// Table name and field names constants
	/** Table name and field names constants*/
	private final String TABLE_NAME = "Movies";
	/** The field name for the movie ID. */
	public static final String ID = "ID";
	/** The field name for the movie title. */
	public static final String Title = "Title";
	/** The field name for the movie release year. */
	public static final String Release_Year = "Release_Year";
	/** The field name for the movie genre. */
	public static final String Genre = "Genre";
	/** The field name for the movie director. */
	public static final String Director = "Director";
	/** The field name for the movie rating. */
	public static final String Rating = "Rating";
	/**
	 * The field name for the movie watched status.
	 * This constant is used to reference the "Watched_Status" column in the "Movies" table.
	 */
	public static final String Watched_Status = "Watched_Status";

//----------------------------------------------------------------------------------------------------------------------
	// Add ORDER BY clause if sorting is applied, Add WHERE clause if filtering is applied, and Select specific fields or all fields
	/**
	 * Prepares an SQL query string with optional filtering, sorting, and field selection.
	 * <p>
	 * This method constructs the SQL SELECT statement by adding clauses for the fields to be selected, a WHERE clause (if filtering),
	 * and an ORDER BY clause (if sorting). It is used as a helper method for generating SQL queries in other methods.
	 * </p>
	 *
	 * @param fields the fields to select from the database (or null for all fields)
	 * @param whatField the field to filter by (or null for no filtering)
	 * @param whatValue the value to filter by (or null for no filtering)
	 * @param sortField the field to sort by (or null for no sorting)
	 * @param sort the sort order (either "ASC" or "DESC")
	 * @return the formatted SQL query string
	 */
	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

//----------------------------------------------------------------------------------------------------------------------
	//Inserts a new movie record into the database.
	// Wrap string values with quotes for SQL syntax
	/**
	 * Inserts a new movie record into the Movies table.
	 * <p>
	 * This method inserts a new record into the Movies table by constructing an SQL INSERT statement with the provided values for ID, Title,
	 * Release Year, Genre, Director, Rating, and Watched Status. It ensures that the string values are properly formatted for SQL syntax.
	 * </p>
	 *
	 * @param ID the unique identifier of the movie
	 * @param Title the title of the movie
	 * @param Release_Year the release year of the movie
	 * @param Genre the genre of the movie
	 * @param Director the director of the movie
	 * @param Rating the rating of the movie
	 * @param Watched_Status the watched status of the movie (true/false)
	 */
	public void insert(Integer ID, String Title, Integer Release_Year, String Genre, String Director, Double Rating, Boolean Watched_Status) {
		Title = Title != null ? "\"" + Title + "\"" : null;
		Genre = Genre != null ? "\"" + Genre + "\"" : null;
		Director = Director != null ? "\"" + Director + "\"" : null;

		// Arrays to hold field names and values
		Object[] values_ar = {ID, Title, Release_Year, Genre, Director, Rating, Watched_Status};
		String[] fields_ar = {Movies.ID, Movies.Title, Movies.Release_Year, Movies.Genre, Movies.Director, Movies.Rating, Movies.Watched_Status};
		String values = "", fields = "";
		// Build the values and fields strings for the SQL query
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}
		// Remove trailing commas and execute the SQL insert query
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			//// Execute the SQL insert query
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}

//----------------------------------------------------------------------------------------------------------------------
	//Deletes movie records based on a field and its value.
	/**
	 * Deletes movie records from the Movies table based on a specified field and value.
	 * <p>
	 * This method constructs a DELETE SQL statement to remove records that match the specified condition (field and value).
	 * </p>
	 *
	 * @param whatField the field to filter by
	 * @param whatValue the value to filter by
	 */
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

//----------------------------------------------------------------------------------------------------------------------
	// Updates movie records based on specified conditions.
	/**
	 * Updates a movie record in the Movies table based on a specified field and value.
	 * <p>
	 * This method constructs an UPDATE SQL statement to modify a movie record that matches the specified condition (whereField and whereValue).
	 * </p>
	 *
	 * @param whatField the field to update
	 * @param whatValue the new value to set for the field
	 * @param whereField the field to filter by (to identify the record to update)
	 * @param whereValue the value to filter by (to identify the record to update)
	 */
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

//----------------------------------------------------------------------------------------------------------------------
	// Selects movie records with optional filtering and sorting.
	/**
	 * Selects movie records from the Movies table with optional filtering and sorting.
	 * <p>
	 * This method constructs an SQL SELECT statement using the provided parameters, executes the query, and returns the results as an ArrayList
	 * of ArrayLists, where each inner ArrayList represents a row in the result set.
	 * </p>
	 *
	 * @param fields the fields to select (or null for all fields)
	 * @param whatField the field to filter by (or null for no filtering)
	 * @param whatValue the value to filter by (or null for no filtering)
	 * @param sortField the field to sort by (or null for no sorting)
	 * @param sort the sort order (either "ASC" or "DESC")
	 * @return an ArrayList of ArrayLists containing the query results
	 */
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

//----------------------------------------------------------------------------------------------------------------------
//Executes a custom SQL query and returns the result as an ArrayList.
	/**
	 * Executes a custom SQL query and returns the result as an ArrayList.
	 * <p>
	 * This method is used to execute arbitrary SQL queries that may not necessarily be related to the Movies table, returning the results as
	 * an ArrayList of ArrayLists.
	 * </p>
	 *
	 * @param query the custom SQL query to execute
	 * @return an ArrayList of ArrayLists containing the query results
	 */
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

//----------------------------------------------------------------------------------------------------------------------
	//Executes a custom SQL query without returning results.
	/**
	 * Executes a custom SQL query without returning results.
	 * <p>
	 * This method is used to execute arbitrary SQL queries that do not return data, such as INSERT, UPDATE, or DELETE operations.
	 * </p>
	 *
	 * @param query the custom SQL query to execute
	 */
	public void execute(String query) {
		super.execute(query);
	}

//----------------------------------------------------------------------------------------------------------------------
	//Selects movie records and returns them as a DefaultTableModel for GUI display.
	/**
	 * Selects movie records from the Movies table and returns the result as a DefaultTableModel for GUI display.
	 * <p>
	 * This method constructs a SELECT SQL statement with optional filtering and sorting, executes the query, and returns the results in a
	 * DefaultTableModel, which can be used directly in GUI components like JTable.
	 * </p>
	 *
	 * @param fields the fields to select (or null for all fields)
	 * @param whatField the field to filter by (or null for no filtering)
	 * @param whatValue the value to filter by (or null for no filtering)
	 * @param sortField the field to sort by (or null for no sorting)
	 * @param sort the sort order (either "ASC" or "DESC")
	 * @return a DefaultTableModel containing the query results
	 */
	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}
}//class