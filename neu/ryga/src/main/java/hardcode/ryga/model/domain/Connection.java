package hardcode.ryga.model.domain;


public class Connection {
	
	private String connectionName;
	private Vocable vocable;
		
	public Connection(String connectionName, Vocable vocable) {
		super();
		this.connectionName = connectionName;
		this.vocable = vocable;
	}
	
	public String getConnectionName() {
		return connectionName;
	}
	
	public Vocable getVocable() {
		return vocable;
	}
}
