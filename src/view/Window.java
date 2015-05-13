package view;

import java.awt.Dimension;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Window {

	Map map;
	BorderPane rootLayout;
	Pane graphPane; 
	Dimension stageSize;

	public Dimension getStageSize() {
		return stageSize;
	}

	public Window(Stage stage) {
		rootLayout = loadFXML("/RootLayout.fxml");
		graphPane = (Pane) rootLayout.lookup("#graphPane");
		this.stageSize = prepareStage(stage);
	}

	public Dimension prepareStage(Stage stage) {
		map = new Map(rootLayout);
		graphPane.getChildren().add(map.getViz1());
		
		stage.setTitle("Displaying One JUNG Graph");
		stage.setMaximized(true);
		stage.setScene(map.getScene());
		stage.show();
		
		System.out.println(stage.getWidth());
		
		return new Dimension((int) stage.getWidth(), (int) stage.getHeight());
	}

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
	public Group getGroup() {
		return map.getViz1();
	}
	
	
}
