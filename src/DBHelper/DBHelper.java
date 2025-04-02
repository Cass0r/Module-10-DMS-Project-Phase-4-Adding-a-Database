package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

//This class was created by the code generator class to make methods to aid in the connection to the SQLite database
/**
 * The {@code DBHelper} class provides utility methods for interacting with the SQLite database.
 * <p>
 * This class is responsible for establishing a connection to the SQLite database, executing queries,
 * and retrieving results. It includes database connection variables for managing the connection, statement,
 * and result set. The class ensures that the application can interact with the database to perform operations
 * such as adding, removing, and retrieving movie data.
 * </p>
 *
 * <p>
 * The class includes the following key components:
 * <ul>
 *     <li>{@link #DATABASE_NAME} - Path to the SQLite database file for practice.</li>
 *     <li>{@link #connection} - Connection object for establishing a connection to the database.</li>
 *     <li>{@link #statement} - Statement object for executing SQL queries.</li>
 *     <li>{@link #resultSet} - ResultSet object for holding the data returned by queries.</li>
 * </ul>
 *
 *
 * @see java.sql.Connection
 * @see java.sql.Statement
 * @see java.sql.ResultSet
 */
public class DBHelper {
	/**SQLite database file path, was mainly used for quick testing before changing operations. */
	// SQLite database file path
	private final String DATABASE_NAME = "C:\\sqlite\\MovieCollectionDatabase.db";

	// Database connection variables
	/**represents the connection to the SQLite database, It is used to establish and manage communication between the Java application and the database. */
	private Connection connection;
	/**is used to execute SQL queries against the database, It helps in sending SQL commands to the database and retrieving results. */
	private Statement statement;
	/**holds the results of a query executed via the {@link #statement}, It provides access to the rows and columns of data returned by the query. */
	private ResultSet resultSet;

//----------------------------------------------------------------------------------------------------------------------
	// Constructor: Initializes connection variables to null
	/**Constructor that initializes the connection variables to null, This is used to set up the DBHelper object for subsequent database interactions.*/
	public DBHelper() {
		connection = null;
		statement = null;
		resultSet = null;
	}
//----------------------------------------------------------------------------------------------------------------------
	// Establishes connection to the SQLite database
	/**
	 * Establishes a connection to the SQLite database.
	 * This method loads the JDBC driver for SQLite, establishes a connection to the database file,
	 * and initializes a statement object for executing queries.
	 */
	private void connect() {
		try {
			// Load the JDBC driver for SQLite
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e) {
			// Print the stack trace if the driver is not found
			e.printStackTrace();
		}
		try {
			// Establish a connection to the SQLite database
			connection = DriverManager.getConnection("jdbc:sqlite:" + DATABASE_NAME);
			statement = connection.createStatement();
		} catch (SQLException e) {
			// Print the stack trace if connection fails
			e.printStackTrace();
		}
	}

//----------------------------------------------------------------------------------------------------------------------
	// Closes the database connection and releases resources
	/**
	 * Closes the database connection and releases any associated resources.
	 * This method ensures that the database connection, statement, and result set are properly closed to avoid resource leaks.
	 */
	private void close() {
		try {
			connection.close();
			statement.close();
			if (resultSet != null)
			resultSet.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

//----------------------------------------------------------------------------------------------------------------------
	// Converts an ArrayList of ArrayLists into a 2D Object array
	/**
	 * Converts an ArrayList of ArrayLists into a 2D Object array.
	 * This utility method is used to transform data retrieved from the database into a format suitable for table display.
	 *
	 * @param list the ArrayList of ArrayLists to be converted
	 * @return a 2D Object array representing the data
	 */
	private Object[][] arrayListTo2DArray(ArrayList<ArrayList<Object>> list) {
		// Initialize a 2D array with the size of the list
		Object[][] array = new Object[list.size()][];
		for (int i = 0; i < list.size(); i++) {
			// Get each row (ArrayList) from the list
			ArrayList<Object> row = list.get(i);
			array[i] = row.toArray(new Object[row.size()]);
		}
		// Return the 2D Object array
		return array;
	}

//----------------------------------------------------------------------------------------------------------------------
	// Executes SQL commands that do not return a result set (CRUD operations)
	/**
	 * Executes a SQL command that does not return a result set, such as CRUD operations.
	 * This method connects to the database, executes the provided SQL command, and closes the connection.
	 *
	 * @param sql the SQL command to be executed
	 */
	protected void execute(String sql) {
		try {
			// Connect to the database
			connect();
			// Execute the SQL command
			statement.execute(sql);
		} catch (SQLException e) {
			// Print the stack trace if execution fails
			e.printStackTrace();
		}
		finally {
			// Close the database connection and resources
			close();
		}
	}

//----------------------------------------------------------------------------------------------------------------------
	// Executes a SQL query and returns the result as a DefaultTableModel (for GUI display)
	/**
	 * Executes a SQL query and returns the result as a DefaultTableModel for displaying in a GUI.
	 * This method retrieves the data from the result set, formats it as a table model, and returns it for further use.
	 *
	 * @param sql the SQL query to be executed
	 * @return a DefaultTableModel containing the query results for GUI display
	 */
	protected DefaultTableModel executeQueryToTable(String sql) {
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		ArrayList<Object> columns = new ArrayList<Object>();
		connect();
		try {
			resultSet = statement.executeQuery(sql);
			// Retrieve column names
			int columnCount = resultSet.getMetaData().getColumnCount();
			// Retrieve column names and store them in the columns list
			for (int i = 1; i <= columnCount; i++)
			columns.add(resultSet.getMetaData().getColumnName(i));
			// Retrieve the row data and store each row in the result list
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++)
				subresult.add(resultSet.getObject(i));
				// Add the row data to the result list
				result.add(subresult);
			}
		} catch (SQLException e) {
			// Print the stack trace if query execution fails
			e.printStackTrace();
		}
		close();
		// Convert the ArrayList result into a table model
		// Convert the ArrayList result into a table model and return it
		return new DefaultTableModel(arrayListTo2DArray(result), columns.toArray());
	}

//----------------------------------------------------------------------------------------------------------------------
	// Executes a SQL query and returns the result as an ArrayList of ArrayLists
	/**
	 * Executes a SQL query and returns the result as an ArrayList of ArrayLists.
	 * This method retrieves data from the result set and stores it in an ArrayList for further processing.
	 *
	 * @param sql the SQL query to be executed
	 * @return an ArrayList of ArrayLists containing the query results
	 */
	protected ArrayList<ArrayList<Object>> executeQuery(String sql) {
		// Initialize a list to store the result rows
		ArrayList<ArrayList<Object>> result = new ArrayList<ArrayList<Object>>();
		// Connect to the database
		connect();
		try {
			// Execute the query and store the result in resultSet
			resultSet = statement.executeQuery(sql);
			int columnCount = resultSet.getMetaData().getColumnCount();
			// Retrieve row data and store it in ArrayList
			while (resultSet.next()) {
				ArrayList<Object> subresult = new ArrayList<Object>();
				for (int i = 1; i <= columnCount; i++) {
					subresult.add(resultSet.getObject(i));
				}
				// Add the row data to the result list
				result.add(subresult);
			}
		} catch (SQLException e){
			// Print the stack trace if query execution fails
			e.printStackTrace();
		}
		// Close the database connection and resources
		close();
		// Return the list of rows (ArrayList of ArrayLists)
		return result;
	}
}//class