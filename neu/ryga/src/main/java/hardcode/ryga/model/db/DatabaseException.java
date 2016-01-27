package hardcode.ryga.model.db;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1332525141929678665L;

	public DatabaseException(String msg, Exception e){
		super(msg, e);
	}
	
	public DatabaseException(String msg){
		super(msg);
	}
	
	public DatabaseException(Exception e){
		super(e);
	}

	
}
