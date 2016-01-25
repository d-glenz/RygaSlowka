package hardcode.vortaro.db;

import hardcode.ryga.S;
import hardcode.ryga.model.Vocable;
import hardcode.ryga.model.Vocabulary;
import hardcode.ryga.model.VocabularyLookup;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyDerbySQLDatabase extends DerbySQLDatabase implements Vocabulary, VocabularyLookup {
	
	public static String DATABASE_NAME = S.APP_NAME.toLowerCase() + "_db";
	public static String TABLE_VOCABULARY = "vocabulary";
	public static String TABLE_TRANSLATION = "translations";
	
	private static int MAX_WORD_LENGTH = 120;
	
	private List<String> connectionTableNames;
	
	public MyDerbySQLDatabase() throws SQLException {
		super(DATABASE_NAME);
		determineConnectionTableNames();
	}

	private void determineConnectionTableNames() throws SQLException {
		connectionTableNames = new ArrayList<>();
//		connectionTableNames.add(TABLE_TRANSLATION);
		ResultSet rs = select("SELECT tablename FROM sys.systables");
		while (rs.next()) {
			String tableName = rs.getString("tablename");
			if(!(tableName.toLowerCase().startsWith("sys")||
					tableName.equalsIgnoreCase(TABLE_VOCABULARY) ||
					tableName.equalsIgnoreCase(TABLE_TRANSLATION))) {
				connectionTableNames.add(tableName);
			}
		}
		
		for(String name : connectionTableNames) {
			System.out.println(name);
		}
	}
	
	@Override
	public List<String> getConnectionTableNames() {
		return connectionTableNames;		
	}
	
	@Override
	public List<String> getConnectionTableNamesWithTranslation() {
		List<String> l = new ArrayList<>(connectionTableNames);
		l.add(0, TABLE_TRANSLATION);
		return l;
	}
	
	private void insertVocable(String language, String word, String notes) throws SQLException {
		String sql = "INSERT INTO " + TABLE_VOCABULARY + " VALUES (?, ?, ?)";
		PreparedStatement ps = prepareStatement(sql);
		ps.setString(1, language);
		ps.setString(2, word);
		ps.setString(3, notes);
		ps.execute();
	}
	
	@Override
	public void addConnectionTable(String tableName) throws SQLException {
		if(tableName.toLowerCase().startsWith("sys")) {
			throw new SQLException("Table name must not start with 'sys'.");
		}
		
//		String create = "CREATE TABLE " + tableName + " ("
//				+ "firstId int NOT NULL,"
//				+ "secondId int NOT NULL,"
//				+ "CONSTRAINT primaryKey PRIMARY KEY ("
//					+ "firstId,"
//					+ "secondId"
//				+ "),"
//				+ "FOREIGN KEY (firstId) REFERENCES " + TABLE_VOCABULARY + " (id),"
//				+ "FOREIGN KEY (secondId) REFERENCES " + TABLE_VOCABULARY + " (id)"
//				+ ")";
		String create = "CREATE TABLE " + tableName + " ("
				+ "firstWord VARCHAR (" + MAX_WORD_LENGTH + ") NOT NULL,"
				+ "firstLang CHAR (2) NOT NULL,"
				+ "secondWord VARCHAR (" + MAX_WORD_LENGTH + ") NOT NULL,"
				+ "secondLang CHAR (2) NOT NULL,"
				+ "CONSTRAINT pk_" + tableName + " PRIMARY KEY (firstWord, firstLang, secondWord, secondLang),"
//				+ "FOREIGN KEY (firstWord) REFERENCES " + TABLE_VOCABULARY + " (word),"
//				+ "FOREIGN KEY (firstLang) REFERENCES " + TABLE_VOCABULARY + " (language),"
//				+ "FOREIGN KEY (secondWord) REFERENCES " + TABLE_VOCABULARY + " (word),"
//				+ "FOREIGN KEY (secondLang) REFERENCES " + TABLE_VOCABULARY + " (language)"
				+ "FOREIGN KEY (firstWord, firstLang) REFERENCES " + TABLE_VOCABULARY + " (word, language),"
				+ "FOREIGN KEY (secondWord, secondLang) REFERENCES " + TABLE_VOCABULARY + " (word, language)"
				+ ")";
		
		executeStatement(create);
		
		determineConnectionTableNames();
	}
	
	public void deleteConnectionTable(String tableName) throws SQLException {
		//TODO
	}
	
	@Override
	protected String initDatabase() throws SQLException {
		//String primKey = "id INT not null primary key GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),"; 
		
		String create = "CREATE TABLE " + TABLE_VOCABULARY + " ("
//				+ primKey
				+ "language CHAR (2), "
				+ "word VARCHAR(" + MAX_WORD_LENGTH + ") NOT NULL, "
				+ "notes VARCHAR(8000),"
				+ "PRIMARY KEY (word, language)"
				+ ")";
		
		executeStatement(create);
		
		System.out.println("Database created.");
		System.out.println("Creating translation connection table...");
		
		addConnectionTable(TABLE_TRANSLATION);
		
		System.out.println("Translation connection table created.");
		
		return null;
	}

	@Override
	public Vocable createNewVocable() {
		return new Vocable();
	}

	@Override
	public void addVocable(Vocable vocable) throws SQLException {
		insertVocable(vocable.getLanguage(), vocable.getWord(), vocable.getNotes());
	}

	@Override
	public void updateVocable(Vocable vocable) throws SQLException {
		updateVocableNotes(vocable.getLanguage(), vocable.getWord(), vocable.getNotes());
	}
	
	/**
	 * Updates the vocable's notes
	 * @param language
	 * @param word
	 * @param notes
	 * @throws SQLException
	 */
	private void updateVocableNotes(String language, String word, String notes) throws SQLException {
		String sql = "UPDATE " + TABLE_VOCABULARY + " SET notes = ? WHERE word = ? AND language = ?";
		PreparedStatement ps = prepareStatement(sql);
		ps.setString(1, notes);
		ps.setString(2, word);
		ps.setString(3, language);
		ps.execute();
	}

	@Override
	public List<Vocable> query(String query) throws SQLException {
		ResultSet currentResult = select("SELECT language, word, notes FROM vocabulary WHERE "
				+ "LOWER(word) LIKE LOWER('" + query + "%')");

		ArrayList<Vocable> vocables = new ArrayList<>();

		while (currentResult.next()) {
			String language = currentResult.getString("language");
			String word = currentResult.getString("word");
			String notes = currentResult.getString("notes");
			Vocable voc = new Vocable(language, word, notes);
			vocables.add(voc);
		}
		return vocables;
	}

	@Override
	public List<Vocable> getConnectionsFor(Vocable vocable, String connectionTableName) throws SQLException{
		ArrayList<Vocable> vocables = new ArrayList<>();
		
		//		"LOWER(word) LIKE LOWER('" + query + "%')"
		
		String query = "SELECT secondWord, secondLang FROM " 
				+ connectionTableName + " WHERE firstWord LIKE '" 
				+ vocable.getWord() + "' AND firstLang = '"
				+ vocable.getLanguage() + "'";
		
//		System.out.println(query);
		
		ResultSet rs1 = select(query);
		
		while(rs1.next()) {
			String word = rs1.getString("secondWord");
			String language = rs1.getString("secondLang");
			Vocable v = new Vocable(language, word, ""); //TODO: nicht fertig -- nur zum Testen
			vocables.add(v);
		}
		
		
//		ResultSet currentResult = select("SELECT " 
//				+ TABLE_VOCABULARY + ".language AS vlang, "
//				+ TABLE_VOCABULARY + ".word AS vword, "
//				+ TABLE_VOCABULARY + ".notes AS vnotes, "
//				+ connectionTableName + ".firstLang AS flang, "
//				+ connectionTableName + ".firstWord AS fword, "
//				+ connectionTableName + ".secondLang AS slang, "
//				+ connectionTableName + ".secondWord AS sword "
//				+ "FROM " + TABLE_VOCABULARY + " INNER JOIN " + connectionTableName + " ON "
//				+ connectionTableName + ".firstLang = " + TABLE_VOCABULARY + ".language AND "
//				+ connectionTableName + ".firstWord = " + TABLE_VOCABULARY + ".word ");
////				+ "INNER JOIN vlang ON "  

		
		

//		while (currentResult.next()) {
//			String vlang = currentResult.getString("vlang");
//			String vword = currentResult.getString("vword");
//			String vnotes = currentResult.getString("vnotes");
//			String flang = currentResult.getString("flang");
//			String fword = currentResult.getString("fword");
//			String slang = currentResult.getString("slang");
//			String sword = currentResult.getString("sword");
//			
//			System.out.println(vlang);
//			System.out.println(vword);
//			System.out.println(vnotes);
//			System.out.println(flang);
//			System.out.println(fword);
//			System.out.println(slang);
//			System.out.println(sword);
//			System.out.println("---");
//			
//			Vocable voc = new Vocable(vlang, vword, vnotes); //TODO: so falsch!
//			vocables.add(voc);
//		}
		
		return vocables;
	}

	@Override
	public void addConnection(Vocable from, Vocable to, String connectionTableName) throws SQLException{
		String sql = "INSERT INTO " + connectionTableName + " VALUES (?, ?, ?, ?)";
		PreparedStatement ps = prepareStatement(sql);
		ps.setString(1, from.getWord());
		ps.setString(2, from.getLanguage());
		ps.setString(3, to.getWord()); //FIXME: null, wenn ueber Connection-Dialog
		//eine neue Vokabel 'faul' erzeugt wird
		ps.setString(4, to.getLanguage());
		ps.execute();
	}

	@Override
	public List<Vocable> getTranslations(Vocable vocable) throws SQLException {
		return getConnectionsFor(vocable, TABLE_TRANSLATION);
	}

	@Override //TODO im Hintergrund bearbeiten und der Reihe nach einspeisen
	public List<Connection> getAllConnectionsFor(Vocable vocable) throws SQLException {
		List<Connection> connections = new ArrayList<>();
		for(String tableName : getConnectionTableNames()) {
			List<Vocable> vocables = getConnectionsFor(vocable, tableName);
			for(Vocable v : vocables) {
				Connection c = new Connection(tableName, v);
				connections.add(c);
			}
		}
		return connections;
	}

}
