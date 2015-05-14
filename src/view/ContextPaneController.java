package view;

import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ContextPaneController {

	private TextField text;
	private VBox box;
	
	public ContextPaneController (TextField text, VBox box) {
		this.text = text;
		this.box = box;
		
		box.setVisible(false);
	}
	
	
	
}
