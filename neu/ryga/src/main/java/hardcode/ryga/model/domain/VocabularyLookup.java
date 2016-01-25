package hardcode.ryga.model.domain;

import java.sql.SQLException;
import java.util.List;

public interface VocabularyLookup {
	
	/**
	 * Queries the DB for vocables starting with the query string
	 * @param query
	 * @return
	 * @throws SQLException
	 */
	public List<Vocable> query(String query) throws SQLException;

}
