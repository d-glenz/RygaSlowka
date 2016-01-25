package view;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

/**
 * Creates a JavaFX group, where the JUNG grap is painted on. 
 *
 */
public class GraphMapping implements EventHandler<MouseEvent> {

	private double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
	private Group graphVizualzation;
	
	/**
	 * 
	 * @param graphPane The pane to place the graph visualization in.
	 */
	public GraphMapping(Pane graphPane){
		graphPane.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		graphPane.addEventHandler(MouseEvent.MOUSE_PRESSED, this);
		graphVizualzation = new Group();
		graphPane.getChildren().add(graphVizualzation);
	}
	
	@Override
	public void handle(MouseEvent event) {
		if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
			graphVizualzation.setTranslateX(orgTranslateX + event.getSceneX() - orgSceneX);
			graphVizualzation.setTranslateY(orgTranslateY + event.getSceneY() - orgSceneY);
		} else if (event.getEventType().equals(MouseEvent.MOUSE_PRESSED)) {
			orgSceneX = event.getSceneX();
			orgSceneY = event.getSceneY();
			orgTranslateX = graphVizualzation.getTranslateX();
			orgTranslateY = graphVizualzation.getTranslateY();
		}
		event.consume();		
	}

	public Group getGraphVisualzation() {
		return graphVizualzation;
	}
}
