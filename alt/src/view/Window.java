package view;

import java.awt.Dimension;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Window {

	private GraphMapping mapping;
	private Dimension stageSize;
	
	private Scene scene;
	private BorderPane rootLayout;
	private Pane graphPane; 
	private TextField inputTextField;
	private VBox contextPane;
	private ContextPaneController ctxPaneCtr;
	

	public Dimension getStageSize() {
		return stageSize;
	}

	public Window(Stage stage) {
		//lookup UI elements
		rootLayout = loadFXML("/RootLayout.fxml");
		graphPane = (Pane) rootLayout.lookup("#graphPane");
		inputTextField = (TextField) rootLayout.lookup("#inputTxt");
		contextPane = (VBox) rootLayout.lookup("#contextPane");
		
		//controls the behavior of the text field and context pane 
		ctxPaneCtr = new ContextPaneController(inputTextField, contextPane);
		
		//make the scene
		scene = new Scene(rootLayout, 800, 400, Color.WHITE);
		scene.getStylesheets().add("/style.css");
		
		//create graph visualization and place it on the graphPane
		mapping = new GraphMapping(graphPane);
		
		//prepare and show stage
		stage.setTitle("Displaying One JUNG Graph");
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
		
		stageSize = new Dimension((int) stage.getWidth(), (int) stage.getHeight());
	}

	/**
	 * Loads the application's root pane from an FXML file.
	 * @param fname FXML file name
	 * @return
	 */
	private BorderPane loadFXML(String fname) {
		BorderPane rootLayout = null;

		FXMLLoader fxmlLoader = new FXMLLoader();
		try {
			fxmlLoader.setLocation(getClass().getResource(fname));
			rootLayout = (BorderPane) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rootLayout;
	}

	/**
	 * @return JavaFX Group the Jung graph is painted on
	 */
	public Group getGraphVisualzation() {
		return mapping.getGraphVisualzation();
	}
	
	
}
