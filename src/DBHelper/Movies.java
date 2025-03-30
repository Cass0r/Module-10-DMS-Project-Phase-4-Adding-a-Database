package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
//This class was created by the code generator class to make methods to aid with SQL queries in the connection to the SQLite database
public class Movies extends DBHelper {
	// Table name and field names constants
	private final String TABLE_NAME = "Movies";
	public static final String ID = "ID";
	public static final String Title = "Title";
	public static final String Release_Year = "Release_Year";
	public static final String Genre = "Genre";
	public static final String Director = "Director";
	public static final String Rating = "Rating";
	public static final String Watched_Status = "Watched_Status";

	// Add ORDER BY clause if sorting is applied, Add WHERE clause if filtering is applied, and Select specific fields or all fields
	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

	//Inserts a new movie record into the database.
	// Wrap string values with quotes for SQL syntax
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

	//Deletes movie records based on a field and its value.
	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

	// Updates movie records based on specified conditions.
	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	// Selects movie records with optional filtering and sorting.
	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}
//Executes a custom SQL query and returns the result as an ArrayList.
	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	//Executes a custom SQL query without returning results.
	public void execute(String query) {
		super.execute(query);
	}

	//Selects movie records and returns them as a DefaultTableModel for GUI display.
	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}