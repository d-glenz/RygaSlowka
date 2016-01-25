package hardcode.vortaro.connectionPanes;

import hardcode.ryga.S;
import hardcode.ryga.model.Vocable;
import hardcode.ryga.ui.FxUtils;
import hardcode.vortaro.gui.listener.VocableActionHandler;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class ConnectionPane extends Pane {
	
	private VocableActionHandler handler;
	@FXML
	private VBox root;
	@FXML
	private HBox paneWord;
	@FXML
	private Label lblConnection;
	@FXML
	private Label lblWord;
	@FXML
	private Label lblNotes;
	@FXML
	private ImageView imgViewWord;
	@FXML
	private ImageView imgViewX;
	@FXML
	private Button btnConnect;
	
	public ConnectionPane(Vocable vocable, String connectionName) {
		FXMLLoader fxmlLoader = new FXMLLoader();
		VBox root = null;
		try {
			root = fxmlLoader.load(getClass().getResource("ConnectionPane.fxml").openStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		root.setOnMouseClicked(e -> {
			if(handler != null)
				handler.action(vocable);
		});
		
		paneWord = FxUtils.getChildByID(root, "paneWord");
		lblConnection = FxUtils.getChildByID(root, "lblConnection");
		lblWord = FxUtils.getChildByID(root, "lblWord");
		lblNotes = FxUtils.getChildByID(root, "lblNotes");
		imgViewWord = FxUtils.getChildByID(root, "imgViewWord");
		imgViewX = FxUtils.getChildByID(root, "imgViewX");
		
		lblConnection.setText(connectionName);
		lblWord.setText(vocable.getWord());
		lblNotes.setText(vocable.getNotes());
		imgViewWord.setImage(S.getFlagImage(vocable.getLanguage()));
		imgViewX.setImage(S.getCloseImage());
		imgViewX.setOnMouseClicked(e -> {
			//TODO
			System.out.println("REMOVE CONNECTION");
		});
		getChildren().add(root);
	}
	
	public void setActionHandler(VocableActionHandler handler) {
		this.handler = handler;
	}

}
