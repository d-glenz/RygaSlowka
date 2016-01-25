package hardcode.ryga.view.gui;

import hardcode.ryga.model.domain.Vocable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public abstract class VocableDialog extends Dialog {

	private TextField txtWord;
	private TextArea txtNotes;
	private boolean wordEditable;

	public VocableDialog(Vocable vocable, boolean wordEditable) {
		super(vocable, true);
		this.wordEditable = wordEditable;
		txtWord = (TextField) getNode("txtWord");
		txtWord.setEditable(wordEditable);
		txtNotes = (TextArea) getNode("txtNotes");
		txtWord.setText(vocable.getWord());
		txtNotes.setText(vocable.getNotes());
	}

	@Override
	protected void okHook() {
		if (wordEditable) {
			String word = txtWord.getText();
			getVocable().setWord(word);
		}
		String notes = txtNotes.getText();
		getVocable().setNotes(notes);
	}

	public abstract void onOk(Vocable vocable);

	public abstract void onCancel();

	@Override
	public String getFxmlFilename() {
		return "VocableDialog.fxml";
	}
}
