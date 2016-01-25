package hardcode.vortaro.db;

import hardcode.ryga.model.Vocable;
import hardcode.ryga.model.Vocabulary;
import hardcode.ryga.model.VocabularyLookup;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class FakeDatabase implements Database, Vocabulary, VocabularyLookup{

	public FakeDatabase(String databaseName) throws SQLException {
		super();
	}

	@Override
	public Vocable createNewVocable() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addVocable(Vocable vocable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateVocable(Vocable vocable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Vocable> query(String query) throws SQLException {
		return new ArrayList<>();
	}

	@Override
	public List<String> getConnectionTableNames() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getConnectionTableNamesWithTranslation() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vocable> getConnectionsFor(Vocable vocable,
			String connectionTableName) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Connection> getAllConnectionsFor(Vocable vocable)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vocable> getTranslations(Vocable vocable) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConnectionTable(String connectionTableName)
			throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addConnection(Vocable from, Vocable to,
			String connectionTableName) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String initDatabase() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void connectToDatabase() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void createDatabase(Properties props) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeDatabaseFromDisk(File dir) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initDatabaseInternal() throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void executeStatement(String statement) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResultSet select(String sqlQuery) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dropTable(String tableName) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void shutdownDatabase() throws SQLException {
		// TODO Auto-generated method stub
		
	}

}
