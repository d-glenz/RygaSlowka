package view;

import java.awt.Dimension;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

public class Map implements EventHandler<MouseEvent> {

	Scene scene;
	double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
	Dimension stageSize;
	Group viz1;
	
	public Map(BorderPane rootLayout){
		scene = new Scene(rootLayout, 800, 400, Color.WHITE);
		scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		viz1 = new Group();
	}
	
	@Override
	public void handle(MouseEvent arg0) {
		if (arg0.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
			viz1.setTranslateX(orgTranslateX + arg0.getSceneX() - orgSceneX);
			viz1.setTranslateY(orgTranslateY + arg0.getSceneY() - orgSceneY);
		} else if (arg0.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
			orgSceneX = arg0.getSceneX();
			orgSceneY = arg0.getSceneY();
			orgTranslateX = viz1.getTranslateX();
			orgTranslateY = viz1.getTranslateY();
		}
		arg0.consume();		
	}

	public Scene getScene() {
		return scene;
	}

	public Group getViz1() {
		return viz1;
	}

	
}
