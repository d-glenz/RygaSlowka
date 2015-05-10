package application;

import java.awt.Dimension;
import java.io.IOException;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.TestGraphs;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;

/**
 * A sample showing how to use JUNG's layout classes to position vertices in a
 * graph.
 * 
 * @author jeffreyguenther
 * @author timheng
 */
public class JUNGAndJavaFX extends Application implements
		EventHandler<MouseEvent> {

	private Graph<Circle, Line> graph1;
	Layout<String, Number> circleLayout;
	Group viz1;
	double orgSceneX, orgSceneY, orgTranslateX, orgTranslateY;
	int stageWidth, stageHeight;

	enum LayoutType {
		FR, ISOM, KK, CIRCLE, SPRING
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

	@Override
	public void start(Stage stage) {
		BorderPane rootLayout = loadFXML("/RootLayout.fxml");

		Scene scene = new Scene(rootLayout, 800, 400, Color.WHITE);
		scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, this);
		scene.addEventHandler(MouseEvent.MOUSE_PRESSED, this);

		// create a group for the visualization
		viz1 = new Group();

		circleLayout = doJungStuff(LayoutType.CIRCLE,
				TestGraphs.getOneComponentGraph());

		// draw the graph
		drawGraph(graph1, viz1);

		rootLayout.getChildren().add(viz1);

		prepareStage(stage, scene);
	}

	private void prepareStage(Stage stage, Scene scene) {
		stage.setTitle("Displaying One JUNG Graph");
		stage.setMaximized(true);
		stage.setScene(scene);
		stage.show();
		stageWidth = (int) stage.getWidth();
		stageHeight = (int) stage.getHeight();
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

	public Layout<String, Number> doJungStuff(LayoutType lt,
			Graph<String, Number> temp) {
		// define the layout we want to use for the graph
		// The layout will be modified by the VisualizationModel
		Layout<String, Number> layout;

		switch (lt) {
		case FR:
			layout = new FRLayout<String, Number>(temp);
			break;
		case ISOM:
			layout = new ISOMLayout<String, Number>(temp);
			break;
		case KK:
			layout = new KKLayout<String, Number>(temp);
			break;
		case SPRING:
			layout = new SpringLayout<String, Number>(temp);
			break;
		default:
			layout = new CircleLayout<String, Number>(temp);
			break;
		}

		this.graph1 = GraphUtil.convertGraph(temp, layout, new CircleBuilder());

		// Define the visualization model. This is how JUNG calculates the
		// layout for the graph. It updates the layout object passed in.
		new DefaultVisualizationModel<>(layout, new Dimension(stageWidth,
				stageHeight));

		return layout;
	}

	/**
	 * Render a graph to a particular Group
	 * 
	 * This is purely JAVAFX Stuff
	 * 
	 * @param graph12
	 * @param circleLayout2
	 * @param viz
	 */
	public void drawGraph(Graph<Circle, Line> graph12, Group group) {
		for (Circle c : graph12.getVertices())
			if (!group.getChildren().contains(c))
				group.getChildren().add(c);

		for (Line l : graph12.getEdges())
			if (!group.getChildren().contains(l))
				group.getChildren().add(l);
	}

	/**
	 * Fallback to start FX application
	 * 
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
