package hardcode.ryga.view.gui;

import hardcode.ryga.controller.S;
import hardcode.ryga.model.domain.Vocable;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class Dialog {

	private Button btnOk;
	private Button btnCancel;
	private ImageView imgViewLanguage;
	private Vocable vocable;

	private Scene scene;
	private Stage stage;

	public Dialog(Vocable vocable, boolean showImmediately) {
		this.vocable = vocable;
		
		try {
			FXMLLoader fxmlLoader = new FXMLLoader();
			VBox root = fxmlLoader.load(getClass().getResource(
					getFxmlFilename()).openStream());

			scene = new Scene(root, 400, 450);

			btnOk = (Button) scene.lookup("#btnOk");
			btnOk.setOnAction(e -> {
				onOkInternal();
			});

			btnCancel = (Button) scene.lookup("#btnCancel");
			btnCancel.setOnAction(e -> {
				onCancelInternal();
			});

			imgViewLanguage = (ImageView) scene.lookup("#imgViewLanguage");

		} catch (Exception e) {
			e.printStackTrace();
		}

		imgViewLanguage.setImage(S.getFlagImage(vocable.getLanguage()));
		
		if(showImmediately)
			show();
	}
	
	public Vocable getVocable() {
		return vocable;
	}
	
	public Node getNode(String name) {
		return scene.lookup("#" + name);
	}


	public void show() {
		stage = new Stage();
		stage.initModality(Modality.WINDOW_MODAL);
		// stage.initOwner(owner);
		stage.setScene(scene);
		stage.show();
	}

	private void onCancelInternal() {
		stage.hide();
		cancelHook();
		onCancel();
	}

	private void onOkInternal() {
		stage.hide();
		okHook();
		onOk(vocable);
	}

	public abstract String getFxmlFilename();
	
	public abstract void onOk(Vocable vocable);
	
	protected void okHook() {};

	public abstract void onCancel();
	
	protected void cancelHook() {};

}
