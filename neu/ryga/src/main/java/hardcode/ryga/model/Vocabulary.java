package hardcode.ryga.model;

import hardcode.vortaro.db.Connection;

import java.sql.SQLException;
import java.util.List;

public interface Vocabulary {
		
	/**
	 * Creates and returns a new, empty Vocable.
	 * @return
	 */
	public Vocable createNewVocable();
	
	/**
	 * Adds the new Vocable to the databse.
	 * @param vocable
	 * @throws SQLException
	 */
	public void addVocable(Vocable vocable) throws SQLException;

	/**
	 * If the has bee changed, it is updated in the database. 
	 */
	public void updateVocable(Vocable vocable) throws SQLException;
	
	/**
	 * Queries the DB for vocables starting with the query string
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public List<Vocable> query(String query) throws SQLException;
	
	public List<String> getConnectionTableNames();
	
	public List<String> getConnectionTableNamesWithTranslation();
	
	public List<Vocable> getConnectionsFor(Vocable vocable, String connectionTableName) throws SQLException;
	
	public List<Connection> getAllConnectionsFor(Vocable vocable) throws SQLException;
	
	public List<Vocable> getTranslations(Vocable vocable) throws SQLException;
	
	public void addConnectionTable(String connectionTableName) throws SQLException;
	
	public void addConnection (Vocable from, Vocable to, String connectionTableName) throws SQLException;
}
