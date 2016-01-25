package hardcode.vortaro.db;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public interface Database {
	
	void connectToDatabase() throws SQLException;

	void createDatabase(Properties props) throws SQLException;

	/**
	 * Remove database related files recursively
	 * @param dir database base directory
	 */
	void removeDatabaseFromDisk(File dir);
	


	void initDatabaseInternal() throws SQLException;
	
	void executeStatement(String statement) throws SQLException;

	/**
	 * 
	 * @return the SQL create statement
	 */
	String initDatabase() throws SQLException;

	/**
	 * For inserting and updating<br>
	 * Close resource afterwards!<br>
	 * insert into location values (?, ?)<br>
	 * update location set num=?, addr=? where num=?<br>
	 * psInsert.setInt(1, 1956), psInsert.setString(2, "Webster St."),
	 * psInsert.executeUpdate();
	 * 
	 * @param sql
	 * @return
	 * @throws SQLException
	 */
	PreparedStatement prepareStatement(String sql) throws SQLException;

	/**
	 * Delivers the results for a query
	 * @param sqlQuery
	 * @return ResultSet with query results
	 * @throws SQLException
	 */
	public ResultSet select(String sqlQuery) throws SQLException;
	
	void dropTable(String tableName) throws SQLException;

	void shutdownDatabase() throws SQLException;

}
