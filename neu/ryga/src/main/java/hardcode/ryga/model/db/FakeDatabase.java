package hardcode.ryga.model.db;

import hardcode.ryga.model.domain.Connection;
import hardcode.ryga.model.domain.Vocable;
import hardcode.ryga.model.domain.Vocabulary;
import hardcode.ryga.model.domain.VocabularyLookup;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FakeDatabase implements Vocabulary, VocabularyLookup {
	
	//private List<Vocable> vocables;
	private Map<String, Vocable> vocables;

	public FakeDatabase(String databaseName) throws SQLException {
		super();
		vocables = new HashMap<>();
	}

	@Override
	public Vocable createNewVocable() {
		return new Vocable();
	}

	@Override
	public void addVocable(Vocable vocable) throws DatabaseException {
		// TODO Auto-generated method stub
		//System.out.println("Vokabel "+vocable.getWord()+" hinzugefügt");
		//vocables.add(vocable);
		vocables.put(vocable.getWord(), vocable);
	}

	@Override
	public void updateVocable(Vocable vocable) throws DatabaseException {
		// TODO Auto-generated method stub
		vocables.put(vocable.getWord(), vocable);		
	}

	@Override
	public List<Vocable> query(String query)  {
		// TODO Auto-generated method stub
		//return new ArrayList<>();
		List<Vocable> list = new ArrayList<>(vocables.values());
		return list;
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
			String connectionTableName) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Connection> getAllConnectionsFor(Vocable vocable)
			throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Vocable> getTranslations(Vocable vocable) throws DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addConnectionTable(String connectionTableName)
			throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addConnection(Vocable from, Vocable to,
			String connectionTableName) throws DatabaseException {
		// TODO Auto-generated method stub
		
	}

}
