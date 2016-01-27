package hardcode.ryga.view.gui;

import hardcode.ryga.controller.S;
import hardcode.ryga.model.db.sql.SQLDatabaseUtilities;
import hardcode.ryga.model.domain.Connection;
import hardcode.ryga.model.domain.Vocable;
import hardcode.ryga.model.domain.Vocabulary;
import hardcode.ryga.model.domain.VocabularyLookup;
import hardcode.ryga.view.ConnectionPane;
import hardcode.ryga.view.gui.listener.NewVocableListener;
import hardcode.ryga.view.gui.listener.VocableActionHandler;
import hardcode.ryga.view.gui.listener.VocableSelectedListener;
import hardcode.ryga.model.db.DatabaseException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Scene;
//import javafx.scene.control.Alert;
//import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import com.visulytic.jungfx.visualization.FXVisualizationViewer;

public class GuiController {

	@FXML
	private BorderPane root;
	@FXML
	private TextField txtQuery;
	@FXML
	private Label lblCurrentWord;
	@FXML
	private Label lblCurrentNotes;
	@FXML
	private ImageView imgViewCurrentLang;
	@FXML
	private ImageView imgViewCurrentEdit;
	@FXML
	private ImageView imgViewCurrentAddConnection;
	@FXML
	private ImageView imgViewCurrentAddTranslation;
	@FXML
	private ImageView imgViewTranslateLang;
	@FXML
	private Pane graphPane;
	@FXML
	private Button btnPrev;
	@FXML
	private Button btnNext;
	
	private QueryList queryList;
	private Vocabulary vocabulary;
	private Vocable currentVocable;
	
	private Stack<Vocable> history;

	@SuppressWarnings("unused")
	public void init(Vocabulary vocab) {
		history = new Stack<>();
		Scene scene = root.getScene();
//		System.out.println(scene);
		this.vocabulary = vocab;
		queryList = new QueryList((VocabularyLookup) vocab);
		queryList.setNewVocableListener(new NewVocableListener() {
			@Override
			public void newVocable(String language) {
				addNewVocableFromQueryList(language);
			}
		});

		queryList.setVocableSelectedListener(new VocableSelectedListener() {
			@Override
			public void selected(Vocable voc) {
				setCurrentVocable(voc);
			}
		});

		root.setLeft(queryList);

		txtQuery.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				queryDB(newValue);
			}
		});

		imgViewCurrentEdit.setImage(S.getEditImage());
		imgViewCurrentEdit.setOnMouseClicked(e -> {
			if (currentVocable == null) {
				return;
			}

			new VocableDialog(currentVocable, false) {

				@Override
				public void onOk(Vocable vocable) {
					try {
						vocab.updateVocable(vocable);
					} catch (DatabaseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub

				}
			};
		});
		
		imgViewCurrentAddConnection.setImage(S.getAddConnectionImage());
		imgViewCurrentAddConnection.setOnMouseClicked(e -> {
			if(currentVocable != null)
				new AddConnectionDialog(currentVocable,	vocab);
		});
		
		imgViewCurrentAddTranslation.setImage(S.getAddTranslationImage());
		imgViewCurrentAddTranslation.setOnMouseClicked(e -> {
			if(currentVocable != null)
				new AddConnectionDialog(currentVocable,	vocab);
		});
		
		btnPrev.setOnAction(e -> {
			prev();
		});
		
		//TODO
		btnNext.setVisible(false);
		btnNext.setOnAction(e -> {
			next();
		});
	}
	
	private void prev() {
		if(history.isEmpty()) 
			return;
		history.pop(); //current
		if(history.isEmpty()) 
			return;
		setCurrentVocable(history.pop());
	}
	
	private void next() {
		
	}

	private void queryDB(String query) { // TODO: von hier weg
		try {
			queryList.query(query);
		} catch (DatabaseException e) {
			warningMessage("Could not select in databse.",
					SQLDatabaseUtilities.prettifySQLException((SQLException)e.getCause())); //TODO: funktionierts?
		}
	}
	
	
	
//	private void setCurrentVocableAndAddToHistory(Vocable voc) {
//		setCurrentVocable(voc);
//		history.push(voc);
//	}
	
	private void setCurrentVocable(Vocable voc) {
		currentVocable = voc;
		lblCurrentWord.setText(voc.getWord());
		imgViewCurrentLang.setImage(S.getFlagImage(voc.getLanguage()));
		lblCurrentNotes.setText(voc.getNotes());
		searchForConnections(voc);
		history.push(voc);
	}

	private void searchForConnections(Vocable vocable) {
		//paneConnections.getChildren().clear();
		
		List<Vocable> translations = new ArrayList<>();
		try {
			translations = vocabulary.getTranslations(vocable);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		insertIntoConnectionPane(translations, "TRANSLATIONS"); //TODO: hier kein String, verweis auf Konstante
		
		List<Connection> connections = new ArrayList<>();
		try {
			connections = vocabulary.getAllConnectionsFor(vocable);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		insertIntoConnectionPane(connections);
		
	}
	
	private void insertIntoConnectionPane(List<Vocable> vocables, String connectionName) {
		if (vocables == null) {
			return;
		}
		for(Vocable v : vocables) {
			insertIntoConnectionPane(v, connectionName);
		}
	}
	
	private void insertIntoConnectionPane(List<Connection> connections) {
		if (connections == null) {
			return;
		}
		for(Connection c : connections) {
			insertIntoConnectionPane(c.getVocable(), c.getConnectionName());
		}
	}
	
	private void insertIntoConnectionPane(Vocable vocable, String connectionName) {
		ConnectionPane cp = new ConnectionPane(vocable, connectionName);
		cp.setActionHandler(new VocableActionHandler() {
			@Override
			public void action(Vocable vocable) {
				setCurrentVocable(vocable);
			}
		});
		//paneConnections.getChildren().add(cp);
		//TODO
	}

	public void addNewVocableFromQueryList(String language) {
		String word = txtQuery.getText();
		Vocable newVoc = vocabulary.createNewVocable();
		newVoc.setWord(word);
		newVoc.setLanguage(language);

		new VocableDialog(newVoc, true) {
			@Override
			public void onCancel() {
				// nop
			}

			@Override
			public void onOk(Vocable vocable) {
				try {
					vocabulary.addVocable(vocable);
				} catch (DatabaseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

	}
	
	public void insertGraph(FXVisualizationViewer<?, ?> graph) {
		graphPane.getChildren().add(graph);
	}

	private void warningMessage(String header, String msg) {
//		Alert alert = new Alert(AlertType.WARNING);
//		alert.setTitle(S.APP_NAME);
//		alert.setHeaderText(header);
//		alert.setContentText(msg);
//		alert.showAndWait();
	}
}
