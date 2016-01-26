package hardcode.ryga.model.db;

import hardcode.ryga.model.domain.Connection;
import hardcode.ryga.model.domain.Vocable;
import hardcode.ryga.model.domain.Vocabulary;
import hardcode.ryga.model.domain.VocabularyLookup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FakeDatabase implements Vocabulary, VocabularyLookup {
	
	private List<Vocable> vocables;

	public FakeDatabase(String databaseName) throws SQLException {
		super();
		vocables = new ArrayList<>();
	}

	@Override
	public Vocable createNewVocable() {
		return new Vocable();
	}

	@Override
	public void addVocable(Vocable vocable) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("Vokabel "+vocable.getWord()+" hinzugefügt");
		vocables.add(vocable);
	}

	@Override
	public void updateVocable(Vocable vocable) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Vocable> query(String query) throws SQLException {
		// TODO Auto-generated method stub
		//return new ArrayList<>();
		return vocables;
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

}
