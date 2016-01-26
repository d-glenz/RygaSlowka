package hardcode.ryga.model.db.sql;

import java.sql.SQLException;

public class SQLDatabaseUtilities {
	
	/**
	 * Prints details of an SQLException chain to <code>System.err</code>.
	 * Details included are SQL State, Error code, Exception message.
	 *
	 * @param e
	 *            the SQLException from which to print details.
	 */
	public static void printSQLException(SQLException e) {
		// Unwraps the entire exception chain to unveil the real cause of the
		// Exception.
		while (e != null) {
			System.err.println("\n----- SQLException -----");
			System.err.println("  SQL State:  " + e.getSQLState());
			System.err.println("  Error Code: " + e.getErrorCode());
			System.err.println("  Message:    " + e.getMessage());
			// for stack traces, refer to derby.log or uncomment this:
			System.err.println("----- Trace -----");
			e./**
			 * Remove database related files recursively
			 * @param dir database base directory
			 */printStackTrace(System.err);
			e = e.getNextException();
		}
	}
	
	public static String prettifySQLException(SQLException e) {
		StringBuffer sb = new StringBuffer();
		while (e != null) {
			sb.append("----- SQLException -----")/*.append("\n")*/;
			sb.append("  SQL State:  ").append(e.getSQLState())/*.append("\n")*/;
			sb.append("  Error Code: ").append(e.getErrorCode())/*.append("\n")*/;
			sb.append("  Message:    ").append(e.getMessage())/*.append("\n")*/;
			e = e.getNextException();
		}
		System.out.println(sb.toString());
		return sb.toString();
		
	}

}
