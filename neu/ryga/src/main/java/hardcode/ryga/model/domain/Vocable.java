package hardcode.ryga.model.domain;

public class Vocable {

	private String language;
	private String word;
	private String notes;
	private boolean changed;

	/**
	 * Creates a new, empty vocable. Contained fields are null, 'changed' is true;
	 */
	public Vocable() {
		changed = true;
	}
	
	public Vocable(String language, String word, String notes) {
		this.language = language;
		this.word = word;
		this.notes = notes;
		changed = false;
	}

	public String getLanguage() {
		return language;
	}

	public String getWord() {
		return word;
	}

	public String getNotes() {
		return notes;
	}
	
	

	public void setLanguage(String language) {
		this.language = language;
		changed = true;
	}

	public void setWord(String word) {
		this.word = word;
		changed = true;
	}

	public void setNotes(String notes) {
		this.notes = notes;
		changed = true;
	}
	
	public boolean isChanged() {
		return changed;
	}
	
	protected void setUnchanged() {
		changed = false;
	}

	@Override
	public String toString() {
		return word + " (" + language + "): " + notes + " [changed: " + changed + "]";
	}
}
