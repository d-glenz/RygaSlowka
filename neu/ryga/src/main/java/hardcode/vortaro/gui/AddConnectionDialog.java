package hardcode.vortaro.gui;

import hardcode.ryga.S;
import hardcode.ryga.model.Vocable;
import hardcode.ryga.model.Vocabulary;
import hardcode.ryga.model.VocabularyLookup;
import hardcode.vortaro.gui.listener.NewVocableListener;
import hardcode.vortaro.gui.listener.VocableSelectedListener;

import java.sql.SQLException;
import java.util.Optional;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class AddConnectionDialog extends Dialog {
	
	private Label lblWord;
	private ComboBox<String> combo;
	private Button btnAddConnection;
	private TextField txtWord;
	private VBox queryListContainer;
	
	private VBox vboxConnectTo;
	private Label lblConnectToWord;
	private ImageView imgViewConnectToLanguage;
	private Label lblConnecToNotes;
	
	private Button btnConnect;
	
	private QueryList queryList;
	private Vocabulary vocabulary;
	
	private Vocable currentConnectionToVocable;
	
	@SuppressWarnings("unchecked")
	public AddConnectionDialog(Vocable connectionFrom, Vocabulary vocab) {
		super(connectionFrom, true);
		
		this.vocabulary = vocab;
		
		lblWord = (Label) getNode("lblWord");
		lblWord.setText(connectionFrom.getWord());
		
		combo = (ComboBox<String>) getNode("combo");
		combo.getItems().setAll(vocab.getConnectionTableNamesWithTranslation());
		combo.getSelectionModel().select(0); //Translations is first
		
		btnAddConnection = (Button) getNode("btnAddConnection");
		btnAddConnection.setOnAction(e -> {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle(S.APP_NAME);
			dialog.setHeaderText("Create a new Connection for vocables");
			dialog.setContentText("Connection name:");
			Optional<String> result = dialog.showAndWait();
			if (result.isPresent()){
			    try {
					vocab.addConnectionTable(result.get());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		txtWord = (TextField) getNode("txtWord");
		txtWord.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
						try {
							queryList.query(newValue);
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			}
		});
		
		btnConnect = (Button) getNode("btnConnect");
		btnConnect.setOnAction(e -> {
			try {
				vocabulary.addConnection(getVocable(), currentConnectionToVocable, combo.getSelectionModel().getSelectedItem());
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
		});
		
		vboxConnectTo = (VBox) getNode("vboxConnectTo");
		lblConnectToWord = (Label) getNode("lblConnectToWord");
		imgViewConnectToLanguage = (ImageView) getNode("imgViewConnectToLanguage");
		lblConnecToNotes = (Label) getNode("lblConnecToNotes");
		
		
		queryListContainer = (VBox) getNode("queryListContainer");
		queryList = new QueryList((VocabularyLookup) vocab);
		queryList.setVocableSelectedListener(new VocableSelectedListener() {
			@Override
			public void selected(Vocable voc) {
				vocableSelected(voc);
			}
		});
		queryList.setNewVocableListener(new NewVocableListener() {
			@Override
			public void newVocable(String language) {
				addNewVocableFromQueryList(language);	
			}
		});
		queryListContainer.getChildren().add(queryList);
	}
	
	public void addNewVocableFromQueryList(String language) {
		String word = txtWord.getText();
		Vocable newVoc = vocabulary.createNewVocable();
		newVoc.setWord(word);
		newVoc.setLanguage(language);

		new VocableDialog(newVoc, true) {
			@Override
			public void onCancel() { /* nop */ }

			@Override
			public void onOk(Vocable vocable) {
				try {
					vocabulary.addVocable(vocable);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
	}
	
	private void vocableSelected(Vocable voc) {
		lblConnectToWord.setText(voc.getWord());
		imgViewConnectToLanguage.setImage(S.getFlagImage(voc.getLanguage()));
		lblConnecToNotes.setText(voc.getNotes());
		currentConnectionToVocable = voc;
	}
	
	@Override
	public void onOk(Vocable vocable) {
		
	}

	@Override
	public void onCancel() {
		
	}
	
	@Override
	public String getFxmlFilename() {
		return "AddConnectionDialog.fxml";
	}

}
