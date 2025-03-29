package DBHelper;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class Movies extends DBHelper {
	private final String TABLE_NAME = "Movies";
	public static final String ID = "ID";
	public static final String Title = "Title";
	public static final String Release_Year = "Release_Year";
	public static final String Genre = "Genre";
	public static final String Director = "Director";
	public static final String Rating = "Rating";
	public static final String Watched_Status = "Watched_Status";

	private String prepareSQL(String fields, String whatField, String whatValue, String sortField, String sort) {
		String query = "SELECT ";
		query += fields == null ? " * FROM " + TABLE_NAME : fields + " FROM " + TABLE_NAME;
		query += whatField != null && whatValue != null ? " WHERE " + whatField + " = \"" + whatValue + "\"" : "";
		query += sort != null && sortField != null ? " order by " + sortField + " " + sort : "";
		return query;
	}

	public void insert(Integer ID, String Title, Integer Release_Year, String Genre, String Director, Double Rating, Boolean Watched_Status) {
		Title = Title != null ? "\"" + Title + "\"" : null;
		Genre = Genre != null ? "\"" + Genre + "\"" : null;
		Director = Director != null ? "\"" + Director + "\"" : null;
		
		Object[] values_ar = {ID, Title, Release_Year, Genre, Director, Rating, Watched_Status};
		String[] fields_ar = {Movies.ID, Movies.Title, Movies.Release_Year, Movies.Genre, Movies.Director, Movies.Rating, Movies.Watched_Status};
		String values = "", fields = "";
		for (int i = 0; i < values_ar.length; i++) {
			if (values_ar[i] != null) {
				values += values_ar[i] + ", ";
				fields += fields_ar[i] + ", ";
			}
		}
		if (!values.isEmpty()) {
			values = values.substring(0, values.length() - 2);
			fields = fields.substring(0, fields.length() - 2);
			super.execute("INSERT INTO " + TABLE_NAME + "(" + fields + ") values(" + values + ");");
		}
	}

	public void delete(String whatField, String whatValue) {
		super.execute("DELETE from " + TABLE_NAME + " where " + whatField + " = " + whatValue + ";");
	}

	public void update(String whatField, String whatValue, String whereField, String whereValue) {
		super.execute("UPDATE " + TABLE_NAME + " set " + whatField + " = \"" + whatValue + "\" where " + whereField + " = \"" + whereValue + "\";");
	}

	public ArrayList<ArrayList<Object>> select(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQuery(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

	public ArrayList<ArrayList<Object>> getExecuteResult(String query) {
		return super.executeQuery(query);
	}

	public void execute(String query) {
		super.execute(query);
	}

	public DefaultTableModel selectToTable(String fields, String whatField, String whatValue, String sortField, String sort) {
		return super.executeQueryToTable(prepareSQL(fields, whatField, whatValue, sortField, sort));
	}

}