package hardcode.ryga.model.db.sql;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public abstract class DerbySQLDatabase  {
	private String protocol = "jdbc:derby:";
	private String databaseName;
	private Connection conn;
//	private boolean autoCommit; //TODO
	private Properties properties = null; //TODO
	
	public DerbySQLDatabase(String databaseName) throws SQLException {
		this.databaseName = databaseName;
		conn = null;
		databaseName = null;
//		autoCommit = false;
		connectToDatabase(); //autocommit true
	}

	public void connectToDatabase() throws SQLException {
		/*
		 * This connection specifies create=true in the connection URL to cause
		 * the database to be created when connecting for the first time. To
		 * remove the database, remove the directory derbyDB (the same as the
		 * database name) and its contents.
		 * 
		 * The directory derbyDB will be created under the directory that the
		 * system property derby.system.home points to, or the current directory
		 * (user.dir) if derby.system.home is not set.
		 */


		try {
			if (properties == null) {
				conn = DriverManager.getConnection(protocol + databaseName);
			} else {
				conn = DriverManager.getConnection(protocol + databaseName,	properties);
			}
		} catch (SQLException sqle) {
			// If db does not exist, create it!
			if (sqle.getSQLState().equalsIgnoreCase("XJ004")) { 
				System.out.println("Database '" + databaseName + "' does not exist. Creating database...");
				createDatabase(properties);
			} else {
				throw sqle;
			}
		}

		//TODO
		// If we want to control transactions manually. Autocommit is on by
		// default in JDBC.
//		this.autoCommit = autocommit;
//		conn.setAutoCommit(autocommit);
	}

	public void createDatabase(Properties props) throws SQLException {
		
		if (props == null) {
			conn = DriverManager.getConnection(protocol + databaseName + ";create=true");
		} else {
			conn = DriverManager.getConnection(protocol + databaseName + ";create=true", props);
		}
		
		try {
			initDatabaseInternal();
		} catch (SQLException e) {
			shutdownDatabase();
			
			File dir = new File(System.getProperty("user.dir") + File.separator + databaseName);
			removeDatabaseFromDisk(dir);
			
			throw e;
		}
	}

	/**
	 * Remove database related files recursively
	 * @param dir database base directory
	 */
	public void removeDatabaseFromDisk(File dir) {
		if(dir.isDirectory()) {
			File[] files = dir.listFiles();
			for(File f : files) {
				removeDatabaseFromDisk(f);
			}
			dir.delete();
		} else {
			dir.delete();
		}
	}
	


	@SuppressWarnings("unused")
  public void initDatabaseInternal() throws SQLException {
		// TODO; check things, bla bla
		Statement executableStatement = conn.createStatement();
//		String createStatement = 
				initDatabase();
//		executableStatement.execute(createStatement);
	}
	
	public void executeStatement(String statement) throws SQLException {
		Statement executableStatement = conn.createStatement();
		executableStatement.execute(statement);
	}

	/**
	 * 
	 * @return the SQL create statement
	 */
	public abstract String initDatabase() throws SQLException;

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
	public PreparedStatement prepareStatement(String sql)
			throws SQLException {
		return conn.prepareStatement(sql);
	}

	/**
	 * Delivers the results for a query
	 * @param sqlQuery
	 * @return ResultSet with query results
	 * @throws SQLException
	 */
	public ResultSet select(String sqlQuery) throws SQLException { //FIXME: mache protected!
		Statement s = conn.createStatement();
		ResultSet rs = s.executeQuery(sqlQuery);
		return rs;
	}
	
	public void dropTable(String tableName) throws SQLException {
		PreparedStatement ps = conn.prepareStatement("drop table ?");
		ps.setString(1, tableName);
		ps.execute();
		System.out.println("Dropped table location");
	}

	public void shutdownDatabase() throws SQLException {
		try {
			// the shutdown=true attribute shuts down Derby
			DriverManager.getConnection(protocol + ";shutdown=true");

		} catch (SQLException se) {
			if (((se.getErrorCode() == 50000) && ("XJ015".equals(se.getSQLState())))) {
				// we got the expected exception
				System.out.println("Derby shut down normally");

			} else {
				System.err.println("Derby did not shut down normally");
				throw se;
			}
		}

		if (conn != null) {
			conn.close();
			conn = null;
		}

		// To shut down a specific database only, but keep the //TODO
		// engine running (for example for connecting to other
		// databases), specify a database in the connection URL:
		// DriverManager.getConnection("jdbc:derby:" + dbName +
		// ";shutdown=true");
		// Note that for single database shutdown, the expected
		// SQL state is "08006", and the error code is 45000.
	}


}
