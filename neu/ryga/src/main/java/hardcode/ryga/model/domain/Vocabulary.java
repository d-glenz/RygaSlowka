package hardcode.ryga.model.domain;

import hardcode.ryga.model.db.DatabaseException;

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
	public void addVocable(Vocable vocable) throws DatabaseException;

	/**
	 * If the has bee changed, it is updated in the database. 
	 */
	public void updateVocable(Vocable vocable) throws DatabaseException;
	
	/**
	 * Queries the DB for vocables starting with the query string
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public List<Vocable> query(String query) throws DatabaseException;
	
	public List<String> getConnectionTableNames();
	
	public List<String> getConnectionTableNamesWithTranslation();
	
	public List<Vocable> getConnectionsFor(Vocable vocable, String connectionTableName) throws DatabaseException;
	
	public List<Connection> getAllConnectionsFor(Vocable vocable) throws DatabaseException;
	
	public List<Vocable> getTranslations(Vocable vocable) throws DatabaseException;
	
	public void addConnectionTable(String connectionTableName) throws DatabaseException;
	
	public void addConnection (Vocable from, Vocable to, String connectionTableName) throws DatabaseException;
}
